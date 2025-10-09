package com.example.locationsearch.controller;

import com.example.locationsearch.model.Location;
import com.example.locationsearch.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class SearchController {

    @Autowired
    LocationService locationService;

    @GetMapping("/search")
    public String searchPage() {
        return "search";
    }

    @GetMapping("/result")
    public String handleSearch(@RequestParam String query, Model model) {
        if (query == null || query.isBlank()) {
            model.addAttribute("error", "Enter valid input");
            return "search";
        }
        Optional<Location> searchByZip = locationService.findByZip(query);
        if (searchByZip.isPresent()) {
            model.addAttribute("searchByZip", searchByZip.get());
            return "search";
        }
        List<Location> searchByCity = locationService.findByCity(query);
        if (!searchByCity.isEmpty()) {
            model.addAttribute("searchByCity", searchByCity);
            model.addAttribute("cityName", query);
            return "search";
        }
        model.addAttribute("error", "No location found for input: " + query);
        return "search";
    }
}
