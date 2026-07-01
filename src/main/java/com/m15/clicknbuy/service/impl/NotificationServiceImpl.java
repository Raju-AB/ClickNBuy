package com.m15.clicknbuy.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.m15.clicknbuy.entity.Notification;
import com.m15.clicknbuy.entity.User;
import com.m15.clicknbuy.repository.NotificationRepository;
import com.m15.clicknbuy.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public Notification createAndSend(User user, String title, String message, String type, Long orderId) {
        // Persist the notification
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);
        notification.setOrderId(orderId);
        notification = notificationRepository.save(notification);

        // Build the payload for the WebSocket message
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", notification.getId());
        payload.put("title", notification.getTitle());
        payload.put("message", notification.getMessage());
        payload.put("type", notification.getType());
        payload.put("orderId", notification.getOrderId());
        payload.put("createdAt", notification.getCreatedAt() != null ? notification.getCreatedAt().toString() : null);
        payload.put("read", notification.isRead());

        // Send to the specific user's queue via their email (Spring Security principal name)
        messagingTemplate.convertAndSendToUser(
                user.getEmail(),
                "/queue/notifications",
                payload
        );

        return notification;
    }

    @Override
    public List<Notification> getNotifications(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public long getUnreadCount(User user) {
        return notificationRepository.countByUserAndReadFalse(user);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    @Override
    @Transactional
    public void markAllAsRead(User user) {
        notificationRepository.markAllAsReadForUser(user);
    }
}
