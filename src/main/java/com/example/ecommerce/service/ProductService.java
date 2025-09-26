package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface ProductService {
    Page<ProductDTO> getAll(Pageable pageable);

    Page<ProductDTO> searchByName(String name, Pageable pageable);

    Page<ProductDTO> filterByCategory(String category, Pageable pageable);

    Page<ProductDTO> getByTenant(String tenantName, Pageable pageable);

    ProductDTO save(ProductDTO productDTO, String tenantName) throws IOException;

    ProductDTO update(ProductDTO updatedProductDTO, String tenantName) throws IOException;

    void delete(Long id, String tenantName);

    void checkTenantAccess(Long tenantId);

    ProductDTO getProductById(Long productId);
}
