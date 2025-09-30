package com.example.ecommerce.controller;

import com.example.ecommerce.config.Context;
import com.example.ecommerce.dto.FavoriteDTO;
import com.example.ecommerce.dto.FavoriteRequest;
import com.example.ecommerce.service.FavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final Logger logger = LoggerFactory.getLogger(FavoriteController.class);

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<FavoriteDTO> addFavorite(@Valid @RequestBody FavoriteRequest request) {
        Long userId = Context.getUserId();
        logger.info("Adding favorite for user ID: {} and product ID: {}", userId, request.getProductId());
        FavoriteDTO favorite = favoriteService.addFavorite(userId, request.getProductId());
        logger.info("Favorite added with ID: {}", favorite.getId());
        return ResponseEntity.ok(favorite);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(@Valid @RequestBody FavoriteRequest request) {
        Long userId = Context.getUserId();
        logger.info("Removing favorite for user ID: {} and product ID: {}", userId, request.getProductId());
        favoriteService.removeFavorite(userId, request.getProductId());
        logger.info("Favorite removed");
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public Page<FavoriteDTO> getFavorites(Pageable pageable) {
        Long userId = Context.getUserId();
        logger.info("Fetching favorites for user ID: {}", userId);
        return favoriteService.getFavorites(userId, pageable);
    }
}