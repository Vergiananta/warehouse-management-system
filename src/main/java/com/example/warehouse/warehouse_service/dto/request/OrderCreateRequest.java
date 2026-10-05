package com.example.warehouse.warehouse_service.dto.request;

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
public class OrderCreateRequest {

    @NotEmpty(message = "Order items list cannot be empty")
    @Valid
    private List<OrderItemRequest> items;
}
