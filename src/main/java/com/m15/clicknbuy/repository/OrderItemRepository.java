package com.m15.clicknbuy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.m15.clicknbuy.entity.OrderItem;
import com.m15.clicknbuy.entity.User;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Find all order items for a given user (through Order -> User)
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.user = :user")
    List<OrderItem> findByOrderUser(@Param("user") User user);

    // Get product IDs ranked by how many times they've been ordered (popularity)
    @Query("SELECT oi.product.id, COUNT(oi) as cnt FROM OrderItem oi GROUP BY oi.product.id ORDER BY cnt DESC")
    List<Object[]> findProductIdsByPopularity();

    // Get distinct categories that a user has purchased
    @Query("SELECT DISTINCT oi.product.category FROM OrderItem oi WHERE oi.order.user = :user")
    List<String> findDistinctCategoriesByUser(@Param("user") User user);

    // Get average price of products a user has purchased
    @Query("SELECT AVG(oi.price) FROM OrderItem oi WHERE oi.order.user = :user")
    Double findAveragePriceByUser(@Param("user") User user);

    // Get distinct product IDs a user has already purchased
    @Query("SELECT DISTINCT oi.product.id FROM OrderItem oi WHERE oi.order.user = :user")
    List<Long> findDistinctProductIdsByUser(@Param("user") User user);
}
