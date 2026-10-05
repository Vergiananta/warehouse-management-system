package com.example.warehouse.warehouse_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detail item/varian dan kuantitas dalam pesanan")
public class OrderItemRequest {

    @NotNull(message = "Item ID cannot be null")
    @Schema(description = "ID item yang dipesan", example = "1")
    private Long itemId;

    @Schema(description = "ID varian item (opsional, jika memesan varian tertentu)", example = "1")
    private Long variantId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Schema(description = "Jumlah barang yang dipesan", example = "2")
    private Integer quantity;
}

