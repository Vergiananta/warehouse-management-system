package com.example.warehouse.warehouse_service.controller;

import com.example.warehouse.warehouse_service.dto.request.ItemCreateRequest;
import com.example.warehouse.warehouse_service.dto.request.ItemUpdateRequest;
import com.example.warehouse.warehouse_service.dto.response.ApiResponse;
import com.example.warehouse.warehouse_service.dto.response.ItemResponse;
import com.example.warehouse.warehouse_service.service.ItemService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Tag(name = "Items", description = "Item management APIs")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @Operation(summary = "Create a new item", description = "Menambahkan data item baru ke dalam inventaris gudang")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Item berhasil dibuat",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "SuccessResponse",
                                    value = "{\n  \"success\": true,\n  \"message\": \"Item created successfully\",\n  \"data\": {\n    \"id\": 1,\n    \"sku\": \"ITEM-001\",\n    \"name\": \"Wireless Mechanical Keyboard\",\n    \"description\": \"Keyboard mechanical wireless 75% dengan RGB\",\n    \"price\": 350000.00,\n    \"stockQuantity\": 50,\n    \"variants\": [],\n    \"createdAt\": \"2026-10-05T20:00:00\",\n    \"updatedAt\": \"2026-10-05T20:00:00\"\n  },\n  \"timestamp\": \"2026-10-05T20:00:00\"\n}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Permintaan tidak valid (validasi gagal)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ValidationErrorResponse",
                                    value = "{\n  \"success\": false,\n  \"message\": \"Validation failed: name: Name cannot be blank\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T20:00:00\"\n}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "SKU item sudah terdaftar",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "DuplicateErrorResponse",
                                    value = "{\n  \"success\": false,\n  \"message\": \"Item with SKU 'ITEM-001' already exists\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T20:00:00\"\n}"
                            )
                    )
            )
    })
    public ResponseEntity<ApiResponse<ItemResponse>> createItem(

            @Valid @org.springframework.web.bind.annotation.RequestBody
            @RequestBody(
                    description = "Data item baru yang akan dibuat",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "CreateItemExample",
                                    value = "{\n  \"sku\": \"ITEM-001\",\n  \"name\": \"Wireless Mechanical Keyboard\",\n  \"description\": \"Keyboard mechanical wireless 75% dengan RGB\",\n  \"price\": 350000.00,\n  \"stockQuantity\": 50\n}"
                            )
                    )
            ) ItemCreateRequest request) {
        ItemResponse response = itemService.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(com.example.warehouse.warehouse_service.dto.response.ApiResponse.success("Item created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all items", description = "Mengambil daftar seluruh item yang ada di inventaris")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Daftar item berhasil diambil",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "GetAllItemsResponse",
                                    value = "{\n  \"success\": true,\n  \"message\": \"Items retrieved successfully\",\n  \"data\": [\n    {\n      \"id\": 1,\n      \"sku\": \"ITEM-001\",\n      \"name\": \"Wireless Mechanical Keyboard\",\n      \"description\": \"Keyboard mechanical wireless 75% dengan RGB\",\n      \"price\": 350000.00,\n      \"stockQuantity\": 50,\n      \"variants\": [\n        {\n          \"id\": 1,\n          \"itemId\": 1,\n          \"sku\": \"ITEM-001-RED\",\n          \"name\": \"Red Linear Switch\",\n          \"price\": 365000.00,\n          \"stockQuantity\": 20,\n          \"createdAt\": \"2026-10-05T20:30:00\",\n          \"updatedAt\": \"2026-10-05T20:30:00\"\n        }\n      ],\n      \"createdAt\": \"2026-10-05T20:00:00\",\n      \"updatedAt\": \"2026-10-05T20:00:00\"\n    }\n  ],\n  \"timestamp\": \"2026-10-05T20:00:00\"\n}"
                            )
                    )
            )
    })
    public ResponseEntity<ApiResponse<List<ItemResponse>>> getAllItems() {
        List<ItemResponse> response = itemService.getAllItems();
        return ResponseEntity.ok(ApiResponse.success("Items retrieved successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get item by ID", description = "Mengambil detail item berdasarkan ID item")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Detail item ditemukan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "GetItemByIdResponse",
                                    value = "{\n  \"success\": true,\n  \"message\": \"Item retrieved successfully\",\n  \"data\": {\n    \"id\": 1,\n    \"sku\": \"ITEM-001\",\n    \"name\": \"Wireless Mechanical Keyboard\",\n    \"description\": \"Keyboard mechanical wireless 75% dengan RGB\",\n    \"price\": 350000.00,\n    \"stockQuantity\": 50,\n    \"variants\": [],\n    \"createdAt\": \"2026-10-05T20:00:00\",\n    \"updatedAt\": \"2026-10-05T20:00:00\"\n  },\n  \"timestamp\": \"2026-10-05T20:00:00\"\n}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Item tidak ditemukan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "NotFoundResponse",
                                    value = "{\n  \"success\": false,\n  \"message\": \"Item not found with id: 99\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T20:00:00\"\n}"
                            )
                    )
            )
    })
    public ResponseEntity<ApiResponse<ItemResponse>> getItemById(@PathVariable Long id) {
        ItemResponse response = itemService.getItemById(id);
        return ResponseEntity.ok(ApiResponse.success("Item retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an item by ID", description = "Memperbarui informasi item berdasarkan ID item")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Item berhasil diperbarui",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "UpdateItemSuccessResponse",
                                    value = "{\n  \"success\": true,\n  \"message\": \"Item updated successfully\",\n  \"data\": {\n    \"id\": 1,\n    \"sku\": \"ITEM-001\",\n    \"name\": \"Wireless Mechanical Keyboard Pro\",\n    \"description\": \"Keyboard mechanical wireless 75% dengan RGB dan hot-swappable switches\",\n    \"price\": 380000.00,\n    \"stockQuantity\": 75,\n    \"variants\": [],\n    \"createdAt\": \"2026-10-05T20:00:00\",\n    \"updatedAt\": \"2026-10-05T21:00:00\"\n  },\n  \"timestamp\": \"2026-10-05T21:00:00\"\n}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Permintaan tidak valid (validasi gagal)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ValidationErrorResponse",
                                    value = "{\n  \"success\": false,\n  \"message\": \"Validation failed: price: Price must be greater than or equal to 0\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T21:00:00\"\n}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Item tidak ditemukan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "NotFoundResponse",
                                    value = "{\n  \"success\": false,\n  \"message\": \"Item not found with id: 99\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T21:00:00\"\n}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "SKU item sudah digunakan oleh item lain",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "DuplicateErrorResponse",
                                    value = "{\n  \"success\": false,\n  \"message\": \"Item with SKU 'ITEM-002' already exists\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T21:00:00\"\n}"
                            )
                    )
            )
    })
    public ResponseEntity<ApiResponse<ItemResponse>> updateItem(
            @PathVariable Long id,
            @Valid @org.springframework.web.bind.annotation.RequestBody
            @RequestBody(
                    description = "Data pembaruan item",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemUpdateRequest.class),
                            examples = @ExampleObject(
                                    name = "UpdateItemExample",
                                    value = "{\n  \"sku\": \"ITEM-001\",\n  \"name\": \"Wireless Mechanical Keyboard Pro\",\n  \"description\": \"Keyboard mechanical wireless 75% dengan RGB dan hot-swappable switches\",\n  \"price\": 380000.00,\n  \"stockQuantity\": 75\n}"
                            )
                    )
            ) ItemUpdateRequest request) {
        ItemResponse response = itemService.updateItem(id, request);
        return ResponseEntity.ok(ApiResponse.success("Item updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an item by ID", description = "Menghapus item dari inventaris berdasarkan ID item")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Item berhasil dihapus",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "DeleteItemSuccessResponse",
                                    value = "{\n  \"success\": true,\n  \"message\": \"Item deleted successfully\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T21:00:00\"\n}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Item tidak ditemukan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "NotFoundResponse",
                                    value = "{\n  \"success\": false,\n  \"message\": \"Item not found with id: 99\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T21:00:00\"\n}"
                            )
                    )
            )
    })
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok(ApiResponse.success("Item deleted successfully", null));
    }
}



