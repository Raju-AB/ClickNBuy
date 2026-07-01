package com.m15.clicknbuy.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.m15.clicknbuy.entity.Order;
import com.m15.clicknbuy.entity.User;
import com.m15.clicknbuy.repository.OrderRepository;
import com.m15.clicknbuy.service.NotificationService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminOrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/orders")
    public String viewAllOrders(ModelMap map, HttpSession session) {
        if (session.getAttribute("success") != null) {
            map.put("success", session.getAttribute("success"));
            session.removeAttribute("success");
        }
        if (session.getAttribute("error") != null) {
            map.put("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }

        List<Order> orders = orderRepository.findAll();
        map.put("orders", orders);
        return "admin-orders";
    }

    @PostMapping("/orders/{orderId}/status")
    public String updateOrderStatus(@PathVariable Long orderId,
                                     @RequestParam String status,
                                     HttpSession session) {
        Optional<Order> optOrder = orderRepository.findById(orderId);
        if (optOrder.isEmpty()) {
            session.setAttribute("error", "Order not found");
            return "redirect:/admin/orders";
        }

        Order order = optOrder.get();
        String oldStatus = order.getStatus();
        order.setStatus(status);
        orderRepository.save(order);

        // Send real-time notification to the user
        User user = order.getUser();
        if (user != null) {
            String title = getNotificationTitle(status);
            String message = getNotificationMessage(status, order);
            notificationService.createAndSend(user, title, message, "ORDER_UPDATE", order.getId());
        }

        session.setAttribute("success", "Order #" + orderId + " status updated from " + oldStatus + " to " + status);
        return "redirect:/admin/orders";
    }

    private String getNotificationTitle(String status) {
        switch (status.toUpperCase()) {
            case "SHIPPED":
                return "Order Shipped! 🚚";
            case "DELIVERED":
                return "Order Delivered! 📦";
            case "CANCELLED":
                return "Order Cancelled ❌";
            case "PAID":
                return "Payment Confirmed ✅";
            default:
                return "Order Update";
        }
    }

    private String getNotificationMessage(String status, Order order) {
        String trackingInfo = order.getTrackingNumber() != null ? " (Tracking: " + order.getTrackingNumber() + ")" : "";
        switch (status.toUpperCase()) {
            case "SHIPPED":
                return "Your order #" + order.getId() + " has been shipped and is on its way!" + trackingInfo;
            case "DELIVERED":
                return "Your order #" + order.getId() + " has been delivered successfully. Enjoy!";
            case "CANCELLED":
                return "Your order #" + order.getId() + " has been cancelled. Contact support for details.";
            case "PAID":
                return "Payment for order #" + order.getId() + " has been confirmed. We're preparing your order!";
            default:
                return "Your order #" + order.getId() + " status has been updated to " + status + ".";
        }
    }
}
