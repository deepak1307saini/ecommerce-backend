package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.FavoriteDTO;
import com.example.ecommerce.entity.Favorite;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.FavoriteRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;

    @Override
    public FavoriteDTO addFavorite(Long userId, Long productId) {
        log.info("Adding favorite for user ID: {} and product ID: {}", userId, productId);
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setProductId(productId);
        Favorite savedFavorite = favoriteRepository.save(favorite);
        log.info("Favorite added with ID: {}", savedFavorite.getId());
        return toDTO(savedFavorite, null);
    }

    @Override
    public void removeFavorite(Long userId, Long productId) {
        log.info("Removing favorite for user ID: {} and product ID: {}", userId, productId);
        Favorite favorite = favoriteRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));
        favoriteRepository.delete(favorite);
        log.info("Favorite removed for user ID: {} and product ID: {}", userId, productId);
    }

    @Override
    public Page<FavoriteDTO> getFavorites(Long userId, Pageable pageable) {
        log.info("Fetching favorites for user ID: {}", userId);
        return favoriteRepository.findByUserId(userId, pageable)
                .map(favorite -> {
                    // Fetch product details using productId
                    Product product = productRepository.findById(favorite.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));
                    return toDTO(favorite, product.getName());
                });
    }

    private FavoriteDTO toDTO(Favorite favorite, String productName) {
        FavoriteDTO dto = new FavoriteDTO();
        dto.setId(favorite.getId());
        dto.setUserId(favorite.getUserId());
        dto.setProductId(favorite.getProductId());
        dto.setProductName(productName);
        return dto;
    }
}