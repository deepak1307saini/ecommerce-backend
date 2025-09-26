package com.example.ecommerce.controller;

import com.example.ecommerce.dto.TenantDTO;
import com.example.ecommerce.dto.TenantRequest;
import com.example.ecommerce.dto.UserDTO;
import com.example.ecommerce.enums.RoleEnum;
import com.example.ecommerce.service.TenantService;
import com.example.ecommerce.service.UserService;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class TenantController {

    private final TenantService tenantService;
    private final UserService userService;

    @PostMapping("/tenants")
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public ResponseEntity<TenantDTO> createTenant(@RequestBody TenantRequest request) {
        log.info("Creating tenant: {}", request.getName());
        TenantDTO tenant = tenantService.createTenant(request.getName());
        log.info("Tenant created with ID: {}", tenant.getId());
        return ResponseEntity.ok(tenant);
    }

    @GetMapping("/tenants")
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public ResponseEntity<Page<TenantDTO>> getTenants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        log.info("Fetching tenants: page={}, size={}", page, size);
        Page<TenantDTO> tenants = tenantService.getTenants(page, size);
        log.info("Fetched {} tenants", tenants.getTotalElements());
        return ResponseEntity.ok(tenants);
    }

    @DeleteMapping("/tenants/{name}")
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public ResponseEntity<Void> deleteTenant(@PathVariable String name) {
        log.info("Deleting tenant: {}", name);
        tenantService.deleteTenant(name);
        log.info("Tenant deleted: {}", name);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tenant-admin")
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public ResponseEntity<Void> createTenantAdmin(@RequestBody UserDTO request) {
        if(isNull(request.getTenantId())) throw new BadRequestException("tenantId can't be null");
        log.info("Creating tenant admin: {}", request.getUsername());
        userService.registerUser(request, RoleEnum.TENANT_ADMIN.toString());
        log.info("Tenant admin created: {}", request.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> getUsers(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(tenantService.getAllUsers(pageable));
    }
}