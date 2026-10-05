package com.example.warehouse.warehouse_service.service;

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
import com.example.warehouse.warehouse_service.service.impl.ItemVariantServiceImpl;
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
class ItemVariantServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemVariantRepository itemVariantRepository;

    @Spy
    private ItemVariantMapper itemVariantMapper;

    @InjectMocks
    private ItemVariantServiceImpl itemVariantService;

    private Item item;
    private ItemVariant variant;
    private ItemVariantCreateRequest createRequest;
    private ItemVariantUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        item = Item.builder()
                .id(1L)
                .sku("SKU-001")
                .name("Item 1")
                .price(new BigDecimal("100.00"))
                .stockQuantity(10)
                .build();

        variant = ItemVariant.builder()
                .id(10L)
                .item(item)
                .sku("VAR-001")
                .name("Red - XL")
                .price(new BigDecimal("120.00"))
                .stockQuantity(5)
                .build();

        createRequest = ItemVariantCreateRequest.builder()
                .sku("VAR-001")
                .name("Red - XL")
                .price(new BigDecimal("120.00"))
                .stockQuantity(5)
                .build();

        updateRequest = ItemVariantUpdateRequest.builder()
                .sku("VAR-001-UPDATED")
                .name("Red - XXL")
                .price(new BigDecimal("130.00"))
                .stockQuantity(8)
                .build();
    }

    @Test
    void createVariant_Success() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemVariantRepository.existsBySku("VAR-001")).thenReturn(false);
        when(itemVariantRepository.save(any(ItemVariant.class))).thenReturn(variant);

        ItemVariantResponse response = itemVariantService.createVariant(1L, createRequest);

        assertNotNull(response);
        assertEquals("VAR-001", response.getSku());
        assertEquals("Red - XL", response.getName());
        verify(itemVariantRepository).save(any(ItemVariant.class));
    }

    @Test
    void createVariant_ItemNotFound_ThrowsException() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> itemVariantService.createVariant(1L, createRequest));
    }

    @Test
    void createVariant_DuplicateSku_ThrowsException() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemVariantRepository.existsBySku("VAR-001")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> itemVariantService.createVariant(1L, createRequest));
    }

    @Test
    void getVariantsByItemId_Success() {
        when(itemRepository.existsById(1L)).thenReturn(true);
        when(itemVariantRepository.findByItemId(1L)).thenReturn(List.of(variant));

        List<ItemVariantResponse> responses = itemVariantService.getVariantsByItemId(1L);

        assertEquals(1, responses.size());
        assertEquals("VAR-001", responses.get(0).getSku());
    }

    @Test
    void getVariantsByItemId_ItemNotFound_ThrowsException() {
        when(itemRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> itemVariantService.getVariantsByItemId(1L));
    }

    @Test
    void getVariantById_Success() {
        when(itemVariantRepository.findByIdAndItemId(10L, 1L)).thenReturn(Optional.of(variant));

        ItemVariantResponse response = itemVariantService.getVariantById(1L, 10L);

        assertNotNull(response);
        assertEquals(10L, response.getId());
    }

    @Test
    void getVariantById_NotFound_ThrowsException() {
        when(itemVariantRepository.findByIdAndItemId(10L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> itemVariantService.getVariantById(1L, 10L));
    }

    @Test
    void updateVariant_Success() {
        when(itemVariantRepository.findByIdAndItemId(10L, 1L)).thenReturn(Optional.of(variant));
        when(itemVariantRepository.existsBySkuAndIdNot("VAR-001-UPDATED", 10L)).thenReturn(false);
        when(itemVariantRepository.save(any(ItemVariant.class))).thenReturn(variant);

        ItemVariantResponse response = itemVariantService.updateVariant(1L, 10L, updateRequest);

        assertNotNull(response);
        assertEquals("VAR-001-UPDATED", response.getSku());
    }

    @Test
    void updateVariant_DuplicateSku_ThrowsException() {
        when(itemVariantRepository.findByIdAndItemId(10L, 1L)).thenReturn(Optional.of(variant));
        when(itemVariantRepository.existsBySkuAndIdNot("VAR-001-UPDATED", 10L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> itemVariantService.updateVariant(1L, 10L, updateRequest));
    }

    @Test
    void deleteVariant_Success() {
        when(itemVariantRepository.findByIdAndItemId(10L, 1L)).thenReturn(Optional.of(variant));

        itemVariantService.deleteVariant(1L, 10L);

        verify(itemVariantRepository).delete(variant);
    }

    @Test
    void deleteVariant_NotFound_ThrowsException() {
        when(itemVariantRepository.findByIdAndItemId(10L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> itemVariantService.deleteVariant(1L, 10L));
    }
}
