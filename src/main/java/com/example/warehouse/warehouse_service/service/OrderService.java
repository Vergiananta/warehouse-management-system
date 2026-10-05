package com.example.warehouse.warehouse_service.service;

import com.example.warehouse.warehouse_service.dto.request.OrderCreateRequest;
import com.example.warehouse.warehouse_service.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderCreateRequest request);
    List<OrderResponse> getAllOrders();
    OrderResponse getOrderById(Long id);
}
