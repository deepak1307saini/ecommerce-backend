package com.example.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    @NotBlank(message = "Product name is required")
    private String name;
    @NotBlank(message = "Product category is required")
    private String category;
    @Positive(message = "Price must be positive")
    private double price;
    @Min(value = 0, message = "Available quantity cannot be negative")
    private int availableQuantity;
    private String thumbnail;
    private String description;
}