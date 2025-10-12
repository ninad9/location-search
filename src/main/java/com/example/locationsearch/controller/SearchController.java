package com.example.locationsearch.controller;

import com.example.locationsearch.model.Location;
import com.example.locationsearch.service.SearchService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

/**
 * Controller that handles search operations for locations by ZIP or city name.
 * - Provides endpoints for showing the search page and processing search results.
 * - Access to search and result endpoints is guarded by checking the "loggedInUser" session attribute.
 */
@Controller
//@Validated
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    SearchService searchService;

    /**
     * This method handles GET requests to the /search endpoint and returns the search view.
     *
     * @param session the current {@link HttpSession} used to validate user authentication
     * @return the name of the view template to render:
     * - "search" if the user is authenticated
     * - "redirect:/login" if the user is not authenticated
     */
    @GetMapping("/search")
    public String searchPage(HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            logger.info("Unauthenticated access attempted — redirecting to /login");
            return "redirect:/login";
        }
        return "search";
    }

    /**
     * This method accepts a user-provided input string via query parameter, which may represent
     * either a city name or a ZIP/postal code.
     *
     * @param input   the search term provided by the user; must be non-empty and non-blank
     * @param model   the Spring {@link Model} used to pass attributes to the view
     * @param session the current {@link HttpSession} used to verify authentication
     * @return the name of the view template to render, either /search or a redirect to /login
     * @throws IllegalArgumentException if input validation fails
     */
    @GetMapping("/result")
    public String search(
            @RequestParam String input,
            Model model,
            HttpSession session
    ) {
        Object user = session.getAttribute("loggedInUser");
        if (user == null) {
            logger.info("Unauthenticated access to search — redirecting to login");
            return "redirect:/login";
        }
        if (input == null || input.isBlank()) {
            model.addAttribute("error", "Input is required");
            return "search";
        }

        String normalized = input.strip();
        logger.info("Received search request for input '{}'", normalized);

        Location location = searchService.findLocation(normalized);
        if (Objects.nonNull(location)) {
            logger.info("Found location for input '{}': {}", normalized, location);
            model.addAttribute("result", location);
        } else {
            logger.info("No location found for input '{}'", normalized);
            model.addAttribute("error", "No location found for input: " + normalized);
        }
        return "search";
    }
}
