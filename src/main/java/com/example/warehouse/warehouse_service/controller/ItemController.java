package com.example.warehouse.warehouse_service.controller;

import com.example.warehouse.warehouse_service.dto.request.ItemCreateRequest;
import com.example.warehouse.warehouse_service.dto.request.ItemUpdateRequest;
import com.example.warehouse.warehouse_service.dto.response.ApiResponse;
import com.example.warehouse.warehouse_service.dto.response.ItemResponse;
import com.example.warehouse.warehouse_service.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.RequestBody;
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
    @Operation(summary = "Create a new item")
    public ResponseEntity<ApiResponse<ItemResponse>> createItem(@Valid @RequestBody ItemCreateRequest request) {
        ItemResponse response = itemService.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Item created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all items")
    public ResponseEntity<ApiResponse<List<ItemResponse>>> getAllItems() {
        List<ItemResponse> response = itemService.getAllItems();
        return ResponseEntity.ok(ApiResponse.success("Items retrieved successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get item by ID")
    public ResponseEntity<ApiResponse<ItemResponse>> getItemById(@PathVariable Long id) {
        ItemResponse response = itemService.getItemById(id);
        return ResponseEntity.ok(ApiResponse.success("Item retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an item by ID")
    public ResponseEntity<ApiResponse<ItemResponse>> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody ItemUpdateRequest request) {
        ItemResponse response = itemService.updateItem(id, request);
        return ResponseEntity.ok(ApiResponse.success("Item updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an item by ID")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok(ApiResponse.success("Item deleted successfully", null));
    }
}

