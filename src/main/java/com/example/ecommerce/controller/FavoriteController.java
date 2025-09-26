package com.example.ecommerce.controller;

import com.example.ecommerce.config.Context;
import com.example.ecommerce.dto.FavoriteDTO;
import com.example.ecommerce.dto.FavoriteRequest;
import com.example.ecommerce.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@Slf4j
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<FavoriteDTO> addFavorite(@RequestBody FavoriteRequest request) {
        Long userId = Context.getUserId();
        log.info("Adding favorite for user ID: {} and product ID: {}", userId, request.getProductId());
        FavoriteDTO favorite = favoriteService.addFavorite(userId, request.getProductId());
        log.info("Favorite added with ID: {}", favorite.getId());
        return ResponseEntity.ok(favorite);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(@RequestBody FavoriteRequest request) {
        Long userId = Context.getUserId();
        log.info("Removing favorite for user ID: {} and product ID: {}", userId, request.getProductId());
        favoriteService.removeFavorite(userId, request.getProductId());
        log.info("Favorite removed");
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public Page<FavoriteDTO> getFavorites(Pageable pageable) {
        Long userId = Context.getUserId();
        log.info("Fetching favorites for user ID: {}", userId);
        return favoriteService.getFavorites(userId, pageable);
    }
}