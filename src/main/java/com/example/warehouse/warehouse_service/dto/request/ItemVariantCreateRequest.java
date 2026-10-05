package com.example.warehouse.warehouse_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body untuk membuat varian item baru")
public class ItemVariantCreateRequest {

    @NotBlank(message = "SKU cannot be blank")
    @Schema(description = "Kode unik SKU varian item", example = "ITEM-001-RED")
    private String sku;

    @NotBlank(message = "Variant name cannot be blank")
    @Schema(description = "Nama varian item", example = "Red Linear Switch")
    private String name;

    @NotNull(message = "Price cannot be null")
    @PositiveOrZero(message = "Price must be greater than or equal to 0")
    @Schema(description = "Harga varian item", example = "365000.00")
    private BigDecimal price;

    @NotNull(message = "Stock quantity cannot be null")
    @PositiveOrZero(message = "Stock quantity must be greater than or equal to 0")
    @Schema(description = "Jumlah stok varian item", example = "20")
    private Integer stockQuantity;
}

