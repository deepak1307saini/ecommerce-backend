package com.example.ecommerce.entity;

import com.example.ecommerce.util.DBConstants;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = DBConstants.TABLE_FAVORITES)
@Data
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBConstants.COLUMN_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = DBConstants.COLUMN_USER_ID)
    private User user;

    @Column(name = DBConstants.COLUMN_USER_ID, insertable = false, updatable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = DBConstants.COLUMN_PRODUCT_ID)
    private Product product;

    @Column(name = DBConstants.COLUMN_PRODUCT_ID, insertable = false, updatable = false)
    private Long productId;

    @CreationTimestamp
    @Column(name = DBConstants.COLUMN_CREATED_AT)
    private Instant createdAt = Instant.now();

    @UpdateTimestamp
    @Column(name = DBConstants.COLUMN_UPDATED_AT)
    private Instant updatedAt = Instant.now();
}