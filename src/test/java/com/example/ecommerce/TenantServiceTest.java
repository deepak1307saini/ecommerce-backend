package com.example.ecommerce;

import com.example.ecommerce.dto.TenantDTO;
import com.example.ecommerce.entity.Tenant;
import com.example.ecommerce.repository.TenantRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.impl.TenantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TenantServiceTest {

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TenantServiceImpl tenantService;

    private TenantDTO tenantDTO;

    @BeforeEach
    void setUp() {
        tenantDTO = new TenantDTO();
        tenantDTO.setId(1L);
        tenantDTO.setName("TestTenant");
    }

    @Test
    void testCreateTenant_Success() {
        Tenant tenant = new Tenant();
        tenant.setId(1L);
        tenant.setName("TestTenant");

        when(tenantRepository.save(any(Tenant.class))).thenReturn(tenant);

        TenantDTO result = tenantService.createTenant("TestTenant");

        assertNotNull(result);
        assertEquals("TestTenant", result.getName());
        verify(tenantRepository).save(any(Tenant.class));
    }

    @Test
    void testGetTenants_Success() {
        Tenant tenant = new Tenant();
        tenant.setId(1L);
        tenant.setName("TestTenant");
        Page<Tenant> tenantPage = new PageImpl<>(Collections.singletonList(tenant));

        when(tenantRepository.findAll(any(Pageable.class))).thenReturn(tenantPage);

        Page<TenantDTO> result = tenantService.getTenants(0, 5);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("TestTenant", result.getContent().get(0).getName());
    }
}