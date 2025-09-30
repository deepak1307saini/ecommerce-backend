package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.TenantRepository;
import com.example.ecommerce.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final TenantRepository tenantRepository;

    @Override
    public Page<ProductDTO> getAll(Pageable pageable) {
        logger.info("Fetching all products, page: {}", pageable.getPageNumber());
        return productRepository.findAll(pageable).map(this::toDTO);
    }

    @Override
    public Page<ProductDTO> searchByName(String name, Pageable pageable) {
        logger.info("Searching products by name: {}", name);
        return productRepository.findByNameContainingIgnoreCase(name, pageable).map(this::toDTO);
    }

    @Override
    public Page<ProductDTO> filterByCategory(String category, Pageable pageable) {
        logger.info("Filtering products by category: {}", category);
        return productRepository.findByCategory(category, pageable).map(this::toDTO);
    }

    @Override
    public Page<ProductDTO> getByTenant(String tenantName, Pageable pageable) {
        Long tenantId = getTenantIdByName(tenantName);
        logger.info("Fetching products for tenant: {}", tenantName);
        return productRepository.findByTenantId(tenantId, pageable).map(this::toDTO);
    }

    @Override
    public ProductDTO save(ProductDTO productDTO, String tenantName) throws IOException {
        Long tenantId = getTenantIdByName(tenantName);
        Product product = toEntity(productDTO);
        product.setTenantId(tenantId);

        Product savedProduct = productRepository.save(product);
        logger.info("Saved product: {}", savedProduct.getName());
        return toDTO(savedProduct);
    }

    @Override
    @Transactional
    public ProductDTO update(ProductDTO updatedProductDTO, String tenantName) throws IOException {
        Long tenantId = getTenantIdByName(tenantName);
        Product product = productRepository.findById(updatedProductDTO.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (!product.getTenantId().equals(tenantId)) {
            logger.warn("Unauthorized tenant access for product ID: {}", updatedProductDTO.getId());
            throw new RuntimeException("Unauthorized tenant");
        }
        product.setName(updatedProductDTO.getName());
        product.setCategory(updatedProductDTO.getCategory());
        product.setPrice(updatedProductDTO.getPrice());
        product.setAvailableQuantity(updatedProductDTO.getAvailableQuantity());
        product.setDescription(updatedProductDTO.getDescription());
        product.setThumbnail(updatedProductDTO.getThumbnail());
        Product updatedProduct = productRepository.save(product);
        logger.info("Updated product: {}", updatedProduct.getName());
        return toDTO(updatedProduct);
    }

    @Override
    public void delete(Long id, String tenantName) {
        Long tenantId = getTenantIdByName(tenantName);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (!product.getTenantId().equals(tenantId)) {
            logger.warn("Unauthorized tenant access for product ID: {}", id);
            throw new RuntimeException("Unauthorized tenant");
        }
        productRepository.deleteById(id);
        logger.info("Deleted product ID: {}", id);
    }

    @Override
    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        return toDTO(product);
    }

    public Long getTenantIdByName(String name) {
        return tenantRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Tenant not found"))
                .getId();
    }

    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setAvailableQuantity(product.getAvailableQuantity());
        dto.setThumbnail(product.getThumbnail());
        dto.setDescription(product.getDescription());
        return dto;
    }

    private Product toEntity(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        product.setAvailableQuantity(dto.getAvailableQuantity());
        product.setThumbnail(dto.getThumbnail());
        product.setDescription(dto.getDescription());
        return product;
    }
}