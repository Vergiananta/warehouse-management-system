package com.example.warehouse.warehouse_service.mapper;

import com.example.warehouse.warehouse_service.dto.response.OrderItemResponse;
import com.example.warehouse.warehouse_service.dto.response.OrderResponse;
import com.example.warehouse.warehouse_service.entity.Order;
import com.example.warehouse.warehouse_service.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class OrderMapper {

    public OrderItemResponse toItemResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .itemId(orderItem.getItem().getId())
                .itemSku(orderItem.getItem().getSku())
                .itemName(orderItem.getItem().getName())
                .variantId(orderItem.getVariant() != null ? orderItem.getVariant().getId() : null)
                .variantSku(orderItem.getVariant() != null ? orderItem.getVariant().getSku() : null)
                .variantName(orderItem.getVariant() != null ? orderItem.getVariant().getName() : null)
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .subtotal(orderItem.getSubtotal())
                .build();
    }

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems() != null
                ? order.getItems().stream().map(this::toItemResponse).toList()
                : Collections.emptyList();

        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().name())
                .items(itemResponses)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
