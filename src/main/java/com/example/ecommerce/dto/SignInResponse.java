package com.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class SignInResponse {
    private String token;
    private UserDTO user;
    private List<String> roles;
    private TenantDTO tenant;
}