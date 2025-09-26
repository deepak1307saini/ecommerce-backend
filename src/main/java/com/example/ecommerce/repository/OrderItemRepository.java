package com.example.ecommerce.repository;

import com.example.ecommerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long id);

    /**
     * Calculates the total price of order items for a given order and tenant.
     *
     * @param tenantId The ID of the tenant whose products are considered.
     * @param orderId  The ID of the order to calculate the price for.
     * @return The sum of the prices of order items for the specified tenant and order.
     */
    @Query("SELECT SUM(oi.price * oi.quantity) FROM OrderItem oi " +
            "JOIN Product p ON oi.productId = p.id " +
            "WHERE p.tenantId = :tenantId AND oi.orderId = :orderId")
    double getOrderPrice(@Param("tenantId") Long tenantId, @Param("orderId") Long orderId);
}