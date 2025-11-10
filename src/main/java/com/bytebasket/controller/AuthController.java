package com.bytebasket.controller;

import com.bytebasket.dto.RegistrationDto;
import com.bytebasket.model.User;
import com.bytebasket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Show registration page
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegistrationDto());
        return "auth/register";
    }

    // Handle registration form submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") RegistrationDto registrationDto,
                               RedirectAttributes redirectAttributes) {
        try {
            // Validate passwords match
            if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
                redirectAttributes.addFlashAttribute("error", "Passwords do not match!");
                return "redirect:/register";
            }

            // Validate password length
            if (registrationDto.getPassword().length() < 6) {
                redirectAttributes.addFlashAttribute("error", "Password must be at least 6 characters!");
                return "redirect:/register";
            }

            // Create User entity from DTO
            User user = new User();
            user.setUsername(registrationDto.getUsername());
            user.setEmail(registrationDto.getEmail());
            user.setPassword(registrationDto.getPassword());
            user.setFirstName(registrationDto.getFirstName());
            user.setLastName(registrationDto.getLastName());
            user.setPhone(registrationDto.getPhone());
            user.setAddress(registrationDto.getAddress());

            // Determine role based on user type
            String roleName = registrationDto.getUserType();
            if (roleName == null || roleName.isEmpty()) {
                roleName = "CUSTOMER"; // Default to customer
            }

            // Register user
            userService.registerUser(user, roleName);

            redirectAttributes.addFlashAttribute("success",
                    "Registration successful! Please login.");
            return "redirect:/login";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    // Show login page
    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }
}