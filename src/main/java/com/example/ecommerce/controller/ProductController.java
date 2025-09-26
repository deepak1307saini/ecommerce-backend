package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping("/public/products")
    public Page<ProductDTO> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            Pageable pageable) {
        log.info("Fetching products with search: {}, category: {}", search, category);
        if (nonNull(search)) {
            return productService.searchByName(search, pageable);
        } else if (nonNull(category))    {
            return productService.filterByCategory(category, pageable);
        }
        return productService.getAll(pageable);
    }

    @GetMapping("/tenant/{tenantName}/products")
    @PreAuthorize("hasRole('TENANT_ADMIN')")
    public Page<ProductDTO> getTenantProducts(@PathVariable String tenantName, Pageable pageable) {
        log.info("Fetching products for tenant: {}", tenantName);
        return productService.getByTenant(tenantName, pageable);
    }

    @PostMapping("/tenant/{tenantName}/products")
    @PreAuthorize("hasRole('TENANT_ADMIN')")
    public ResponseEntity<ProductDTO> addProduct(
            @PathVariable String tenantName,
            @RequestPart("product") ProductDTO productDTO) throws IOException {
        log.info("Adding product for tenant: {}", tenantName);
        ProductDTO savedProduct = productService.save(productDTO, tenantName);
        log.info("Product added: {}", productDTO.getName());
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/tenant/{tenantName}/products/{id}")
    @PreAuthorize("hasRole('TENANT_ADMIN')")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable String tenantName,
            @PathVariable Long id,
            @RequestPart("product") ProductDTO productDTO) throws IOException {
        log.info("Updating product ID: {} for tenant: {}", id, tenantName);
        productDTO.setId(id);
        ProductDTO updatedProduct = productService.update(productDTO, tenantName);
        log.info("Product updated: {}", updatedProduct.getName());
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/tenant/{tenantName}/products/{id}")
    @PreAuthorize("hasRole('TENANT_ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable String tenantName, @PathVariable Long id) {
        log.info("Deleting product ID: {} for tenant: {}", id, tenantName);
        productService.delete(id, tenantName);
        log.info("Product deleted: {}", id);
        return ResponseEntity.ok().build();
    }
}