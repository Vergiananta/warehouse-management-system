package com.example.warehouse.warehouse_service.controller;

import com.example.warehouse.warehouse_service.dto.request.ItemVariantCreateRequest;
import com.example.warehouse.warehouse_service.dto.request.ItemVariantUpdateRequest;
import com.example.warehouse.warehouse_service.dto.response.ApiResponse;
import com.example.warehouse.warehouse_service.dto.response.ItemVariantResponse;
import com.example.warehouse.warehouse_service.service.ItemVariantService;
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
@RequestMapping("/api/items/{itemId}/variants")
@RequiredArgsConstructor
@Tag(name = "Item Variants", description = "Item variant management APIs")
public class ItemVariantController {

    private final ItemVariantService itemVariantService;

    @PostMapping
    @Operation(summary = "Create a new variant for an item", description = "Menambahkan varian baru untuk item yang sudah ada")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Varian item berhasil dibuat",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "CreateVariantSuccessResponse",
                                    value = "{\n  \"success\": true,\n  \"message\": \"Variant created successfully\",\n  \"data\": {\n    \"id\": 1,\n    \"itemId\": 1,\n    \"sku\": \"ITEM-001-RED\",\n    \"name\": \"Red Linear Switch\",\n    \"price\": 365000.00,\n    \"stockQuantity\": 20,\n    \"createdAt\": \"2026-10-05T20:30:00\",\n    \"updatedAt\": \"2026-10-05T20:30:00\"\n  },\n  \"timestamp\": \"2026-10-05T20:30:00\"\n}"
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
                                    value = "{\n  \"success\": false,\n  \"message\": \"Validation failed: sku: SKU cannot be blank\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T20:30:00\"\n}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Item induk tidak ditemukan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ItemNotFoundResponse",
                                    value = "{\n  \"success\": false,\n  \"message\": \"Item not found with id: 99\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T20:30:00\"\n}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "SKU varian sudah digunakan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "DuplicateSkuResponse",
                                    value = "{\n  \"success\": false,\n  \"message\": \"Item variant with SKU 'ITEM-001-RED' already exists\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T20:30:00\"\n}"
                            )
                    )
            )
    })
    public ResponseEntity<ApiResponse<ItemVariantResponse>> createVariant(
            @PathVariable Long itemId,
            @Valid @org.springframework.web.bind.annotation.RequestBody
            @RequestBody(
                    description = "Data varian item baru",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemVariantCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "CreateVariantExample",
                                    value = "{\n  \"sku\": \"ITEM-001-RED\",\n  \"name\": \"Red Linear Switch\",\n  \"price\": 365000.00,\n  \"stockQuantity\": 20\n}"
                            )
                    )
            ) ItemVariantCreateRequest request) {
        ItemVariantResponse response = itemVariantService.createVariant(itemId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Variant created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all variants of an item", description = "Mengambil seluruh varian milik item tertentu")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Daftar varian berhasil diambil",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "GetVariantsResponse",
                                    value = "{\n  \"success\": true,\n  \"message\": \"Variants retrieved successfully\",\n  \"data\": [\n    {\n      \"id\": 1,\n      \"itemId\": 1,\n      \"sku\": \"ITEM-001-RED\",\n      \"name\": \"Red Linear Switch\",\n      \"price\": 365000.00,\n      \"stockQuantity\": 20,\n      \"createdAt\": \"2026-10-05T20:30:00\",\n      \"updatedAt\": \"2026-10-05T20:30:00\"\n    },\n    {\n      \"id\": 2,\n      \"itemId\": 1,\n      \"sku\": \"ITEM-001-BLUE\",\n      \"name\": \"Blue Clicky Switch\",\n      \"price\": 365000.00,\n      \"stockQuantity\": 15,\n      \"createdAt\": \"2026-10-05T20:35:00\",\n      \"updatedAt\": \"2026-10-05T20:35:00\"\n    }\n  ],\n  \"timestamp\": \"2026-10-05T20:35:00\"\n}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Item tidak ditemukan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ItemNotFoundResponse",
                                    value = "{\n  \"success\": false,\n  \"message\": \"Item not found with id: 99\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T20:35:00\"\n}"
                            )
                    )
            )
    })
    public ResponseEntity<ApiResponse<List<ItemVariantResponse>>> getVariantsByItemId(@PathVariable Long itemId) {
        List<ItemVariantResponse> response = itemVariantService.getVariantsByItemId(itemId);
        return ResponseEntity.ok(ApiResponse.success("Variants retrieved successfully", response));
    }

    @GetMapping("/{variantId}")
    @Operation(summary = "Get variant by ID", description = "Mengambil detail varian berdasarkan ID item dan ID varian")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Detail varian ditemukan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "GetVariantByIdResponse",
                                    value = "{\n  \"success\": true,\n  \"message\": \"Variant retrieved successfully\",\n  \"data\": {\n    \"id\": 1,\n    \"itemId\": 1,\n    \"sku\": \"ITEM-001-RED\",\n    \"name\": \"Red Linear Switch\",\n    \"price\": 365000.00,\n    \"stockQuantity\": 20,\n    \"createdAt\": \"2026-10-05T20:30:00\",\n    \"updatedAt\": \"2026-10-05T20:30:00\"\n  },\n  \"timestamp\": \"2026-10-05T20:30:00\"\n}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Item atau varian tidak ditemukan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "VariantNotFoundResponse",
                                    value = "{\n  \"success\": false,\n  \"message\": \"Item variant not found with id: 99\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T20:30:00\"\n}"
                            )
                    )
            )
    })
    public ResponseEntity<ApiResponse<ItemVariantResponse>> getVariantById(
            @PathVariable Long itemId,
            @PathVariable Long variantId) {
        ItemVariantResponse response = itemVariantService.getVariantById(itemId, variantId);
        return ResponseEntity.ok(ApiResponse.success("Variant retrieved successfully", response));
    }

    @PutMapping("/{variantId}")
    @Operation(summary = "Update a variant by ID", description = "Memperbarui data varian item")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Varian item berhasil diperbarui",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "UpdateVariantResponse",
                                    value = "{\n  \"success\": true,\n  \"message\": \"Variant updated successfully\",\n  \"data\": {\n    \"id\": 1,\n    \"itemId\": 1,\n    \"sku\": \"ITEM-001-RED\",\n    \"name\": \"Red Linear Switch (Lubed)\",\n    \"price\": 375000.00,\n    \"stockQuantity\": 25,\n    \"createdAt\": \"2026-10-05T20:30:00\",\n    \"updatedAt\": \"2026-10-05T21:15:00\"\n  },\n  \"timestamp\": \"2026-10-05T21:15:00\"\n}"
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
                                    value = "{\n  \"success\": false,\n  \"message\": \"Validation failed: price: Price must be greater than or equal to 0\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T21:15:00\"\n}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Item atau varian tidak ditemukan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "NotFoundResponse",
                                    value = "{\n  \"success\": false,\n  \"message\": \"Item variant not found with id: 99\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T21:15:00\"\n}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "SKU varian sudah digunakan oleh varian lain",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "DuplicateSkuResponse",
                                    value = "{\n  \"success\": false,\n  \"message\": \"Item variant with SKU 'ITEM-001-BLUE' already exists\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T21:15:00\"\n}"
                            )
                    )
            )
    })
    public ResponseEntity<ApiResponse<ItemVariantResponse>> updateVariant(
            @PathVariable Long itemId,
            @PathVariable Long variantId,
            @Valid @org.springframework.web.bind.annotation.RequestBody
            @RequestBody(
                    description = "Data pembaruan varian item",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemVariantUpdateRequest.class),
                            examples = @ExampleObject(
                                    name = "UpdateVariantExample",
                                    value = "{\n  \"sku\": \"ITEM-001-RED\",\n  \"name\": \"Red Linear Switch (Lubed)\",\n  \"price\": 375000.00,\n  \"stockQuantity\": 25\n}"
                            )
                    )
            ) ItemVariantUpdateRequest request) {
        ItemVariantResponse response = itemVariantService.updateVariant(itemId, variantId, request);
        return ResponseEntity.ok(ApiResponse.success("Variant updated successfully", response));
    }

    @DeleteMapping("/{variantId}")
    @Operation(summary = "Delete a variant by ID", description = "Menghapus varian dari item tertentu")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Varian item berhasil dihapus",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "DeleteVariantResponse",
                                    value = "{\n  \"success\": true,\n  \"message\": \"Variant deleted successfully\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T21:15:00\"\n}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Item atau varian tidak ditemukan",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "NotFoundResponse",
                                    value = "{\n  \"success\": false,\n  \"message\": \"Item variant not found with id: 99\",\n  \"data\": null,\n  \"timestamp\": \"2026-10-05T21:15:00\"\n}"
                            )
                    )
            )
    })
    public ResponseEntity<ApiResponse<Void>> deleteVariant(
            @PathVariable Long itemId,
            @PathVariable Long variantId) {
        itemVariantService.deleteVariant(itemId, variantId);
        return ResponseEntity.ok(ApiResponse.success("Variant deleted successfully", null));
    }
}
