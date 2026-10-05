package com.example.warehouse.warehouse_service.mapper;

import com.example.warehouse.warehouse_service.dto.request.ItemVariantCreateRequest;
import com.example.warehouse.warehouse_service.dto.request.ItemVariantUpdateRequest;
import com.example.warehouse.warehouse_service.dto.response.ItemVariantResponse;
import com.example.warehouse.warehouse_service.entity.Item;
import com.example.warehouse.warehouse_service.entity.ItemVariant;
import org.springframework.stereotype.Component;

@Component
public class ItemVariantMapper {

    public ItemVariant toEntity(ItemVariantCreateRequest request, Item item) {
        return ItemVariant.builder()
                .item(item)
                .sku(request.getSku())
                .name(request.getName())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .build();
    }

    public void updateEntity(ItemVariant variant, ItemVariantUpdateRequest request) {
        variant.setSku(request.getSku());
        variant.setName(request.getName());
        variant.setPrice(request.getPrice());
        variant.setStockQuantity(request.getStockQuantity());
    }

    public ItemVariantResponse toResponse(ItemVariant variant) {
        return ItemVariantResponse.builder()
                .id(variant.getId())
                .itemId(variant.getItem() != null ? variant.getItem().getId() : null)
                .sku(variant.getSku())
                .name(variant.getName())
                .price(variant.getPrice())
                .stockQuantity(variant.getStockQuantity())
                .createdAt(variant.getCreatedAt())
                .updatedAt(variant.getUpdatedAt())
                .build();
    }
}
