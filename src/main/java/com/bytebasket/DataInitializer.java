package com.bytebasket;

import com.bytebasket.model.*;
import com.bytebasket.service.CategoryService;
import com.bytebasket.service.ProductService;
import com.bytebasket.service.RoleService;
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

    @Override
    public void run(String... args) throws Exception {
        // Initialize Roles
        initializeRoles();

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
            System.out.println("⚠️ Products already exist, skipping product initialization");
            return;
        }

        // For now, we'll create a few sample products without a seller
        // We'll add sellers later when we implement user management

        Category electronics = categoryService.getCategoryByName("Electronics").orElse(null);
        Category clothing = categoryService.getCategoryByName("Clothing").orElse(null);
        Category books = categoryService.getCategoryByName("Books").orElse(null);

        if (electronics != null) {
            createSampleProduct(
                    "Wireless Bluetooth Headphones",
                    "Premium noise-canceling headphones with 30-hour battery life",
                    new BigDecimal("79.99"),
                    50,
                    "Sony",
                    electronics
            );

            createSampleProduct(
                    "4K Smart TV 55 inch",
                    "Ultra HD Smart TV with HDR and built-in streaming apps",
                    new BigDecimal("499.99"),
                    20,
                    "Samsung",
                    electronics
            );
        }

        if (clothing != null) {
            createSampleProduct(
                    "Cotton T-Shirt",
                    "Comfortable 100% cotton t-shirt available in multiple colors",
                    new BigDecimal("19.99"),
                    100,
                    "Generic",
                    clothing
            );

            createSampleProduct(
                    "Denim Jeans",
                    "Classic fit denim jeans with stretch comfort",
                    new BigDecimal("49.99"),
                    75,
                    "Levi's",
                    clothing
            );
        }

        if (books != null) {
            createSampleProduct(
                    "The Great Gatsby",
                    "Classic American novel by F. Scott Fitzgerald",
                    new BigDecimal("12.99"),
                    200,
                    "Penguin Classics",
                    books
            );
        }

        System.out.println("✅ Sample products created");
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
        // Note: seller will be null for now, we'll add it when we create users

        productService.saveProduct(product);
        System.out.println("   📦 Product created: " + name);
    }
}