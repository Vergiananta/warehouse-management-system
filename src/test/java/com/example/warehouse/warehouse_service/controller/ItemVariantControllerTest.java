package com.example.warehouse.warehouse_service.controller;

import com.example.warehouse.warehouse_service.dto.request.ItemVariantCreateRequest;
import com.example.warehouse.warehouse_service.dto.request.ItemVariantUpdateRequest;
import com.example.warehouse.warehouse_service.dto.response.ApiResponse;
import com.example.warehouse.warehouse_service.dto.response.ItemVariantResponse;
import com.example.warehouse.warehouse_service.service.ItemVariantService;
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
class ItemVariantControllerTest {

    @Mock
    private ItemVariantService itemVariantService;

    @InjectMocks
    private ItemVariantController itemVariantController;

    private ItemVariantResponse variantResponse;

    @BeforeEach
    void setUp() {
        variantResponse = ItemVariantResponse.builder()
                .id(10L)
                .itemId(1L)
                .sku("VAR-001")
                .name("Red - XL")
                .price(new BigDecimal("120.00"))
                .stockQuantity(5)
                .build();
    }

    @Test
    void createVariant_ReturnsCreatedResponse() {
        ItemVariantCreateRequest request = ItemVariantCreateRequest.builder()
                .sku("VAR-001")
                .name("Red - XL")
                .price(new BigDecimal("120.00"))
                .stockQuantity(5)
                .build();

        when(itemVariantService.createVariant(1L, request)).thenReturn(variantResponse);

        ResponseEntity<ApiResponse<ItemVariantResponse>> response = itemVariantController.createVariant(1L, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("VAR-001", response.getBody().getData().getSku());
    }

    @Test
    void getVariantsByItemId_ReturnsOkResponse() {
        when(itemVariantService.getVariantsByItemId(1L)).thenReturn(List.of(variantResponse));

        ResponseEntity<ApiResponse<List<ItemVariantResponse>>> response = itemVariantController.getVariantsByItemId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void getVariantById_ReturnsOkResponse() {
        when(itemVariantService.getVariantById(1L, 10L)).thenReturn(variantResponse);

        ResponseEntity<ApiResponse<ItemVariantResponse>> response = itemVariantController.getVariantById(1L, 10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10L, response.getBody().getData().getId());
    }

    @Test
    void updateVariant_ReturnsOkResponse() {
        ItemVariantUpdateRequest request = ItemVariantUpdateRequest.builder()
                .sku("VAR-001")
                .name("Red - XXL")
                .price(new BigDecimal("130.00"))
                .stockQuantity(8)
                .build();

        when(itemVariantService.updateVariant(1L, 10L, request)).thenReturn(variantResponse);

        ResponseEntity<ApiResponse<ItemVariantResponse>> response = itemVariantController.updateVariant(1L, 10L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("VAR-001", response.getBody().getData().getSku());
    }

    @Test
    void deleteVariant_ReturnsOkResponse() {
        doNothing().when(itemVariantService).deleteVariant(1L, 10L);

        ResponseEntity<ApiResponse<Void>> response = itemVariantController.deleteVariant(1L, 10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        verify(itemVariantService).deleteVariant(1L, 10L);
    }
}
