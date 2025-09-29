package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByUserId(Long userId, Pageable pageable);

    /**
     * Finds orders that contain at least one item whose product belongs to the specified tenant.
     * Uses DISTINCT to avoid duplicate orders when joining with order items.
     *
     * @param tenantId The id of the tenant to filter orders by.
     * @param pageable   Pagination and sorting parameters.
     * @return A page of orders associated with the tenant's products.
     */
    @Query("SELECT DISTINCT o FROM Order o JOIN OrderItem oi ON o.id = oi.orderId " +
            "JOIN Product p ON oi.productId = p.id " +
            "WHERE p.tenantId = :tenantId")
    Page<Order> findByItemProductTenantName(@Param("tenantId") Long tenantId, Pageable pageable);
}