package com.example.locationsearch.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object representing the login credentials submitted by a user.
 **/
public class LoginRequest {
    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Password is required")
    private String password;

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
