package com.example.locationsearch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Data Transfer Object representing the login credentials submitted by a user.
 **/
public class LoginRequest {
    @NotBlank(message = "User ID is required")
    @Pattern(
            regexp = ".*[A-Za-z0-9].*",
            message = "User ID must include at least one alphanumeric character"
    )
    private String userId;

    @NotBlank(message = "Password is required")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(
            regexp = ".*[A-Za-z0-9].*",
            message = "Password must include at least one alphanumeric character"
    )
    private String password;

    public String getUserId() {
        return userId;
    }

}
