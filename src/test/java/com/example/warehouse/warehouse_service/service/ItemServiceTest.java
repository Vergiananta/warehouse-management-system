package com.example.warehouse.warehouse_service.service;

import com.example.warehouse.warehouse_service.dto.request.ItemCreateRequest;
import com.example.warehouse.warehouse_service.dto.request.ItemUpdateRequest;
import com.example.warehouse.warehouse_service.dto.response.ItemResponse;
import com.example.warehouse.warehouse_service.entity.Item;
import com.example.warehouse.warehouse_service.exception.DuplicateResourceException;
import com.example.warehouse.warehouse_service.exception.ResourceNotFoundException;
import com.example.warehouse.warehouse_service.mapper.ItemMapper;
import com.example.warehouse.warehouse_service.mapper.ItemVariantMapper;
import com.example.warehouse.warehouse_service.repository.ItemRepository;
import com.example.warehouse.warehouse_service.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Spy
    private ItemVariantMapper itemVariantMapper;

    @InjectMocks
    private ItemMapper itemMapper;

    private ItemServiceImpl itemService;

    private Item item;
    private ItemCreateRequest createRequest;
    private ItemUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemRepository, itemMapper);

        item = Item.builder()
                .id(1L)
                .sku("SKU-001")
                .name("Item 1")
                .description("Description 1")
                .price(new BigDecimal("100.00"))
                .stockQuantity(10)
                .build();

        createRequest = ItemCreateRequest.builder()
                .sku("SKU-001")
                .name("Item 1")
                .description("Description 1")
                .price(new BigDecimal("100.00"))
                .stockQuantity(10)
                .build();

        updateRequest = ItemUpdateRequest.builder()
                .sku("SKU-001-UPDATED")
                .name("Item 1 Updated")
                .description("Description Updated")
                .price(new BigDecimal("150.00"))
                .stockQuantity(20)
                .build();
    }

    @Test
    void createItem_Success() {
        when(itemRepository.existsBySku("SKU-001")).thenReturn(false);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemResponse response = itemService.createItem(createRequest);

        assertNotNull(response);
        assertEquals("SKU-001", response.getSku());
        assertEquals("Item 1", response.getName());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void createItem_DuplicateSku_ThrowsException() {
        when(itemRepository.existsBySku("SKU-001")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> itemService.createItem(createRequest));
    }

    @Test
    void getAllItems_Success() {
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<ItemResponse> response = itemService.getAllItems();

        assertEquals(1, response.size());
        assertEquals("SKU-001", response.get(0).getSku());
    }

    @Test
    void getItemById_Success() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ItemResponse response = itemService.getItemById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void getItemById_NotFound_ThrowsException() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> itemService.getItemById(1L));
    }

    @Test
    void updateItem_Success() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.existsBySkuAndIdNot("SKU-001-UPDATED", 1L)).thenReturn(false);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemResponse response = itemService.updateItem(1L, updateRequest);

        assertNotNull(response);
        assertEquals("SKU-001-UPDATED", response.getSku());
    }

    @Test
    void updateItem_DuplicateSku_ThrowsException() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.existsBySkuAndIdNot("SKU-001-UPDATED", 1L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> itemService.updateItem(1L, updateRequest));
    }

    @Test
    void deleteItem_Success() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        itemService.deleteItem(1L);

        verify(itemRepository).delete(item);
    }

    @Test
    void deleteItem_NotFound_ThrowsException() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> itemService.deleteItem(1L));
    }
}
