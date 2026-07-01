# ClickNBuy 🛍️

A feature-rich, production-grade full-stack e-commerce web application built using the modern Spring Boot framework, dynamic Thymeleaf templating, and relational database management. 

---

## 🚀 Key Features

*   **User Management & Security:**
    *   Secure registration with password hashing via **BCrypt**.
    *   Authentication and role-based access control (Admin vs. Customer) using **Spring Security**.
    *   Double-layer OTP verification: **Twilio SMS OTP** and **Gmail SMTP Email OTP**.
    *   Secure password reset flow with verification tokens.
*   **Dynamic Product Catalog:**
    *   Pagination and sorting (by name, price, category).
    *   15+ Product categories with dynamic filter pills.
    *   Admin CRUD dashboard (Add, Edit, Delete products) with real-time field validation.
*   **Persistent Shopping Cart:**
    *   Real-time stock validation to prevent overselling.
    *   Quantities update dynamically with automatic stock adjustment.
*   **Checkout & Integrated Payments:**
    *   **Razorpay SDK** integration for processing online payments.
    *   HMAC-SHA256 signature verification for secure callback and webhook handling.
    *   Mock payment mode fallback for testing environments.
*   **AI-Powered Recommendation Engine:**
    *   Hybrid content-based + collaborative recommendation system.
    *   Scores calculated dynamically based on purchase history, cart items, search history, and average category spending.
    *   Cold-start fallback showing trending/popular items for new and guest users.
*   **Real-Time Notifications (WebSocket):**
    *   Implements **STOMP/SockJS** over WebSocket.
    *   Instant order status updates (e.g., Shipped, Delivered) pushed from the admin dashboard to the customer's browser.
    *   Nav bell badge with unread counters, ringing animations, and a slide-out drawer panel.
*   **Promotional Modules:**
    *   Countdown "Deal of the Day" exclusive offer popups (session-controlled).

---

## 🛠️ Tech Stack

### Backend
*   **Spring Boot 3.5.7** (Core Framework)
*   **Spring Security 6.x** (Authentication & Authorization)
*   **Spring Data JPA** (ORM / Hibernate)
*   **Spring WebSocket** (STOMP Protocol messaging)
*   **Spring Mail** (SMTP email delivery)
*   **MySQL 8.x** (Database)

### Frontend
*   **Thymeleaf 3.x** (Server-side HTML rendering)
*   **Bootstrap 5.3.2** (Responsive Styling & Layouts)
*   **SockJS & STOMP.js** (Real-time client connections)
*   **Vanilla JavaScript** (Interactivity & Notifications)

### External Services
*   **Razorpay SDK** (Payment processing)
*   **Twilio SDK** (SMS OTP notifications)
*   **Cloudinary SDK** (Cloud image hosting)

---

## 📁 System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     CLIENT LAYER                            │
│  Browser (Thymeleaf + Bootstrap + JS + SockJS/STOMP)        │
└───────────────────────────┬─────────────────────────────────┘
                            │ HTTP / WebSocket
┌───────────────────────────▼─────────────────────────────────┐
│                   CONTROLLER LAYER                          │
│  ViewController │ UserController │ AdminController          │
│  AdminOrderController │ NotificationController (REST)       │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│                    SERVICE LAYER                             │
│  UserService │ AdminService │ NotificationService            │
│  RecommendationService (AI Engine)                           │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│                 DATA ACCESS LAYER                            │
│  UserRepository │ ProductRepository │ CartItemRepository     │
│  OrderRepository │ OrderItemRepository │ NotificationRepo    │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│                   DATABASE LAYER                             │
│                    MySQL 8.x                                 │
└─────────────────────────────────────────────────────────────┘
```

---

## ⚙️ Quick Start

### Prerequisites
*   **Java 17+**
*   **MySQL 8.x** running on port 3306
*   **Maven** (or use the provided wrapper)

### Setup Steps
1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/Raju-AB/ClickNBuy.git
    cd ClickNBuy
    ```
2.  **Database Configuration:**
    Create a database named `clicknbuy` in MySQL:
    ```sql
    CREATE DATABASE clicknbuy;
    ```
3.  **Configure Environment Variables:**
    Update `src/main/resources/application.properties` or set environment variables:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/clicknbuy
    spring.datasource.username=YOUR_MYSQL_USERNAME
    spring.datasource.password=YOUR_MYSQL_PASSWORD

    # Gmail Config (Optional for OTP verification)
    spring.mail.username=your-email@gmail.com
    spring.mail.password=your-app-password

    # Twilio/Razorpay Configs (Optional, has mock fallbacks)
    twilio.sid=YOUR_TWILIO_SID
    twilio.auth.token=YOUR_TWILIO_TOKEN
    twilio.mobile=YOUR_TWILIO_NUMBER
    razorpay.id=YOUR_RAZORPAY_KEY
    razorpay.secret=YOUR_RAZORPAY_SECRET
    ```
4.  **Run the Application:**
    *   **Windows:**
        ```cmd
        .\mvnw.cmd spring-boot:run
        ```
    *   **macOS / Linux:**
        ```bash
        ./mvnw spring-boot:run
        ```
5.  **Access in Browser:**
    Open [http://localhost:8080](http://localhost:8080).