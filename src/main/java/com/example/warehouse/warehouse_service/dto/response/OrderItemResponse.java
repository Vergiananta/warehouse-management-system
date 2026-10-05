package com.example.warehouse.warehouse_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private Long id;
    private Long itemId;
    private String itemSku;
    private String itemName;
    private Long variantId;
    private String variantSku;
    private String variantName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}
