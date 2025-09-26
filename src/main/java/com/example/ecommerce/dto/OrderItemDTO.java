package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;
    private Long productId;
    private int quantity;
    private double price;
    private ProductDTO product;
}