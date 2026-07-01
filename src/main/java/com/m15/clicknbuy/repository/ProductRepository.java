package com.m15.clicknbuy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.m15.clicknbuy.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

	boolean existsByName(String name);

	Page<Product> findByCategory(String category, Pageable pageable);

	@Query("SELECT DISTINCT p.category FROM Product p ORDER BY p.category ASC")
	List<String> findAllCategories();

	// Find products in given categories, excluding specific product IDs, with stock > 0
	@Query("SELECT p FROM Product p WHERE p.category IN :categories AND p.id NOT IN :excludeIds AND p.stock > 0")
	List<Product> findByCategoryInAndIdNotIn(@Param("categories") List<String> categories, @Param("excludeIds") List<Long> excludeIds);

	// Find products in given categories with stock > 0
	@Query("SELECT p FROM Product p WHERE p.category IN :categories AND p.stock > 0")
	List<Product> findByCategoryInAndStockGreaterThanZero(@Param("categories") List<String> categories);

	// Find products NOT in the given IDs, with stock > 0
	@Query("SELECT p FROM Product p WHERE p.id NOT IN :excludeIds AND p.stock > 0")
	List<Product> findByIdNotInAndStockGreaterThanZero(@Param("excludeIds") List<Long> excludeIds);

	// Find top products by order popularity (most ordered first)
	@Query("SELECT p FROM Product p WHERE p.stock > 0 ORDER BY p.createdTime DESC")
	List<Product> findTopProducts();
}
