package com.example.locationsearch.service;

import com.example.locationsearch.model.Location;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LocationService {
    private final Map<String, Location> zipMap = new HashMap<>();
    private final Map<String, Location> cityMap = new HashMap<>();


    public Optional<Location> findByZipOrCity(String query) {
        addLocations();

        query = query.trim();
        if (zipMap.containsKey(query)) {
            return Optional.of(zipMap.get(query));
        }
        if (cityMap.containsKey(query.toLowerCase())) {
            return Optional.of(cityMap.get(query.toLowerCase()));
        }
        return Optional.empty();

    }

    private void addLocations() {
        zipMap.put("10115", new Location("10115", "Berlin"));
        zipMap.put("80331", new Location("80331", "Munich"));
        zipMap.put("50667", new Location("50667", "Cologne"));

        cityMap.put("berlin", new Location("10115", "Berlin"));
        cityMap.put("munich", new Location("80331", "Munich"));
        cityMap.put("cologne", new Location("50667", "Cologne"));
    }
}
