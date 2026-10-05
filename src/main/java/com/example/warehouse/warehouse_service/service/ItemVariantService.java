package com.example.warehouse.warehouse_service.service;

import com.example.warehouse.warehouse_service.dto.request.ItemVariantCreateRequest;
import com.example.warehouse.warehouse_service.dto.request.ItemVariantUpdateRequest;
import com.example.warehouse.warehouse_service.dto.response.ItemVariantResponse;

import java.util.List;

public interface ItemVariantService {
    ItemVariantResponse createVariant(Long itemId, ItemVariantCreateRequest request);
    List<ItemVariantResponse> getVariantsByItemId(Long itemId);
    ItemVariantResponse getVariantById(Long itemId, Long variantId);
    ItemVariantResponse updateVariant(Long itemId, Long variantId, ItemVariantUpdateRequest request);
    void deleteVariant(Long itemId, Long variantId);
}
