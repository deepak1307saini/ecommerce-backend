package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.TenantDTO;
import com.example.ecommerce.dto.UserDTO;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.Tenant;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.enums.RoleEnum;
import com.example.ecommerce.repository.TenantRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;

    @Override
    public TenantDTO createTenant(String name) {
        log.info("Creating tenant: {}", name);
        Tenant tenant = new Tenant();
        tenant.setName(name);
        Tenant savedTenant = tenantRepository.save(tenant);
        log.info("Tenant created with ID: {}", savedTenant.getId());
        return toDTO(savedTenant);
    }

    @Override
    public void deleteTenant(String name) {
        log.info("Deleting tenant: {}", name);
        Tenant tenant = tenantRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));
        tenantRepository.delete(tenant);
        log.info("Tenant deleted: {}", name);
    }

    @Override
    public Page<TenantDTO> getTenants(int page, int size) {
        log.info("Fetching tenants: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Tenant> tenantPage = tenantRepository.findAll(pageable);
        log.info("Fetched {} tenants", tenantPage.getTotalElements());
        return tenantPage.map(this::toDTO);
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        log.info("Fetching users with role TENANT_ADMIN: pageable={}", pageable);
        Page<User> users = userRepository.findUsersByRoleName(RoleEnum.TENANT_ADMIN.name(), pageable);
        log.info("Fetched {} users with role TENANT_ADMIN", users.getTotalElements());
        return users.map(this::toUserDTO);
    }

    @Override
    public TenantDTO getTenantById(Long tenantId){
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        return toDTO(tenant);
    }
    private TenantDTO toDTO(Tenant tenant) {
        TenantDTO dto = new TenantDTO();
        dto.setId(tenant.getId());
        dto.setName(tenant.getName());
        return dto;
    }

    private UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setTenantId(user.getTenantId());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        return dto;
    }

}