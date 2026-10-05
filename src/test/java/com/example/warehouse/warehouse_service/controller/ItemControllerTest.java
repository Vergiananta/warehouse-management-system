package com.example.warehouse.warehouse_service.controller;

import com.example.warehouse.warehouse_service.dto.request.ItemCreateRequest;
import com.example.warehouse.warehouse_service.dto.request.ItemUpdateRequest;
import com.example.warehouse.warehouse_service.dto.response.ApiResponse;
import com.example.warehouse.warehouse_service.dto.response.ItemResponse;
import com.example.warehouse.warehouse_service.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private ItemResponse itemResponse;

    @BeforeEach
    void setUp() {
        itemResponse = ItemResponse.builder()
                .id(1L)
                .sku("SKU-001")
                .name("Item 1")
                .price(new BigDecimal("100.00"))
                .stockQuantity(10)
                .build();
    }

    @Test
    void createItem_ReturnsCreatedResponse() {
        ItemCreateRequest request = ItemCreateRequest.builder()
                .sku("SKU-001")
                .name("Item 1")
                .price(new BigDecimal("100.00"))
                .stockQuantity(10)
                .build();

        when(itemService.createItem(request)).thenReturn(itemResponse);

        ResponseEntity<ApiResponse<ItemResponse>> response = itemController.createItem(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("SKU-001", response.getBody().getData().getSku());
    }

    @Test
    void getAllItems_ReturnsOkResponse() {
        when(itemService.getAllItems()).thenReturn(List.of(itemResponse));

        ResponseEntity<ApiResponse<List<ItemResponse>>> response = itemController.getAllItems();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void getItemById_ReturnsOkResponse() {
        when(itemService.getItemById(1L)).thenReturn(itemResponse);

        ResponseEntity<ApiResponse<ItemResponse>> response = itemController.getItemById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getData().getId());
    }

    @Test
    void updateItem_ReturnsOkResponse() {
        ItemUpdateRequest request = ItemUpdateRequest.builder()
                .sku("SKU-001")
                .name("Item 1 Updated")
                .price(new BigDecimal("120.00"))
                .stockQuantity(15)
                .build();

        when(itemService.updateItem(1L, request)).thenReturn(itemResponse);

        ResponseEntity<ApiResponse<ItemResponse>> response = itemController.updateItem(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("SKU-001", response.getBody().getData().getSku());
    }

    @Test
    void deleteItem_ReturnsOkResponse() {
        doNothing().when(itemService).deleteItem(1L);

        ResponseEntity<ApiResponse<Void>> response = itemController.deleteItem(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        verify(itemService).deleteItem(1L);
    }
}
