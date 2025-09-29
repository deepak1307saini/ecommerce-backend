package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FavoriteRequest {
    @NotBlank(message = "ProductId is required")
    private Long productId;
}