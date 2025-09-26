package com.example.ecommerce.entity;

import com.example.ecommerce.util.DBConstants;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = DBConstants.TABLE_PRODUCTS)
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBConstants.COLUMN_ID)
    private Long id;

    @Column(name = DBConstants.COLUMN_NAME)
    private String name;

    @Column(name = DBConstants.COLUMN_CATEGORY)
    private String category;

    @Column(name = DBConstants.COLUMN_PRICE)
    private double price;

    @Column(name = DBConstants.COLUMN_AVAILABLE_QUANTITY)
    private int availableQuantity;

    @Column(name = DBConstants.COLUMN_TENANT_ID)
    private Long tenantId;

    @Column(name = DBConstants.COLUMN_IMAGE)
    private String thumbnail;

    @Column(name = DBConstants.COLUMN_DESCRIPTION, columnDefinition = "TEXT")
    private String description;

    @Column(name = DBConstants.COLUMN_CREATED_AT)
    private Instant createdAt = Instant.now();

    @Column(name = DBConstants.COLUMN_UPDATED_AT)
    private Instant updatedAt = Instant.now();
}