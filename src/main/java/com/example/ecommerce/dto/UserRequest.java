package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String email;
    private String keycloakUserId;
    private String tenantName;
}