package com.example.locationsearch.controller;

import com.example.locationsearch.dto.LoginRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controller for handling user login and logout request.
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * Serves the login page.
     **/
    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    /**
     * Processes login requests.
     * <p>
     * @param loginRequest the login credentials
     * @param session the HTTP session used to store login state
     * @return a {@link ResponseEntity} with status 200 and “Login successful” when login succeeds,
     * or status 400 with error message if credentials are invalid
     * </p>
     **/
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest, HttpSession session) {
        if (loginRequest.getUserId() != null && !loginRequest.getUserId().isBlank()) {
            session.setAttribute("loggedInUser", loginRequest.getUserId());
            logger.info("User logged in: {}", loginRequest.getUserId());
            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.badRequest().body("Invalid credentials");
    }

    /**
     * Logs out the current user.
     * <p>
     * This invalidates the HTTP session.
     * After logout, the user is redirected to the root page.
     * </p>
     **/
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        logger.info("Logout successful");
        return "redirect:/";
    }

}
