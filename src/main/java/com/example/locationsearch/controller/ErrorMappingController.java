package com.example.locationsearch.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller to handle fallback error routing by mapping the “/error” path.
 **/
@Controller
public class ErrorMappingController implements ErrorController {

    /**
     * Handles requests forwarded to “/error”.
     * <p>
     * Instead of showing a Whitelabel error page, this method redirects to the root ("/").
     * </p>
     **/
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        return "redirect:/";
    }

}
