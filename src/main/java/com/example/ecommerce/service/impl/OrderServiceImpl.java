package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderItemDTO;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.exception.InsufficientStockException;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Override
    @Transactional
    public OrderDTO createOrder(Long userId, List<OrderItemDTO> items) {
        Order savedOrder = saveOrder(userId, items);

        List<OrderItem> savedItems = getSavedItems(items, savedOrder);

        OrderDTO orderDTO = toDTO(savedOrder);
        orderDTO.setItems(savedItems.stream().map(this::toItemDTO).collect(Collectors.toList()));
        log.info("Order created with ID: {}", savedOrder.getId());
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> getOrderHistory(Long userId, Pageable pageable) {
        log.info("Fetching order history for user ID: {}", userId);
        return orderRepository.findByUserId(userId, pageable).map(this::toDTO);
    }

    @Override
    public Page<OrderDTO> getOrdersByTenant(Long tenantId, Pageable pageable) {
        Page<OrderDTO> orders=  orderRepository.findByItemProductTenantName(tenantId, pageable).map(this::toDTO);
        orders.forEach(orderDTO -> orderDTO.setTotalAmount(orderItemRepository.getOrderPrice(tenantId, orderDTO.getId())));
        return orders;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id, Long userId) {
        log.info("Fetching order with ID: {} for user ID: {}", id, userId);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));

        // Validate that the order belongs to the user
        if (!order.getUserId().equals(userId)) {
            log.warn("User ID: {} is not authorized to access order ID: {}", userId, id);
            throw new RuntimeException("Unauthorized to access this order");
        }

        return toDTO(order);
    }

    private List<OrderItem> getSavedItems(List<OrderItemDTO> items, Order savedOrder) {
        return items.stream().map(itemDTO -> {
            OrderItem item = new OrderItem();
            item.setOrderId(savedOrder.getId());
            item.setProductId(itemDTO.getProductId());
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(itemDTO.getPrice());
            return orderItemRepository.save(item);
        }).collect(Collectors.toList());
    }

    private Order saveOrder(Long userId, List<OrderItemDTO> items) {
        log.info("Creating order for user ID: {}", userId);
        double totalAmount = 0;
        int totalQuantity = 0;

        for (OrderItemDTO itemDTO : items) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            if (itemDTO.getQuantity() > product.getAvailableQuantity()) {
                log.warn("Insufficient quantity for product: {}", product.getName());
                throw new InsufficientStockException("Insufficient quantity for product: " + product.getName());
            }
            itemDTO.setPrice(product.getPrice());
            product.setAvailableQuantity(product.getAvailableQuantity() - itemDTO.getQuantity());
            productRepository.save(product);
            totalQuantity += itemDTO.getQuantity();
            totalAmount += itemDTO.getQuantity() * product.getPrice();
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalQuantity(totalQuantity);
        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    }

    private OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setTotalQuantity(order.getTotalQuantity());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());  // Added order date
        List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
        dto.setItems(items.stream().map(this::toItemDTO).collect(Collectors.toList()));
        return dto;
    }

    private OrderItemDTO toItemDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProductId());
        dto.setQuantity(item.getQuantity());
        dto.setProduct(productService.getProductById(item.getProductId()));
        return dto;
    }
}