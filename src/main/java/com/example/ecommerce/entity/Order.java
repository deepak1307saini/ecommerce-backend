package com.example.ecommerce.entity;

import com.example.ecommerce.util.DBConstants;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = DBConstants.TABLE_ORDERS)
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBConstants.COLUMN_ID)
    private Long id;

    @Column(name = DBConstants.COLUMN_USER_ID)
    private Long userId;

    @Column(name = DBConstants.COLUMN_TOTAL_QUANTITY)
    private int totalQuantity;

    @Column(name = DBConstants.COLUMN_TOTAL_AMOUNT)
    private double totalAmount;

    @Column(name = DBConstants.COLUMN_CREATED_AT)
    private Instant createdAt = Instant.now();

    @Column(name = DBConstants.COLUMN_UPDATED_AT)
    private Instant updatedAt = Instant.now();
}