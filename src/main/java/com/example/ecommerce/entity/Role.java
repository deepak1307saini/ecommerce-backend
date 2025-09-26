package com.example.ecommerce.entity;

import com.example.ecommerce.util.DBConstants;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = DBConstants.TABLE_ROLES)
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBConstants.COLUMN_ID)
    private Long id;

    @Column(name = DBConstants.COLUMN_NAME, unique = true)
    private String name;
}