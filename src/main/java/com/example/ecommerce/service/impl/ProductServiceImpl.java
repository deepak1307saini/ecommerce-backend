package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.TenantRepository;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final TenantRepository tenantRepository;
    private final UserService userService;

    @Override
    public Page<ProductDTO> getAll(Pageable pageable) {
        log.info("Fetching all products, page: {}", pageable.getPageNumber());
        return productRepository.findAll(pageable).map(this::toDTO);
    }

    @Override
    public Page<ProductDTO> searchByName(String name, Pageable pageable) {
        log.info("Searching products by name: {}", name);
        return productRepository.findByNameContainingIgnoreCase(name, pageable).map(this::toDTO);
    }

    @Override
    public Page<ProductDTO> filterByCategory(String category, Pageable pageable) {
        log.info("Filtering products by category: {}", category);
        return productRepository.findByCategory(category, pageable).map(this::toDTO);
    }

    @Override
    public Page<ProductDTO> getByTenant(String tenantName, Pageable pageable) {
        Long tenantId = getTenantIdByName(tenantName);
        checkTenantAccess(tenantId);
        log.info("Fetching products for tenant: {}", tenantName);
        return productRepository.findByTenantId(tenantId, pageable).map(this::toDTO);
    }

    @Override
    public ProductDTO save(ProductDTO productDTO, String tenantName) throws IOException {
        Long tenantId = getTenantIdByName(tenantName);
        checkTenantAccess(tenantId);
        Product product = toEntity(productDTO);
        product.setTenantId(tenantId);

        Product savedProduct = productRepository.save(product);
        log.info("Saved product: {}", savedProduct.getName());
        return toDTO(savedProduct);
    }

    @Override
    public ProductDTO update(ProductDTO updatedProductDTO, String tenantName) throws IOException {
        Long tenantId = getTenantIdByName(tenantName);
        checkTenantAccess(tenantId);
        Product product = productRepository.findById(updatedProductDTO.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (!product.getTenantId().equals(tenantId)) {
            log.warn("Unauthorized tenant access for product ID: {}", updatedProductDTO.getId());
            throw new RuntimeException("Unauthorized tenant");
        }
        product.setName(updatedProductDTO.getName());
        product.setCategory(updatedProductDTO.getCategory());
        product.setPrice(updatedProductDTO.getPrice());
        product.setAvailableQuantity(updatedProductDTO.getAvailableQuantity());
        product.setDescription(updatedProductDTO.getDescription());
        product.setThumbnail(updatedProductDTO.getThumbnail());
        Product updatedProduct = productRepository.save(product);
        log.info("Updated product: {}", updatedProduct.getName());
        return toDTO(updatedProduct);
    }

    @Override
    public void delete(Long id, String tenantName) {
        Long tenantId = getTenantIdByName(tenantName);
        checkTenantAccess(tenantId);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (!product.getTenantId().equals(tenantId)) {
            log.warn("Unauthorized tenant access for product ID: {}", id);
            throw new RuntimeException("Unauthorized tenant");
        }
        productRepository.deleteById(id);
        log.info("Deleted product ID: {}", id);
    }

    public Long getTenantIdByName(String name) {
        return tenantRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Tenant not found"))
                .getId();
    }


    @Override
    public void checkTenantAccess(Long tenantId) {
        User user = userService.getCurrentUser();
        if (user.getTenantId() != null && !user.getTenantId().equals(tenantId)) {
            log.warn("Unauthorized tenant access for user: {}", user.getUsername());
            throw new RuntimeException("Unauthorized tenant access");
        }
    }

    private String saveImage(MultipartFile file, String tenantName, String productName) throws IOException {
        String uploadDir = "uploads/" + tenantName + "/";
        String fileName = productName.replaceAll("\\s+", "_") + "_" + System.currentTimeMillis() + ".jpg";
        Path path = Paths.get(uploadDir + fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());
        return "/images/" + tenantName + "/" + fileName;
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

    @Override
    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        return toDTO(product);
    }
}