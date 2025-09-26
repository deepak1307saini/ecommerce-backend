package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class FavoriteDTO {
    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
}