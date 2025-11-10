package com.bytebasket.dto;

import lombok.Data;

@Data
public class RegistrationDto {

    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String userType; // "CUSTOMER" or "SELLER"
}