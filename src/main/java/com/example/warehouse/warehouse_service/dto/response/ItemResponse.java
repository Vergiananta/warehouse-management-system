package com.example.warehouse.warehouse_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data respons detail item")
public class ItemResponse {
    @Schema(description = "ID unik item", example = "1")
    private Long id;

    @Schema(description = "Kode unik SKU item", example = "ITEM-001")
    private String sku;

    @Schema(description = "Nama item", example = "Wireless Mechanical Keyboard")
    private String name;

    @Schema(description = "Deskripsi item", example = "Keyboard mechanical wireless 75% dengan RGB")
    private String description;

    @Schema(description = "Harga satuan item", example = "350000.00")
    private BigDecimal price;

    @Schema(description = "Jumlah stok item", example = "50")
    private Integer stockQuantity;

    @Schema(description = "Daftar varian item")
    private List<ItemVariantResponse> variants;

    @Schema(description = "Waktu pembuatan item", example = "2026-10-05T20:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Waktu pembaruan item terakhir", example = "2026-10-05T21:00:00")
    private LocalDateTime updatedAt;
}

