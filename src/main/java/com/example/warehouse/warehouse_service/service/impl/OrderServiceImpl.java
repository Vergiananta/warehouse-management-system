package com.example.warehouse.warehouse_service.service.impl;

import com.example.warehouse.warehouse_service.dto.request.OrderCreateRequest;
import com.example.warehouse.warehouse_service.dto.request.OrderItemRequest;
import com.example.warehouse.warehouse_service.dto.response.OrderResponse;
import com.example.warehouse.warehouse_service.entity.Item;
import com.example.warehouse.warehouse_service.entity.ItemVariant;
import com.example.warehouse.warehouse_service.entity.Order;
import com.example.warehouse.warehouse_service.entity.OrderItem;
import com.example.warehouse.warehouse_service.entity.OrderStatus;
import com.example.warehouse.warehouse_service.exception.InsufficientStockException;
import com.example.warehouse.warehouse_service.exception.ResourceNotFoundException;
import com.example.warehouse.warehouse_service.mapper.OrderMapper;
import com.example.warehouse.warehouse_service.repository.ItemRepository;
import com.example.warehouse.warehouse_service.repository.ItemVariantRepository;
import com.example.warehouse.warehouse_service.repository.OrderRepository;
import com.example.warehouse.warehouse_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final ItemVariantRepository itemVariantRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {
        String orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Order order = Order.builder()
                .orderNumber(orderNumber)
                .status(OrderStatus.COMPLETED)
                .totalAmount(BigDecimal.ZERO)
                .items(new ArrayList<>())
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : request.getItems()) {
            Item item = itemRepository.findById(itemRequest.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Item with id " + itemRequest.getItemId() + " not found"));

            ItemVariant variant = null;
            BigDecimal unitPrice;

            if (itemRequest.getVariantId() != null) {
                variant = itemVariantRepository.findByIdAndItemId(itemRequest.getVariantId(), item.getId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Variant with id " + itemRequest.getVariantId() + " not found for item " + item.getId()));

                if (variant.getStockQuantity() < itemRequest.getQuantity()) {
                    throw new InsufficientStockException(
                            "Insufficient stock for variant '" + variant.getName() +
                                    "' (SKU: " + variant.getSku() + "). Available: " +
                                    variant.getStockQuantity() + ", Requested: " + itemRequest.getQuantity());
                }

                variant.setStockQuantity(variant.getStockQuantity() - itemRequest.getQuantity());
                itemVariantRepository.save(variant);
                unitPrice = variant.getPrice();
            } else {
                if (item.getStockQuantity() < itemRequest.getQuantity()) {
                    throw new InsufficientStockException(
                            "Insufficient stock for item '" + item.getName() +
                                    "' (SKU: " + item.getSku() + "). Available: " +
                                    item.getStockQuantity() + ", Requested: " + itemRequest.getQuantity());
                }

                item.setStockQuantity(item.getStockQuantity() - itemRequest.getQuantity());
                itemRepository.save(item);
                unitPrice = item.getPrice();
            }

            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(subtotal);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .item(item)
                    .variant(variant)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .build();

            orderItems.add(orderItem);
        }

        order.setTotalAmount(totalAmount);
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + id + " not found"));
        return orderMapper.toResponse(order);
    }
}
