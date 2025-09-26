package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderService {
    @Transactional
    OrderDTO createOrder(Long userId, List<OrderItemDTO> items);

    Page<OrderDTO> getOrderHistory(Long userId, Pageable pageable);

    Page<OrderDTO> getOrdersByTenant(Long tenantId, Pageable pageable);

    OrderDTO getOrderById(Long id, Long userId);
}
