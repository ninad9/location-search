package com.example.locationsearch.controller;

import com.example.locationsearch.model.Location;
import com.example.locationsearch.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        Optional<Location> result = locationService.findByZipOrCity(query);
        if (result.isPresent()) {
            model.addAttribute("location", result.get());
        } else {
            model.addAttribute("error", "No location found for input: " + query);
        }
        return "search";
    }
}
