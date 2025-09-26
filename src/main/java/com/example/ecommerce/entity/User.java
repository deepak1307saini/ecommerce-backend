package com.example.ecommerce.entity;

import com.example.ecommerce.util.DBConstants;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = DBConstants.TABLE_USERS)
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBConstants.COLUMN_ID)
    private Long id;

    @Column(name = DBConstants.COLUMN_USERNAME, unique = true)
    private String username;

    @Column(name = DBConstants.COLUMN_EMAIL, unique = true)
    private String email;

    @Column(name = DBConstants.COLUMN_FIRST_NAME)
    private String firstName;

    @Column(name = DBConstants.COLUMN_LAST_NAME)
    private String lastName;

    @Column(name = DBConstants.COLUMN_KEYCLOAK_USER_ID, unique = true)
    private String keycloakUserId;

    @Column(name = DBConstants.COLUMN_TENANT_ID)
    private Long tenantId;

    @Column(name = DBConstants.COLUMN_MOBILE_NUMBER)
    private String mobileNumber;

    @Column(name = DBConstants.COLUMN_CREATED_AT)
    private Instant createdAt = Instant.now();

    @Column(name = DBConstants.COLUMN_UPDATED_AT)
    private Instant updatedAt = Instant.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = DBConstants.TABLE_USER_ROLES,
            joinColumns = @JoinColumn(name = DBConstants.COLUMN_USER_ID),
            inverseJoinColumns = @JoinColumn(name = DBConstants.COLUMN_ROLE_ID)
    )
    private Set<Role> roles = new HashSet<>();
}