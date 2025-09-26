package com.example.ecommerce.entity;

import com.example.ecommerce.util.DBConstants;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = DBConstants.TABLE_FAVORITES)
@Data
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBConstants.COLUMN_ID)
    private Long id;

    @Column(name = DBConstants.COLUMN_USER_ID)
    private Long userId;

    @Column(name = DBConstants.COLUMN_PRODUCT_ID)
    private Long productId;

    @Column(name = DBConstants.COLUMN_CREATED_AT)
    private Instant createdAt = Instant.now();

    @Column(name = DBConstants.COLUMN_UPDATED_AT)
    private Instant updatedAt = Instant.now();
}