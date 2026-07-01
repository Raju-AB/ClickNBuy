package com.m15.clicknbuy.service;

import java.util.List;

import com.m15.clicknbuy.entity.Notification;
import com.m15.clicknbuy.entity.User;

public interface NotificationService {

    /**
     * Create a notification, persist it, and send it in real-time via WebSocket.
     */
    Notification createAndSend(User user, String title, String message, String type, Long orderId);

    /**
     * Get all notifications for a user (newest first).
     */
    List<Notification> getNotifications(User user);

    /**
     * Get the count of unread notifications.
     */
    long getUnreadCount(User user);

    /**
     * Mark a single notification as read.
     */
    void markAsRead(Long notificationId);

    /**
     * Mark all notifications for a user as read.
     */
    void markAllAsRead(User user);
}
