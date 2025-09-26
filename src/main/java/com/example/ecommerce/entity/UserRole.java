package com.example.ecommerce.entity;

import com.example.ecommerce.util.DBConstants;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = DBConstants.TABLE_USER_ROLES)
@Data
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBConstants.COLUMN_ID)
    private Long id;

    @Column(name = DBConstants.COLUMN_USER_ID)
    private Long userId;

    @Column(name = DBConstants.COLUMN_ROLE_ID)
    private Long roleId;
}