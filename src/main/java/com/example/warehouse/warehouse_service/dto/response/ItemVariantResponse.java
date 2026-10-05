package com.example.warehouse.warehouse_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data respons detail varian item")
public class ItemVariantResponse {
    @Schema(description = "ID unik varian item", example = "1")
    private Long id;

    @Schema(description = "ID item induk", example = "1")
    private Long itemId;

    @Schema(description = "Kode unik SKU varian item", example = "ITEM-001-RED")
    private String sku;

    @Schema(description = "Nama varian item", example = "Red Linear Switch")
    private String name;

    @Schema(description = "Harga varian item", example = "365000.00")
    private BigDecimal price;

    @Schema(description = "Jumlah stok varian item", example = "20")
    private Integer stockQuantity;

    @Schema(description = "Waktu pembuatan varian item", example = "2026-10-05T20:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Waktu pembaruan varian item terakhir", example = "2026-10-05T21:15:00")
    private LocalDateTime updatedAt;
}

