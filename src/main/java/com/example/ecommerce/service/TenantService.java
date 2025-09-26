package com.example.ecommerce.service;

import com.example.ecommerce.dto.TenantDTO;
import com.example.ecommerce.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TenantService {
    TenantDTO createTenant(String name);

    void deleteTenant(String name);

    Page<TenantDTO> getTenants(int page, int size);

    Page<UserDTO> getAllUsers(Pageable pageable);

    TenantDTO getTenantById(Long tenantId);
}
