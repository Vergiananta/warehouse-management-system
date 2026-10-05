package com.example.warehouse.warehouse_service.controller;

import com.example.warehouse.warehouse_service.dto.request.ItemVariantCreateRequest;
import com.example.warehouse.warehouse_service.dto.request.ItemVariantUpdateRequest;
import com.example.warehouse.warehouse_service.dto.response.ApiResponse;
import com.example.warehouse.warehouse_service.dto.response.ItemVariantResponse;
import com.example.warehouse.warehouse_service.service.ItemVariantService;
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
@RequestMapping("/api/items/{itemId}/variants")
@RequiredArgsConstructor
@Tag(name = "Item Variants", description = "Item variant management APIs")
public class ItemVariantController {

    private final ItemVariantService itemVariantService;

    @PostMapping
    @Operation(summary = "Create a new variant for an item")
    public ResponseEntity<ApiResponse<ItemVariantResponse>> createVariant(
            @PathVariable Long itemId,
            @Valid @RequestBody ItemVariantCreateRequest request) {
        ItemVariantResponse response = itemVariantService.createVariant(itemId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Variant created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all variants of an item")
    public ResponseEntity<ApiResponse<List<ItemVariantResponse>>> getVariantsByItemId(@PathVariable Long itemId) {
        List<ItemVariantResponse> response = itemVariantService.getVariantsByItemId(itemId);
        return ResponseEntity.ok(ApiResponse.success("Variants retrieved successfully", response));
    }

    @GetMapping("/{variantId}")
    @Operation(summary = "Get variant by ID")
    public ResponseEntity<ApiResponse<ItemVariantResponse>> getVariantById(
            @PathVariable Long itemId,
            @PathVariable Long variantId) {
        ItemVariantResponse response = itemVariantService.getVariantById(itemId, variantId);
        return ResponseEntity.ok(ApiResponse.success("Variant retrieved successfully", response));
    }

    @PutMapping("/{variantId}")
    @Operation(summary = "Update a variant by ID")
    public ResponseEntity<ApiResponse<ItemVariantResponse>> updateVariant(
            @PathVariable Long itemId,
            @PathVariable Long variantId,
            @Valid @RequestBody ItemVariantUpdateRequest request) {
        ItemVariantResponse response = itemVariantService.updateVariant(itemId, variantId, request);
        return ResponseEntity.ok(ApiResponse.success("Variant updated successfully", response));
    }

    @DeleteMapping("/{variantId}")
    @Operation(summary = "Delete a variant by ID")
    public ResponseEntity<ApiResponse<Void>> deleteVariant(
            @PathVariable Long itemId,
            @PathVariable Long variantId) {
        itemVariantService.deleteVariant(itemId, variantId);
        return ResponseEntity.ok(ApiResponse.success("Variant deleted successfully", null));
    }
}
