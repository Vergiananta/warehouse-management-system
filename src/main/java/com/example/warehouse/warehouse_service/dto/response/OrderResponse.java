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
@Schema(description = "Data respons pesanan lengkap")
public class OrderResponse {
    @Schema(description = "ID unik pesanan", example = "1")
    private Long id;

    @Schema(description = "Nomor unik pesanan", example = "ORD-20261005-0001")
    private String orderNumber;

    @Schema(description = "Total nilai pesanan", example = "730000.00")
    private BigDecimal totalAmount;

    @Schema(description = "Status pesanan", example = "COMPLETED")
    private String status;

    @Schema(description = "Daftar rincian item pesanan")
    private List<OrderItemResponse> items;

    @Schema(description = "Waktu pesanan dibuat", example = "2026-10-05T22:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Waktu status pesanan terakhir diperbarui", example = "2026-10-05T22:00:00")
    private LocalDateTime updatedAt;
}

