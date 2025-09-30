package com.example.ecommerce.controller;

import com.example.ecommerce.config.Context;
import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderRequest;
import com.example.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderRequest request) {
        Long userId = Context.getUserId();
        logger.info("Creating order for user ID: {}", userId);
        OrderDTO order = orderService.createOrder(userId, request.getItems());
        logger.info("Order created with ID: {}", order.getId());
        return ResponseEntity.ok(order);
    }

    @GetMapping("/orders")
    public Page<OrderDTO> getOrderHistory(Pageable pageable) {
        logger.info("Fetching order history for user ID: {}", Context.getUserId());
        return orderService.getOrderHistory(Context.getUserId(), pageable);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        logger.info("Fetching order with ID: {} for user ID: {}", id, Context.getUserId());
        OrderDTO order = orderService.getOrderById(id, Context.getUserId());
        return ResponseEntity.ok(order);
    }

    @GetMapping("/tenant/{tenantName}/orders")
    @PreAuthorize("hasRole('TENANT_ADMIN')")
    public Page<OrderDTO> getTenantOrders(Pageable pageable) {
        Long tenantId = Context.getTenantId();
        logger.info("Fetching order history for tenant ID: {}, by user ID: {}", tenantId, Context.getUserId());
        return orderService.getOrdersByTenant(tenantId, pageable);
    }
}