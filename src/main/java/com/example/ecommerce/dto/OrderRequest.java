package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    @NotEmpty(message = "Items cannot be empty")
    private List<OrderItemDTO> items;
}