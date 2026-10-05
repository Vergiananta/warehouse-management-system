package com.example.warehouse.warehouse_service.mapper;

import com.example.warehouse.warehouse_service.dto.request.ItemCreateRequest;
import com.example.warehouse.warehouse_service.dto.request.ItemUpdateRequest;
import com.example.warehouse.warehouse_service.dto.response.ItemResponse;
import com.example.warehouse.warehouse_service.dto.response.ItemVariantResponse;
import com.example.warehouse.warehouse_service.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    private final ItemVariantMapper itemVariantMapper;

    public Item toEntity(ItemCreateRequest request) {
        return Item.builder()
                .sku(request.getSku())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .build();
    }

    public void updateEntity(Item item, ItemUpdateRequest request) {
        item.setSku(request.getSku());
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setStockQuantity(request.getStockQuantity());
    }

    public ItemResponse toResponse(Item item) {
        List<ItemVariantResponse> variantResponses = item.getVariants() != null
                ? item.getVariants().stream().map(itemVariantMapper::toResponse).toList()
                : Collections.emptyList();

        return ItemResponse.builder()
                .id(item.getId())
                .sku(item.getSku())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .stockQuantity(item.getStockQuantity())
                .variants(variantResponses)
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}
