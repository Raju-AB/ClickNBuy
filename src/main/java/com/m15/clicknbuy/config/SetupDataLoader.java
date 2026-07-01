package com.m15.clicknbuy.config;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.m15.clicknbuy.entity.User;
import com.m15.clicknbuy.repository.UserRepository;
import com.m15.clicknbuy.entity.Product;
import com.m15.clicknbuy.repository.ProductRepository;

@Component
public class SetupDataLoader implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User admin = userRepository.findByEmail("bailpattar.raju1@gmail.com").orElse(new User());
        admin.setName("Admin Raju");
        admin.setEmail("bailpattar.raju1@gmail.com");
        admin.setPassword(passwordEncoder.encode("Raju@12345"));
        if (admin.getMobile() == null) {
            admin.setMobile(System.currentTimeMillis() % 10000000000L); // Ensure unique mobile if new
        }
        admin.setGender("Male");
        admin.setVerified(true);
        admin.setRole("ROLE_ADMIN"); 
        userRepository.save(admin);

        if (!productRepository.existsByName("Wireless Headphones")) {
            Product p1 = new Product(null, "Wireless Headphones", 199.99, 50, "Premium over-ear wireless headphones.", "/uploads/headphones.png", "Electronics", LocalDateTime.now());
            productRepository.save(p1);
        }
        if (!productRepository.existsByName("Smartwatch")) {
            Product p2 = new Product(null, "Smartwatch", 299.99, 30, "Digital smartwatch with metal band.", "/uploads/smartwatch.png", "Electronics", LocalDateTime.now());
            productRepository.save(p2);
        }
        if (!productRepository.existsByName("Modern Sneakers")) {
            Product p3 = new Product(null, "Modern Sneakers", 89.99, 100, "Stylish and comfortable sneakers.", "/uploads/sneakers.png", "Fashion", LocalDateTime.now());
            productRepository.save(p3);
        }
        if (!productRepository.existsByName("Gaming Laptop")) {
            Product p4 = new Product(null, "Gaming Laptop", 1299.99, 15, "High-performance gaming laptop with RGB keyboard.", "/uploads/gaming_laptop.png", "Computers", LocalDateTime.now());
            productRepository.save(p4);
        }
        if (!productRepository.existsByName("Wireless Mouse")) {
            Product p5 = new Product(null, "Wireless Mouse", 49.99, 80, "Ergonomic wireless mouse for daily use.", "/uploads/wireless_mouse.png", "Accessories", LocalDateTime.now());
            productRepository.save(p5);
        }
        if (!productRepository.existsByName("Mechanical Keyboard")) {
            Product p6 = new Product(null, "Mechanical Keyboard", 109.99, 45, "Premium mechanical gaming keyboard.", "/uploads/mechanical_keyboard.png", "Accessories", LocalDateTime.now());
            productRepository.save(p6);
        }
        if (!productRepository.existsByName("Gaming Monitor")) {
            Product p7 = new Product(null, "Gaming Monitor", 499.99, 20, "Curved gaming monitor with high refresh rate.", "/uploads/gaming_monitor.png", "Electronics", LocalDateTime.now());
            productRepository.save(p7);
        }
        if (!productRepository.existsByName("Smartphone")) {
            Product p8 = new Product(null, "Smartphone", 899.99, 60, "Premium smartphone with amazing camera.", "/uploads/smartphone.png", "Electronics", LocalDateTime.now());
            productRepository.save(p8);
        }
        if (!productRepository.existsByName("Bluetooth Speaker")) {
            Product p9 = new Product(null, "Bluetooth Speaker", 59.99, 120, "Portable waterproof bluetooth speaker.", "/uploads/bluetooth_speaker.png", "Audio", LocalDateTime.now());
            productRepository.save(p9);
        }
        if (!productRepository.existsByName("Running Shoes")) {
            Product p10 = new Product(null, "Running Shoes", 129.99, 85, "Athletic running shoes with breathable mesh.", "/uploads/running_shoes.png", "Fashion", LocalDateTime.now());
            productRepository.save(p10);
        }
        if (!productRepository.existsByName("Digital Camera")) {
            Product p11 = new Product(null, "Digital Camera", 1500.00, 10, "High-end mirrorless digital camera with large lens.", "/uploads/digital_camera.png", "Electronics", LocalDateTime.now());
            productRepository.save(p11);
        }
        if (!productRepository.existsByName("Coffee Maker")) {
            Product p12 = new Product(null, "Coffee Maker", 250.00, 40, "Sleek, modern espresso coffee maker machine.", "/uploads/coffee_maker.png", "Home", LocalDateTime.now());
            productRepository.save(p12);
        }
        if (!productRepository.existsByName("Desk Lamp")) {
            Product p13 = new Product(null, "Desk Lamp", 35.00, 200, "Minimalist LED desk lamp with adjustable arm.", "/uploads/desk_lamp.png", "Home", LocalDateTime.now());
            productRepository.save(p13);
        }
        if (!productRepository.existsByName("Backpack")) {
            Product p14 = new Product(null, "Backpack", 75.00, 150, "Stylish, durable travel backpack.", "/uploads/backpack.png", "Fashion", LocalDateTime.now());
            productRepository.save(p14);
        }
        if (!productRepository.existsByName("Sunglasses")) {
            Product p15 = new Product(null, "Sunglasses", 150.00, 60, "Stylish, premium sunglasses.", "/uploads/sunglasses.png", "Fashion", LocalDateTime.now());
            productRepository.save(p15);
        }
        if (!productRepository.existsByName("4K Action Camera")) {
            Product p16 = new Product(null, "4K Action Camera", 299.99, 45, "Waterproof 4K action camera with mounting kit.", "/uploads/action_camera.png", "Electronics", LocalDateTime.now());
            productRepository.save(p16);
        }
        if (!productRepository.existsByName("Electric Toothbrush")) {
            Product p17 = new Product(null, "Electric Toothbrush", 89.99, 100, "Smart electric toothbrush with multiple modes.", "/uploads/electric_toothbrush.png", "Home", LocalDateTime.now());
            productRepository.save(p17);
        }
        if (!productRepository.existsByName("Tablet Computer")) {
            Product p18 = new Product(null, "Tablet Computer", 499.99, 30, "10-inch tablet with high-resolution display.", "/uploads/tablet.png", "Electronics", LocalDateTime.now());
            productRepository.save(p18);
        }
        if (!productRepository.existsByName("Fitness Tracker")) {
            Product p19 = new Product(null, "Fitness Tracker", 49.99, 150, "Fitness tracker with heart rate monitor.", "/uploads/fitness_tracker.png", "Electronics", LocalDateTime.now());
            productRepository.save(p19);
        }
        if (!productRepository.existsByName("Yoga Mat")) {
            Product p20 = new Product(null, "Yoga Mat", 29.99, 200, "Eco-friendly, non-slip yoga mat.", "/uploads/yoga_mat.png", "Sports", LocalDateTime.now());
            productRepository.save(p20);
        }
        if (!productRepository.existsByName("Novel - The Great Gatsby")) {
            Product p21 = new Product(null, "Novel - The Great Gatsby", 15.99, 100, "Classic literature.", "/uploads/book.png", "Books", LocalDateTime.now());
            productRepository.save(p21);
        }
        if (!productRepository.existsByName("Lego Star Wars")) {
            Product p22 = new Product(null, "Lego Star Wars", 59.99, 40, "Building blocks toy set.", "/uploads/toys.png", "Toys", LocalDateTime.now());
            productRepository.save(p22);
        }
        if (!productRepository.existsByName("Car Wash Kit")) {
            Product p23 = new Product(null, "Car Wash Kit", 35.00, 60, "Complete car washing and detailing kit.", "/uploads/automotive.png", "Automotive", LocalDateTime.now());
            productRepository.save(p23);
        }
        if (!productRepository.existsByName("Organic Coffee Beans")) {
            Product p24 = new Product(null, "Organic Coffee Beans", 19.99, 120, "Premium organic roasted coffee beans.", "/uploads/groceries.png", "Groceries", LocalDateTime.now());
            productRepository.save(p24);
        }
        if (!productRepository.existsByName("Skincare Set")) {
            Product p25 = new Product(null, "Skincare Set", 85.00, 50, "Complete morning and night skincare routine.", "/uploads/health_beauty.png", "Health & Beauty", LocalDateTime.now());
            productRepository.save(p25);
        }
        if (!productRepository.existsByName("Office Chair")) {
            Product p26 = new Product(null, "Office Chair", 150.00, 30, "Ergonomic mesh office chair.", "/uploads/furniture.png", "Furniture", LocalDateTime.now());
            productRepository.save(p26);
        }
        if (!productRepository.existsByName("Gardening Toolkit")) {
            Product p27 = new Product(null, "Gardening Toolkit", 45.00, 80, "Essential tools for home gardening.", "/uploads/gardening.png", "Gardening", LocalDateTime.now());
            productRepository.save(p27);
        }
        if (!productRepository.existsByName("Notebook Set")) {
            Product p28 = new Product(null, "Notebook Set", 12.50, 150, "Set of 5 ruled notebooks.", "/uploads/office_supplies.png", "Office Supplies", LocalDateTime.now());
            productRepository.save(p28);
        }
        if (!productRepository.existsByName("Wireless Earbuds")) {
            Product p29 = new Product(null, "Wireless Earbuds", 89.99, 200, "Compact noise-cancelling wireless earbuds.", "/uploads/earbuds.png", "Audio", LocalDateTime.now());
            productRepository.save(p29);
        }
        if (!productRepository.existsByName("Dumbbell Set")) {
            Product p30 = new Product(null, "Dumbbell Set", 120.00, 30, "Adjustable dumbbell set for home gym.", "/uploads/dumbbells.png", "Sports", LocalDateTime.now());
            productRepository.save(p30);
        }
        if (!productRepository.existsByName("Board Game Collection")) {
            Product p31 = new Product(null, "Board Game Collection", 45.00, 50, "Family favorite board games.", "/uploads/boardgame.png", "Toys", LocalDateTime.now());
            productRepository.save(p31);
        }
        if (!productRepository.existsByName("Car Dash Cam")) {
            Product p32 = new Product(null, "Car Dash Cam", 110.00, 45, "1080p HD dash camera with night vision.", "/uploads/dashcam.png", "Automotive", LocalDateTime.now());
            productRepository.save(p32);
        }
        if (!productRepository.existsByName("Premium Tea Assortment")) {
            Product p33 = new Product(null, "Premium Tea Assortment", 25.00, 100, "Selection of fine green and black teas.", "/uploads/tea.png", "Groceries", LocalDateTime.now());
            productRepository.save(p33);
        }
        if (!productRepository.existsByName("Electric Shaver")) {
            Product p34 = new Product(null, "Electric Shaver", 65.00, 80, "Rechargeable electric shaver for men.", "/uploads/shaver.png", "Health & Beauty", LocalDateTime.now());
            productRepository.save(p34);
        }
        if (!productRepository.existsByName("Bookshelf")) {
            Product p35 = new Product(null, "Bookshelf", 180.00, 20, "Wooden 5-tier bookshelf.", "/uploads/bookshelf.png", "Furniture", LocalDateTime.now());
            productRepository.save(p35);
        }
        if (!productRepository.existsByName("Plant Pots Set")) {
            Product p36 = new Product(null, "Plant Pots Set", 30.00, 60, "Set of 3 ceramic plant pots.", "/uploads/plantpots.png", "Gardening", LocalDateTime.now());
            productRepository.save(p36);
        }
        if (!productRepository.existsByName("Desk Organizer")) {
            Product p37 = new Product(null, "Desk Organizer", 18.50, 150, "Multi-compartment desk organizer.", "/uploads/organizer.png", "Office Supplies", LocalDateTime.now());
            productRepository.save(p37);
        }
        if (!productRepository.existsByName("Science Fiction Novel")) {
            Product p38 = new Product(null, "Science Fiction Novel", 14.99, 120, "Bestselling sci-fi adventure book.", "/uploads/scifi.png", "Books", LocalDateTime.now());
            productRepository.save(p38);
        }
        if (!productRepository.existsByName("Winter Jacket")) {
            Product p39 = new Product(null, "Winter Jacket", 140.00, 50, "Warm insulated winter jacket.", "/uploads/jacket.png", "Fashion", LocalDateTime.now());
            productRepository.save(p39);
        }
        if (!productRepository.existsByName("VR Headset")) {
            Product p40 = new Product(null, "VR Headset", 399.99, 25, "Virtual reality gaming headset.", "/uploads/vr.png", "Electronics", LocalDateTime.now());
            productRepository.save(p40);
        }
        if (!productRepository.existsByName("4K Smart TV")) {
            Product p41 = new Product(null, "4K Smart TV", 599.99, 15, "55-inch 4K Ultra HD Smart LED TV.", "/uploads/tv.png", "Electronics", LocalDateTime.now());
            productRepository.save(p41);
        }
        if (!productRepository.existsByName("Denim Jeans")) {
            Product p42 = new Product(null, "Denim Jeans", 45.00, 80, "Classic fit blue denim jeans.", "/uploads/jeans.png", "Fashion", LocalDateTime.now());
            productRepository.save(p42);
        }
        if (!productRepository.existsByName("Desktop Computer")) {
            Product p43 = new Product(null, "Desktop Computer", 899.99, 10, "Powerful desktop PC for work and gaming.", "/uploads/desktop.png", "Computers", LocalDateTime.now());
            productRepository.save(p43);
        }
        if (!productRepository.existsByName("Webcam 1080p")) {
            Product p44 = new Product(null, "Webcam 1080p", 35.99, 60, "HD webcam with built-in microphone.", "/uploads/webcam.png", "Accessories", LocalDateTime.now());
            productRepository.save(p44);
        }
        if (!productRepository.existsByName("Soundbar System")) {
            Product p45 = new Product(null, "Soundbar System", 150.00, 25, "2.1 channel soundbar with subwoofer.", "/uploads/soundbar.png", "Audio", LocalDateTime.now());
            productRepository.save(p45);
        }
        if (!productRepository.existsByName("Air Purifier")) {
            Product p46 = new Product(null, "Air Purifier", 120.00, 40, "HEPA filter air purifier for large rooms.", "/uploads/air_purifier.png", "Home", LocalDateTime.now());
            productRepository.save(p46);
        }
        if (!productRepository.existsByName("Tennis Racket")) {
            Product p47 = new Product(null, "Tennis Racket", 85.00, 30, "Professional lightweight tennis racket.", "/uploads/tennis.png", "Sports", LocalDateTime.now());
            productRepository.save(p47);
        }
        if (!productRepository.existsByName("Cookbook")) {
            Product p48 = new Product(null, "Cookbook", 25.00, 100, "Healthy recipes for everyday cooking.", "/uploads/cookbook.png", "Books", LocalDateTime.now());
            productRepository.save(p48);
        }
        if (!productRepository.existsByName("Puzzle 1000 Pieces")) {
            Product p49 = new Product(null, "Puzzle 1000 Pieces", 15.00, 80, "Beautiful landscape jigsaw puzzle.", "/uploads/puzzle.png", "Toys", LocalDateTime.now());
            productRepository.save(p49);
        }
        if (!productRepository.existsByName("Car Phone Mount")) {
            Product p50 = new Product(null, "Car Phone Mount", 12.99, 150, "Universal dashboard magnetic phone mount.", "/uploads/phonemount.png", "Automotive", LocalDateTime.now());
            productRepository.save(p50);
        }
        if (!productRepository.existsByName("Olive Oil Extravirgin")) {
            Product p51 = new Product(null, "Olive Oil Extravirgin", 18.00, 60, "Premium cold-pressed olive oil.", "/uploads/oliveoil.png", "Groceries", LocalDateTime.now());
            productRepository.save(p51);
        }
        if (!productRepository.existsByName("Hair Dryer")) {
            Product p52 = new Product(null, "Hair Dryer", 45.00, 40, "Ionic hair dryer with multiple heat settings.", "/uploads/hairdryer.png", "Health & Beauty", LocalDateTime.now());
            productRepository.save(p52);
        }
        if (!productRepository.existsByName("Coffee Table")) {
            Product p53 = new Product(null, "Coffee Table", 110.00, 20, "Modern wooden living room coffee table.", "/uploads/coffeetable.png", "Furniture", LocalDateTime.now());
            productRepository.save(p53);
        }
        if (!productRepository.existsByName("Watering Can")) {
            Product p54 = new Product(null, "Watering Can", 15.00, 100, "Metal watering can for indoor/outdoor plants.", "/uploads/wateringcan.png", "Gardening", LocalDateTime.now());
            productRepository.save(p54);
        }
        if (!productRepository.existsByName("Fountain Pen")) {
            Product p55 = new Product(null, "Fountain Pen", 35.00, 50, "Elegant refillable fountain pen.", "/uploads/pen.png", "Office Supplies", LocalDateTime.now());
            productRepository.save(p55);
        }
    }
}
