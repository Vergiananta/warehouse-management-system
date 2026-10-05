package com.example.warehouse.warehouse_service.controller;

import com.example.warehouse.warehouse_service.dto.request.OrderCreateRequest;
import com.example.warehouse.warehouse_service.dto.request.OrderItemRequest;
import com.example.warehouse.warehouse_service.dto.response.ApiResponse;
import com.example.warehouse.warehouse_service.dto.response.OrderResponse;
import com.example.warehouse.warehouse_service.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        orderResponse = OrderResponse.builder()
                .id(1L)
                .orderNumber("ORD-12345678")
                .totalAmount(new BigDecimal("250.00"))
                .status("COMPLETED")
                .build();
    }

    @Test
    void createOrder_ReturnsCreatedResponse() {
        OrderItemRequest itemRequest = OrderItemRequest.builder()
                .itemId(1L)
                .quantity(2)
                .build();

        OrderCreateRequest request = OrderCreateRequest.builder()
                .items(List.of(itemRequest))
                .build();

        when(orderService.createOrder(request)).thenReturn(orderResponse);

        ResponseEntity<ApiResponse<OrderResponse>> response = orderController.createOrder(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("ORD-12345678", response.getBody().getData().getOrderNumber());
    }

    @Test
    void getAllOrders_ReturnsOkResponse() {
        when(orderService.getAllOrders()).thenReturn(List.of(orderResponse));

        ResponseEntity<ApiResponse<List<OrderResponse>>> response = orderController.getAllOrders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void getOrderById_ReturnsOkResponse() {
        when(orderService.getOrderById(1L)).thenReturn(orderResponse);

        ResponseEntity<ApiResponse<OrderResponse>> response = orderController.getOrderById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getData().getId());
    }
}
