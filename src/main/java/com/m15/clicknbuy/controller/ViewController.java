package com.m15.clicknbuy.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.m15.clicknbuy.dto.PasswordDto;
import com.m15.clicknbuy.dto.UserDto;
import com.m15.clicknbuy.entity.Product;
import com.m15.clicknbuy.entity.User;
import com.m15.clicknbuy.repository.ProductRepository;
import com.m15.clicknbuy.repository.UserRepository;
import com.m15.clicknbuy.service.RecommendationService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ViewController {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RecommendationService recommendationService;

	@GetMapping("/")
	public String loadHome(HttpSession session, ModelMap map, Principal principal,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "12") int size,
			@RequestParam(defaultValue = "name") String sort,
			@RequestParam(defaultValue = "asc") String direction,
			@RequestParam(required = false) String category) {
		manageMessage(session, map);
		
		Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
		Sort sortObj;
		
		// Handle special sorting cases
		switch (sort.toLowerCase()) {
			case "price":
				sortObj = Sort.by(sortDirection, "price");
				break;
			case "category":
				sortObj = Sort.by(sortDirection, "category");
				break;
			case "name":
			default:
				sortObj = Sort.by(sortDirection, "name");
				break;
		}
		
		PageRequest pageable = PageRequest.of(page, size, sortObj);
		Page<Product> productPage;
		
		if (category != null && !category.trim().isEmpty()) {
			productPage = productRepository.findByCategory(category, pageable);
			map.put("selectedCategory", category);
		} else {
			productPage = productRepository.findAll(pageable);
		}
		
		map.put("categories", productRepository.findAllCategories());
		map.put("products", productPage.getContent());
		map.put("currentPage", page);
		map.put("totalPages", productPage.getTotalPages());
		map.put("totalItems", productPage.getTotalElements());
		map.put("sort", sort);
		map.put("direction", direction);

		// AI Recommendations
		try {
			List<Product> recommendations;
			if (principal != null) {
				Optional<User> optUser = userRepository.findByEmail(principal.getName());
				if (optUser.isPresent()) {
					recommendations = recommendationService.getRecommendationsForUser(optUser.get(), 8);
					map.put("recommendationTitle", "Recommended For You");
					map.put("recommendationSubtitle", "Personalized picks based on your interests");
				} else {
					recommendations = recommendationService.getTrendingProducts(8);
					map.put("recommendationTitle", "Trending Now");
					map.put("recommendationSubtitle", "Popular products our customers love");
				}
			} else {
				recommendations = recommendationService.getTrendingProducts(8);
				map.put("recommendationTitle", "Trending Now");
				map.put("recommendationSubtitle", "Popular products our customers love");
			}
			map.put("recommendedProducts", recommendations);
		} catch (Exception e) {
			// Gracefully handle recommendation errors - don't break the home page
			map.put("recommendedProducts", Collections.emptyList());
		}

		return "home.html";
	}

	@GetMapping("/recommendations")
	public String loadRecommendations(HttpSession session, ModelMap map, Principal principal) {
		manageMessage(session, map);

		try {
			if (principal != null) {
				Optional<User> optUser = userRepository.findByEmail(principal.getName());
				if (optUser.isPresent()) {
					User user = optUser.get();
					map.put("personalRecommendations", recommendationService.getRecommendationsForUser(user, 12));
					map.put("isPersonalized", true);
				} else {
					map.put("personalRecommendations", Collections.emptyList());
					map.put("isPersonalized", false);
				}
			} else {
				map.put("personalRecommendations", Collections.emptyList());
				map.put("isPersonalized", false);
			}
			map.put("trendingProducts", recommendationService.getTrendingProducts(12));
		} catch (Exception e) {
			map.put("personalRecommendations", Collections.emptyList());
			map.put("trendingProducts", Collections.emptyList());
			map.put("isPersonalized", false);
		}

		return "recommendations.html";
	}

	@GetMapping("/login")
	public String loadLogin(HttpSession session, ModelMap map) {
		manageMessage(session, map);
		return "login.html";
	}

	@GetMapping("/register")
	public String loadRegister(UserDto userDto) {
		return "register.html";
	}

	@GetMapping("/otp")
	public String loadOtop(HttpSession session, ModelMap map) {
		if (session.getAttribute("id") != null) {
			map.put("id", session.getAttribute("id"));
		}
		manageMessage(session, map);
		return "otp.html";
	}

	@GetMapping("/forgot-password")
	public String loadForgotPassword(HttpSession session, ModelMap map) {
		manageMessage(session, map);
		return "forgot-password.html";
	}

	@GetMapping("/reset-password")
	public String loadResetPassword(PasswordDto passwordDto, HttpSession session, ModelMap map) {
		if (session.getAttribute("id") != null) {
			map.put("id", session.getAttribute("id"));
		}
		manageMessage(session, map);
		return "reset-password.html";
	}

	@GetMapping("/offers")
	public String loadOffers() {
		return "offers.html";
	}

	public void manageMessage(HttpSession session, ModelMap map) {
		if (session.getAttribute("success") != null) {
			map.put("success", session.getAttribute("success"));
			session.removeAttribute("success");
		}
		if (session.getAttribute("error") != null) {
			map.put("error", session.getAttribute("error"));
			session.removeAttribute("error");
		}
	}
}

