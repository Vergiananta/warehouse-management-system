package com.example.warehouse.warehouse_service.service;

import com.example.warehouse.warehouse_service.dto.request.OrderCreateRequest;
import com.example.warehouse.warehouse_service.dto.request.OrderItemRequest;
import com.example.warehouse.warehouse_service.dto.response.OrderResponse;
import com.example.warehouse.warehouse_service.entity.Item;
import com.example.warehouse.warehouse_service.entity.ItemVariant;
import com.example.warehouse.warehouse_service.entity.Order;
import com.example.warehouse.warehouse_service.entity.OrderStatus;
import com.example.warehouse.warehouse_service.exception.InsufficientStockException;
import com.example.warehouse.warehouse_service.exception.ResourceNotFoundException;
import com.example.warehouse.warehouse_service.mapper.OrderMapper;
import com.example.warehouse.warehouse_service.repository.ItemRepository;
import com.example.warehouse.warehouse_service.repository.ItemVariantRepository;
import com.example.warehouse.warehouse_service.repository.OrderRepository;
import com.example.warehouse.warehouse_service.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemVariantRepository itemVariantRepository;

    @Spy
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Item item;
    private ItemVariant variant;
    private Order order;

    @BeforeEach
    void setUp() {
        item = Item.builder()
                .id(1L)
                .sku("SKU-001")
                .name("Item 1")
                .price(new BigDecimal("100.00"))
                .stockQuantity(10)
                .build();

        variant = ItemVariant.builder()
                .id(10L)
                .item(item)
                .sku("VAR-001")
                .name("Item 1 - Red")
                .price(new BigDecimal("120.00"))
                .stockQuantity(5)
                .build();

        order = Order.builder()
                .id(100L)
                .orderNumber("ORD-12345678")
                .status(OrderStatus.COMPLETED)
                .totalAmount(new BigDecimal("200.00"))
                .build();
    }

    @Test
    void createOrder_WithBaseItem_Success() {
        OrderItemRequest itemRequest = OrderItemRequest.builder()
                .itemId(1L)
                .quantity(2)
                .build();

        OrderCreateRequest request = OrderCreateRequest.builder()
                .items(List.of(itemRequest))
                .build();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setId(100L);
            return o;
        });

        OrderResponse response = orderService.createOrder(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("200.00"), response.getTotalAmount());
        assertEquals(8, item.getStockQuantity());
        verify(itemRepository).save(item);
    }

    @Test
    void createOrder_WithVariant_Success() {
        OrderItemRequest itemRequest = OrderItemRequest.builder()
                .itemId(1L)
                .variantId(10L)
                .quantity(3)
                .build();

        OrderCreateRequest request = OrderCreateRequest.builder()
                .items(List.of(itemRequest))
                .build();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemVariantRepository.findByIdAndItemId(10L, 1L)).thenReturn(Optional.of(variant));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setId(100L);
            return o;
        });

        OrderResponse response = orderService.createOrder(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("360.00"), response.getTotalAmount());
        assertEquals(2, variant.getStockQuantity());
        verify(itemVariantRepository).save(variant);
    }

    @Test
    void createOrder_ItemInsufficientStock_ThrowsException() {
        OrderItemRequest itemRequest = OrderItemRequest.builder()
                .itemId(1L)
                .quantity(15)
                .build();

        OrderCreateRequest request = OrderCreateRequest.builder()
                .items(List.of(itemRequest))
                .build();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(InsufficientStockException.class, () -> orderService.createOrder(request));
    }

    @Test
    void createOrder_VariantInsufficientStock_ThrowsException() {
        OrderItemRequest itemRequest = OrderItemRequest.builder()
                .itemId(1L)
                .variantId(10L)
                .quantity(10)
                .build();

        OrderCreateRequest request = OrderCreateRequest.builder()
                .items(List.of(itemRequest))
                .build();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemVariantRepository.findByIdAndItemId(10L, 1L)).thenReturn(Optional.of(variant));

        assertThrows(InsufficientStockException.class, () -> orderService.createOrder(request));
    }

    @Test
    void createOrder_ItemNotFound_ThrowsException() {
        OrderItemRequest itemRequest = OrderItemRequest.builder()
                .itemId(99L)
                .quantity(1)
                .build();

        OrderCreateRequest request = OrderCreateRequest.builder()
                .items(List.of(itemRequest))
                .build();

        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(request));
    }

    @Test
    void getAllOrders_Success() {
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderResponse> responses = orderService.getAllOrders();

        assertEquals(1, responses.size());
        assertEquals("ORD-12345678", responses.get(0).getOrderNumber());
    }

    @Test
    void getOrderById_Success() {
        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));

        OrderResponse response = orderService.getOrderById(100L);

        assertNotNull(response);
        assertEquals(100L, response.getId());
    }

    @Test
    void getOrderById_NotFound_ThrowsException() {
        when(orderRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(100L));
    }
}
