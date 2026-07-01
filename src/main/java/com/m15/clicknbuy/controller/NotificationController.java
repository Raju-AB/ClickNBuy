package com.m15.clicknbuy.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.m15.clicknbuy.entity.Notification;
import com.m15.clicknbuy.entity.User;
import com.m15.clicknbuy.repository.UserRepository;
import com.m15.clicknbuy.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getNotifications(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        List<Notification> notifications = notificationService.getNotifications(user);
        List<Map<String, Object>> result = notifications.stream().map(n -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", n.getId());
            map.put("title", n.getTitle());
            map.put("message", n.getMessage());
            map.put("type", n.getType());
            map.put("orderId", n.getOrderId());
            map.put("read", n.isRead());
            map.put("createdAt", n.getCreatedAt() != null ? n.getCreatedAt().toString() : null);
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        long count = notificationService.getUnreadCount(user);
        Map<String, Long> result = new HashMap<>();
        result.put("count", count);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Map<String, String>> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        Map<String, String> result = new HashMap<>();
        result.put("status", "ok");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/read-all")
    public ResponseEntity<Map<String, String>> markAllAsRead(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        notificationService.markAllAsRead(user);
        Map<String, String> result = new HashMap<>();
        result.put("status", "ok");
        return ResponseEntity.ok(result);
    }
}
