package com.example.warehouse.warehouse_service.service;

import com.example.warehouse.warehouse_service.dto.request.ItemCreateRequest;
import com.example.warehouse.warehouse_service.dto.request.ItemUpdateRequest;
import com.example.warehouse.warehouse_service.dto.response.ItemResponse;

import java.util.List;

public interface ItemService {
    ItemResponse createItem(ItemCreateRequest request);
    List<ItemResponse> getAllItems();
    ItemResponse getItemById(Long id);
    ItemResponse updateItem(Long id, ItemUpdateRequest request);
    void deleteItem(Long id);
}
