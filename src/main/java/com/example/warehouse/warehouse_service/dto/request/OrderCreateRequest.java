package com.example.warehouse.warehouse_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body untuk membuat pesanan baru")
public class OrderCreateRequest {

    @NotEmpty(message = "Order items list cannot be empty")
    @Valid
    @Schema(description = "Daftar item yang dipesan")
    private List<OrderItemRequest> items;
}

