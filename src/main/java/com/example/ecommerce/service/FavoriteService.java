package com.example.ecommerce.service;

import com.example.ecommerce.dto.FavoriteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteService {
    FavoriteDTO addFavorite(Long userId, Long productId);

    void removeFavorite(Long userId, Long productId);

    Page<FavoriteDTO> getFavorites(Long userId, Pageable pageable);
}
