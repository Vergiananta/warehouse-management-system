package com.example.warehouse.warehouse_service.service.impl;

import com.example.warehouse.warehouse_service.dto.request.ItemVariantCreateRequest;
import com.example.warehouse.warehouse_service.dto.request.ItemVariantUpdateRequest;
import com.example.warehouse.warehouse_service.dto.response.ItemVariantResponse;
import com.example.warehouse.warehouse_service.entity.Item;
import com.example.warehouse.warehouse_service.entity.ItemVariant;
import com.example.warehouse.warehouse_service.exception.DuplicateResourceException;
import com.example.warehouse.warehouse_service.exception.ResourceNotFoundException;
import com.example.warehouse.warehouse_service.mapper.ItemVariantMapper;
import com.example.warehouse.warehouse_service.repository.ItemRepository;
import com.example.warehouse.warehouse_service.repository.ItemVariantRepository;
import com.example.warehouse.warehouse_service.service.ItemVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemVariantServiceImpl implements ItemVariantService {

    private final ItemRepository itemRepository;
    private final ItemVariantRepository itemVariantRepository;
    private final ItemVariantMapper itemVariantMapper;

    @Override
    @Transactional
    public ItemVariantResponse createVariant(Long itemId, ItemVariantCreateRequest request) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item with id " + itemId + " not found"));

        if (itemVariantRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException("Variant with SKU '" + request.getSku() + "' already exists");
        }

        ItemVariant variant = itemVariantMapper.toEntity(request, item);
        ItemVariant savedVariant = itemVariantRepository.save(variant);
        return itemVariantMapper.toResponse(savedVariant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemVariantResponse> getVariantsByItemId(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ResourceNotFoundException("Item with id " + itemId + " not found");
        }

        return itemVariantRepository.findByItemId(itemId).stream()
                .map(itemVariantMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ItemVariantResponse getVariantById(Long itemId, Long variantId) {
        ItemVariant variant = itemVariantRepository.findByIdAndItemId(variantId, itemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Variant with id " + variantId + " not found for item " + itemId));
        return itemVariantMapper.toResponse(variant);
    }

    @Override
    @Transactional
    public ItemVariantResponse updateVariant(Long itemId, Long variantId, ItemVariantUpdateRequest request) {
        ItemVariant variant = itemVariantRepository.findByIdAndItemId(variantId, itemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Variant with id " + variantId + " not found for item " + itemId));

        if (itemVariantRepository.existsBySkuAndIdNot(request.getSku(), variantId)) {
            throw new DuplicateResourceException("Variant with SKU '" + request.getSku() + "' already exists");
        }

        itemVariantMapper.updateEntity(variant, request);
        ItemVariant updatedVariant = itemVariantRepository.save(variant);
        return itemVariantMapper.toResponse(updatedVariant);
    }

    @Override
    @Transactional
    public void deleteVariant(Long itemId, Long variantId) {
        ItemVariant variant = itemVariantRepository.findByIdAndItemId(variantId, itemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Variant with id " + variantId + " not found for item " + itemId));
        itemVariantRepository.delete(variant);
    }
}
