package com.example.warehouse.warehouse_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Format standar respons API")
public class ApiResponse<T> {
    @Schema(description = "Status keberhasilan operasi", example = "true")
    private boolean success;

    @Schema(description = "Pesan keterangan hasil operasi", example = "Operation successful")
    private String message;

    @Schema(description = "Data payload respons")
    private T data;

    @Builder.Default
    @Schema(description = "Waktu respons dihasilkan", example = "2026-10-05T22:30:00")
    private LocalDateTime timestamp = LocalDateTime.now();


    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return success("Operation successful", data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
