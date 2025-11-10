package com.bytebasket;

import com.bytebasket.model.*;
import com.bytebasket.service.CategoryService;
import com.bytebasket.service.ProductService;
import com.bytebasket.service.RoleService;
import com.bytebasket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleService roleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // Initialize Roles
        initializeRoles();

        // Initialize Admin User (must be after roles)
        initializeAdminUser();

        // Initialize Categories
        initializeCategories();

        // Initialize Sample Products
        initializeProducts();

        System.out.println("✅ Data initialization completed!");
    }

    private void initializeRoles() {
        if (!roleService.existsByName("CUSTOMER")) {
            Role customerRole = new Role();
            customerRole.setName("CUSTOMER");
            customerRole.setDescription("Regular customer who can browse and purchase products");
            roleService.saveRole(customerRole);
            System.out.println("✅ CUSTOMER role created");
        }

        if (!roleService.existsByName("SELLER")) {
            Role sellerRole = new Role();
            sellerRole.setName("SELLER");
            sellerRole.setDescription("Seller who can list and manage their products");
            roleService.saveRole(sellerRole);
            System.out.println("✅ SELLER role created");
        }

        if (!roleService.existsByName("ADMIN")) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            adminRole.setDescription("Administrator with full system access");
            roleService.saveRole(adminRole);
            System.out.println("✅ ADMIN role created");
        }
    }

    private void initializeAdminUser() {
        if (!userService.getUserByUsername("admin").isPresent()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@bytebasket.com");
            admin.setPassword("admin123"); // Will be encrypted by UserService
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setPhone("+1 555 0000");
            admin.setAddress("ByteBasket HQ, Tech Street, Silicon Valley");

            try {
                userService.registerUser(admin, "ADMIN");
                System.out.println("✅ Admin user created");
                System.out.println("   Username: admin");
                System.out.println("   Password: admin123");
            } catch (Exception e) {
                System.out.println("⚠️ Admin user initialization error: " + e.getMessage());
            }
        } else {
            System.out.println("ℹ️ Admin user already exists");
        }
    }

    private void initializeCategories() {
        String[][] categories = {
                {"Electronics", "Electronic devices and gadgets"},
                {"Clothing", "Fashion and apparel"},
                {"Books", "Books and publications"},
                {"Home & Kitchen", "Home appliances and kitchenware"},
                {"Sports", "Sports equipment and accessories"},
                {"Toys", "Toys and games"},
                {"Beauty", "Beauty and personal care products"},
                {"Automotive", "Automotive parts and accessories"}
        };

        for (String[] categoryData : categories) {
            if (!categoryService.existsByName(categoryData[0])) {
                Category category = new Category();
                category.setName(categoryData[0]);
                category.setDescription(categoryData[1]);
                categoryService.saveCategory(category);
                System.out.println("✅ Category created: " + categoryData[0]);
            }
        }
    }

    private void initializeProducts() {
        // Check if products already exist
        if (!productService.getAllProducts().isEmpty()) {
            System.out.println("ℹ️ Products already exist, skipping product initialization");
            return;
        }

        System.out.println("📦 Creating sample products...");

        // Get categories
        Category electronics = categoryService.getCategoryByName("Electronics").orElse(null);
        Category clothing = categoryService.getCategoryByName("Clothing").orElse(null);
        Category books = categoryService.getCategoryByName("Books").orElse(null);
        Category homeKitchen = categoryService.getCategoryByName("Home & Kitchen").orElse(null);
        Category sports = categoryService.getCategoryByName("Sports").orElse(null);

        // Electronics Products
        if (electronics != null) {
            createSampleProduct(
                    "Wireless Bluetooth Headphones",
                    "Premium noise-canceling headphones with 30-hour battery life. Crystal clear sound quality with deep bass.",
                    new BigDecimal("79.99"),
                    50,
                    "Sony",
                    electronics
            );

            createSampleProduct(
                    "4K Smart TV 55 inch",
                    "Ultra HD Smart TV with HDR and built-in streaming apps. Experience cinema-quality picture at home.",
                    new BigDecimal("499.99"),
                    20,
                    "Samsung",
                    electronics
            );

            createSampleProduct(
                    "Wireless Gaming Mouse",
                    "High-precision gaming mouse with customizable RGB lighting and 16000 DPI sensor.",
                    new BigDecimal("59.99"),
                    75,
                    "Logitech",
                    electronics
            );
        }

        // Clothing Products
        if (clothing != null) {
            createSampleProduct(
                    "Cotton T-Shirt",
                    "Comfortable 100% cotton t-shirt available in multiple colors. Perfect for everyday wear.",
                    new BigDecimal("19.99"),
                    100,
                    "H&M",
                    clothing
            );

            createSampleProduct(
                    "Denim Jeans",
                    "Classic fit denim jeans with stretch comfort. Durable and stylish.",
                    new BigDecimal("49.99"),
                    75,
                    "Levi's",
                    clothing
            );

            createSampleProduct(
                    "Winter Jacket",
                    "Warm waterproof winter jacket with hood. Perfect for cold weather.",
                    new BigDecimal("89.99"),
                    40,
                    "North Face",
                    clothing
            );
        }

        // Books
        if (books != null) {
            createSampleProduct(
                    "The Great Gatsby",
                    "Classic American novel by F. Scott Fitzgerald. A timeless story of love and tragedy.",
                    new BigDecimal("12.99"),
                    200,
                    "Penguin Classics",
                    books
            );

            createSampleProduct(
                    "Clean Code",
                    "A handbook of agile software craftsmanship by Robert C. Martin. Essential reading for developers.",
                    new BigDecimal("39.99"),
                    150,
                    "Prentice Hall",
                    books
            );
        }

        // Home & Kitchen
        if (homeKitchen != null) {
            createSampleProduct(
                    "Stainless Steel Cookware Set",
                    "Professional 10-piece cookware set. Dishwasher safe and oven safe up to 500°F.",
                    new BigDecimal("149.99"),
                    30,
                    "Cuisinart",
                    homeKitchen
            );

            createSampleProduct(
                    "Coffee Maker",
                    "12-cup programmable coffee maker with auto-brew timer. Start your day right!",
                    new BigDecimal("69.99"),
                    45,
                    "Mr. Coffee",
                    homeKitchen
            );
        }

        // Sports
        if (sports != null) {
            createSampleProduct(
                    "Yoga Mat",
                    "Extra thick yoga mat with carrying strap. Non-slip surface for all types of yoga.",
                    new BigDecimal("29.99"),
                    80,
                    "Gaiam",
                    sports
            );

            createSampleProduct(
                    "Dumbbell Set",
                    "Adjustable dumbbell set (10-50 lbs). Perfect for home workouts.",
                    new BigDecimal("199.99"),
                    25,
                    "Bowflex",
                    sports
            );
        }

        System.out.println("✅ Sample products created successfully!");
    }

    private void createSampleProduct(String name, String description, BigDecimal price,
                                     int stock, String brand, Category category) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStockQuantity(stock);
        product.setBrand(brand);
        product.setCategory(category);
        product.setActive(true);
        // Note: seller is null for sample products - will be added when sellers create products

        productService.saveProduct(product);
        System.out.println("   📦 " + name + " - $" + price);
    }
}