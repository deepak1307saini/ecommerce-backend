package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.FavoriteDTO;
import com.example.ecommerce.entity.Favorite;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.FavoriteRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final Logger logger = LoggerFactory.getLogger(FavoriteServiceImpl.class);

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public FavoriteDTO addFavorite(Long userId, Long productId) {
        logger.info("Adding favorite for user ID: {} and product ID: {}", userId, productId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        // Check if favorite already exists to avoid duplicates
        Optional<Favorite> existingFavorite = favoriteRepository.findByUserIdAndProductId(userId, productId);
        if (existingFavorite.isPresent()) {
            logger.info("Favorite already exists for user ID: {} and product ID: {}", userId, productId);
            return toDTO(existingFavorite.get());
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);

        Favorite savedFavorite = favoriteRepository.save(favorite);
        logger.info("Favorite added with ID: {}, user ID: {} and product ID: {}.", savedFavorite.getId(),
                savedFavorite.getUserId(), savedFavorite.getProductId());
        return toDTO(savedFavorite);
    }

    @Override
    public void removeFavorite(Long userId, Long productId) {
        logger.info("Removing favorite for user ID: {} and product ID: {}", userId, productId);
        Favorite favorite = favoriteRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));
        favoriteRepository.delete(favorite);
        logger.info("Favorite removed for user ID: {} and product ID: {}", userId, productId);
    }

    @Override
    public Page<FavoriteDTO> getFavorites(Long userId, Pageable pageable) {
        logger.info("Fetching favorites for user ID: {}", userId);
        return favoriteRepository.findByUserId(userId, pageable)
                .map(this::toDTO);
    }

    private FavoriteDTO toDTO(Favorite favorite) {
        FavoriteDTO dto = new FavoriteDTO();
        dto.setId(favorite.getId());
        dto.setUserId(favorite.getUserId());
        dto.setProductId(favorite.getProductId());
        dto.setProductName(favorite.getProduct().getName());
        return dto;
    }
}