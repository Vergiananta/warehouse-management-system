package com.example.warehouse.warehouse_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data respons item yang ada di dalam pesanan")
public class OrderItemResponse {
    @Schema(description = "ID unik order item", example = "1")
    private Long id;

    @Schema(description = "ID item", example = "1")
    private Long itemId;

    @Schema(description = "SKU item", example = "ITEM-001")
    private String itemSku;

    @Schema(description = "Nama item", example = "Wireless Mechanical Keyboard")
    private String itemName;

    @Schema(description = "ID varian item (jika ada)", example = "1")
    private Long variantId;

    @Schema(description = "SKU varian item (jika ada)", example = "ITEM-001-RED")
    private String variantSku;

    @Schema(description = "Nama varian item (jika ada)", example = "Red Linear Switch")
    private String variantName;

    @Schema(description = "Jumlah barang yang dibeli", example = "2")
    private Integer quantity;

    @Schema(description = "Harga satuan saat transaksi", example = "365000.00")
    private BigDecimal unitPrice;

    @Schema(description = "Subtotal harga (quantity * unitPrice)", example = "730000.00")
    private BigDecimal subtotal;
}

