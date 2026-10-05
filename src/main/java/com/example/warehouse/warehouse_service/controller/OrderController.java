package com.example.warehouse.warehouse_service.controller;

import com.example.warehouse.warehouse_service.dto.request.OrderCreateRequest;
import com.example.warehouse.warehouse_service.dto.response.ApiResponse;
import com.example.warehouse.warehouse_service.dto.response.OrderResponse;
import com.example.warehouse.warehouse_service.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management APIs")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new order", description = "Membuat pesanan baru serta otomatis mengurangi stok item/varian dan memvalidasi stok")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Pesanan berhasil dibuat",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "CreateOrderSuccessResponse",
                                    value = "{\n  \"success\": true,\n  \"message\": \"Order created successfully\",\n  \"data\": {\n    \"id\": 1,\n    \"orderNumber\": \"ORD-20261005-0001\",\n    \"totalAmount\": 730000.00,\n    \"status\": \"COMPLETED\",\n    \"items\": [\n      {\n        \"id\": 1,\n        \"itemId\": 1,\n        \"itemSku\": \"ITEM-001\",\n        \"itemName\": \"Wireless Mechanical Keyboard\",\n        \"variantId\": 1,\n        \"variantSku\": \"ITEM-001-RED\",\n        \"variantName\": \"Red Linear Switch\",\n        \"quantity\": 2,\n        \"unitPrice\": 365000.00,\n        \"subtotal\": 730000.00\n      }\n    ],\n    \"createdAt\": \"2026-10-05T22:00:00\",\n    \"updatedAt\": \"2026-10-05T22:00:00\"\n  },\n  \"timestamp\": \"2026-10-05T22:00:00\"\n}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Permintaan tidak valid atau stok barang tidak mencukupi",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "InsufficientStockResponse",
                                    value = "{\n  \"success\": false,\n  \"message\": \"Insufficient stock for item variant 'Red Linear Switch'. Available: 1, Requested: 2\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T22:00:00\"\n}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Item atau varian pesanan tidak ditemukan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ItemNotFoundResponse",
                                    value = "{\n  \"success\": false,\n  \"message\": \"Item not found with id: 99\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T22:00:00\"\n}"
                            )
                    )
            )
    })
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @org.springframework.web.bind.annotation.RequestBody
            @RequestBody(
                    description = "Data item dan kuantitas pesanan",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "CreateOrderExample",
                                    value = "{\n  \"items\": [\n    {\n      \"itemId\": 1,\n      \"variantId\": 1,\n      \"quantity\": 2\n    }\n  ]\n}"
                            )
                    )
            ) OrderCreateRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Mengambil seluruh riwayat pesanan")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Daftar pesanan berhasil diambil",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "GetAllOrdersResponse",
                                    value = "{\n  \"success\": true,\n  \"message\": \"Orders retrieved successfully\",\n  \"data\": [\n    {\n      \"id\": 1,\n      \"orderNumber\": \"ORD-20261005-0001\",\n      \"totalAmount\": 730000.00,\n      \"status\": \"COMPLETED\",\n      \"items\": [\n        {\n          \"id\": 1,\n          \"itemId\": 1,\n          \"itemSku\": \"ITEM-001\",\n          \"itemName\": \"Wireless Mechanical Keyboard\",\n          \"variantId\": 1,\n          \"variantSku\": \"ITEM-001-RED\",\n          \"variantName\": \"Red Linear Switch\",\n          \"quantity\": 2,\n          \"unitPrice\": 365000.00,\n          \"subtotal\": 730000.00\n        }\n      ],\n      \"createdAt\": \"2026-10-05T22:00:00\",\n      \"updatedAt\": \"2026-10-05T22:00:00\"\n    }\n  ],\n  \"timestamp\": \"2026-10-05T22:00:00\"\n}"
                            )
                    )
            )
    })
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        List<OrderResponse> response = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Mengambil detail pesanan berdasarkan ID pesanan")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Detail pesanan ditemukan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "GetOrderByIdResponse",
                                    value = "{\n  \"success\": true,\n  \"message\": \"Order retrieved successfully\",\n  \"data\": {\n    \"id\": 1,\n    \"orderNumber\": \"ORD-20261005-0001\",\n    \"totalAmount\": 730000.00,\n    \"status\": \"COMPLETED\",\n    \"items\": [\n      {\n        \"id\": 1,\n        \"itemId\": 1,\n        \"itemSku\": \"ITEM-001\",\n        \"itemName\": \"Wireless Mechanical Keyboard\",\n        \"variantId\": 1,\n        \"variantSku\": \"ITEM-001-RED\",\n        \"variantName\": \"Red Linear Switch\",\n        \"quantity\": 2,\n        \"unitPrice\": 365000.00,\n        \"subtotal\": 730000.00\n      }\n    ],\n    \"createdAt\": \"2026-10-05T22:00:00\",\n    \"updatedAt\": \"2026-10-05T22:00:00\"\n  },\n  \"timestamp\": \"2026-10-05T22:00:00\"\n}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Pesanan tidak ditemukan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "NotFoundResponse",
                                    value = "{\n  \"success\": false,\n  \"message\": \"Order not found with id: 99\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T22:00:00\"\n}"
                            )
                    )
            )
    })
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long id) {
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success("Order retrieved successfully", response));
    }
}
