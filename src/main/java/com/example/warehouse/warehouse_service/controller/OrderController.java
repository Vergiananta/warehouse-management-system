package com.example.warehouse.warehouse_service.controller;

import com.example.warehouse.warehouse_service.dto.request.OrderCreateRequest;
import com.example.warehouse.warehouse_service.dto.response.ApiResponse;
import com.example.warehouse.warehouse_service.dto.response.OrderResponse;
import com.example.warehouse.warehouse_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        List<OrderResponse> response = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long id) {
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success("Order retrieved successfully", response));
    }
}
