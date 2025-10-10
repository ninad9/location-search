package com.example.locationsearch.controller;

import com.example.locationsearch.model.Location;
import com.example.locationsearch.service.ILocationService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

/**
 * Controller that handles search operations for locations by ZIP or city name.
 * <p>
 * Provides endpoints for showing the search page and processing search results.
 * Access to search and result endpoints is guarded by checking the "loggedInUser" session attribute.
 * </p>
 */
@Controller
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    ILocationService locationService;

    @GetMapping("/search")
    public String searchPage(HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            logger.info("Unauthenticated access attempted — redirecting to /login");
            return "redirect:/login";
        }
        return "search";
    }

    /**
     * Handles the search request for a ZIP or city name.
     * <p>
     * Validates session and input, performs lookup by ZIP first, then by city,
     * and populates the model accordingly to show results or error message.
     * </p>
     *
     * @param query   the ZIP or city name the user entered e.g. Berlin or 10115
     * @param model   the Spring MVC model to populate attributes
     * @param session the HTTP session to check for login status
     * @return view name “search” or redirect to login
     */
    @GetMapping("/result")
    public String handleSearch(@RequestParam String query, Model model, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            logger.info("Unauthenticated access attempted — redirecting to /login");
            return "redirect:/login";
        }
        if (query == null || query.isBlank()) {
            logger.warn("Entered blank or null query");
            model.addAttribute("error", "Enter valid input");
            return "search";
        }
        Optional<Location> searchByZip = locationService.findByZip(query.strip());
        if (searchByZip.isPresent()) {
            logger.info("Entered ZIP: {}, Location found: {}", query.strip(), searchByZip.get());
            model.addAttribute("searchByZip", searchByZip.get());
            return "search";
        }
        List<Location> searchByCity = locationService.findByCity(query.strip());
        if (!searchByCity.isEmpty()) {
            logger.info("Entered City: {}, Location found: {}", query.strip(), searchByCity);
            model.addAttribute("searchByCity", searchByCity);
            model.addAttribute("cityName", query);
            return "search";
        }
        logger.info("Found no search results for query '{}'", query);
        model.addAttribute("error", "No location found for input: " + query);
        return "search";
    }
}
