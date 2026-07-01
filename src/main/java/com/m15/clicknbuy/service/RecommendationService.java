package com.m15.clicknbuy.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.m15.clicknbuy.entity.CartItem;
import com.m15.clicknbuy.entity.Product;
import com.m15.clicknbuy.entity.User;
import com.m15.clicknbuy.repository.CartItemRepository;
import com.m15.clicknbuy.repository.OrderItemRepository;
import com.m15.clicknbuy.repository.ProductRepository;

/**
 * AI-Based Product Recommendation Engine.
 * 
 * Uses a hybrid content-based + collaborative filtering approach:
 * - Category affinity from purchase history
 * - Cart-based interest signals
 * - Popularity ranking across all users
 * - Price-range similarity matching
 */
@Service
public class RecommendationService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    /**
     * Get personalized product recommendations for a logged-in user.
     * Combines category affinity, cart signals, popularity, and price-range matching.
     */
    public List<Product> getRecommendationsForUser(User user, int limit) {
        // 1. Gather user signals
        List<String> purchasedCategories = orderItemRepository.findDistinctCategoriesByUser(user);
        List<Long> purchasedProductIds = orderItemRepository.findDistinctProductIdsByUser(user);
        Double avgPrice = orderItemRepository.findAveragePriceByUser(user);

        // Get cart items to also exclude and use as interest signal
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        List<Long> cartProductIds = cartItems.stream()
                .map(ci -> ci.getProduct().getId())
                .collect(Collectors.toList());
        List<String> cartCategories = cartItems.stream()
                .map(ci -> ci.getProduct().getCategory())
                .distinct()
                .collect(Collectors.toList());

        // Combine all exclusion IDs (purchased + in cart)
        Set<Long> excludeIds = new HashSet<>();
        excludeIds.addAll(purchasedProductIds);
        excludeIds.addAll(cartProductIds);

        // Combine all interest categories
        Set<String> interestCategories = new LinkedHashSet<>();
        interestCategories.addAll(purchasedCategories);
        interestCategories.addAll(cartCategories);

        // 2. Build popularity map: productId -> orderCount
        Map<Long, Long> popularityMap = new HashMap<>();
        List<Object[]> popularityData = orderItemRepository.findProductIdsByPopularity();
        for (Object[] row : popularityData) {
            Long productId = (Long) row[0];
            Long count = (Long) row[1];
            popularityMap.put(productId, count);
        }

        // 3. Get candidate products
        List<Product> candidates;
        if (!interestCategories.isEmpty() && !excludeIds.isEmpty()) {
            candidates = productRepository.findByCategoryInAndIdNotIn(
                    new ArrayList<>(interestCategories), new ArrayList<>(excludeIds));
        } else if (!interestCategories.isEmpty()) {
            candidates = productRepository.findByCategoryInAndStockGreaterThanZero(
                    new ArrayList<>(interestCategories));
        } else if (!excludeIds.isEmpty()) {
            candidates = productRepository.findByIdNotInAndStockGreaterThanZero(
                    new ArrayList<>(excludeIds));
        } else {
            candidates = productRepository.findTopProducts();
        }

        // Also add some popular products outside user's categories for diversity
        List<Product> allProducts;
        if (!excludeIds.isEmpty()) {
            allProducts = productRepository.findByIdNotInAndStockGreaterThanZero(new ArrayList<>(excludeIds));
        } else {
            allProducts = productRepository.findTopProducts();
        }
        // Add non-duplicate popular products
        Set<Long> candidateIds = candidates.stream().map(Product::getId).collect(Collectors.toSet());
        for (Product p : allProducts) {
            if (!candidateIds.contains(p.getId())) {
                candidates.add(p);
                candidateIds.add(p.getId());
            }
        }

        // 4. Score each candidate
        double finalAvgPrice = (avgPrice != null) ? avgPrice : 0.0;
        Set<String> purchasedCatSet = new HashSet<>(purchasedCategories);
        Set<String> cartCatSet = new HashSet<>(cartCategories);

        List<ScoredProduct> scored = candidates.stream()
                .map(product -> {
                    double score = 0;

                    // +3 for matching purchased category
                    if (purchasedCatSet.contains(product.getCategory())) {
                        score += 3.0;
                    }

                    // +2 for matching cart category
                    if (cartCatSet.contains(product.getCategory())) {
                        score += 2.0;
                    }

                    // +popularity bonus (normalized)
                    Long popCount = popularityMap.getOrDefault(product.getId(), 0L);
                    score += Math.min(popCount * 0.5, 3.0); // cap at 3

                    // +1 if price is within ±30% of user's average purchase price
                    if (finalAvgPrice > 0 && product.getPrice() != null) {
                        double lowerBound = finalAvgPrice * 0.7;
                        double upperBound = finalAvgPrice * 1.3;
                        if (product.getPrice() >= lowerBound && product.getPrice() <= upperBound) {
                            score += 1.0;
                        }
                    }

                    return new ScoredProduct(product, score);
                })
                .sorted(Comparator.comparingDouble(ScoredProduct::getScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());

        return scored.stream().map(ScoredProduct::getProduct).collect(Collectors.toList());
    }

    /**
     * Get trending/popular products for anonymous or new users (cold-start).
     */
    public List<Product> getTrendingProducts(int limit) {
        // Get popularity ranking
        List<Object[]> popularityData = orderItemRepository.findProductIdsByPopularity();
        
        if (!popularityData.isEmpty()) {
            List<Long> topProductIds = popularityData.stream()
                    .limit(limit * 2L) // fetch extra in case some are out of stock
                    .map(row -> (Long) row[0])
                    .collect(Collectors.toList());

            // Fetch actual products and filter by stock
            List<Product> popularProducts = productRepository.findAllById(topProductIds).stream()
                    .filter(p -> p.getStock() != null && p.getStock() > 0)
                    .limit(limit)
                    .collect(Collectors.toList());

            if (!popularProducts.isEmpty()) {
                return popularProducts;
            }
        }

        // Fallback: return newest products with stock
        return productRepository.findTopProducts().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Internal class for scoring products during recommendation ranking.
     */
    private static class ScoredProduct {
        private final Product product;
        private final double score;

        public ScoredProduct(Product product, double score) {
            this.product = product;
            this.score = score;
        }

        public Product getProduct() {
            return product;
        }

        public double getScore() {
            return score;
        }
    }
}
