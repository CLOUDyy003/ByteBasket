package com.bytebasket.controller;

import com.bytebasket.model.User;
import com.bytebasket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        // Get logged-in username
        String username = authentication.getName();

        // Get user details
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);

        // Redirect based on role
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SELLER"))) {
            return "redirect:/seller/dashboard";
        } else {
            return "redirect:/customer/dashboard";
        }
    }

    // Customer Dashboard
    @GetMapping("/customer/dashboard")
    public String customerDashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElseThrow();
        model.addAttribute("user", user);
        return "dashboard/customer";
    }

    // Seller Dashboard
    @GetMapping("/seller/dashboard")
    public String sellerDashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElseThrow();
        model.addAttribute("user", user);
        return "dashboard/seller";
    }

    // Admin Dashboard
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElseThrow();
        model.addAttribute("user", user);
        return "dashboard/admin";
    }
}