package com.example.ecommerce.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private int totalQuantity;
    private double totalAmount;
    private List<OrderItemDTO> items;
    private Instant createdAt;
    private Instant modifiedAt;
}