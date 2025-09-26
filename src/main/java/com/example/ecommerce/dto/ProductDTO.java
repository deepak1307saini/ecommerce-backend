package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String category;
    private double price;
    private int availableQuantity;
    private String thumbnail;
    private String description;
}