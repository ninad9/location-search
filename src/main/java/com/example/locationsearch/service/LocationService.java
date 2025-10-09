package com.example.locationsearch.service;

import com.example.locationsearch.model.Location;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {
    private final Map<String, List<String>> cityMap = new HashMap<>();
    private Map<String, String> zipMap;

    @PostConstruct
    public void init() {
        cityMap.put("berlin", List.of("10115", "10117", "10119"));
        cityMap.put("munich", List.of("80331", "80333", "80335"));
        cityMap.put("cologne", List.of("50667", "50668"));
        cityMap.put("hamburg", List.of("20095", "20097", "20099"));

        zipMap = cityMap.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(zip -> Map.entry(zip, entry.getKey())))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public List<Location> findByCity(String query) {
        String finalQuery = "";
        return cityMap.getOrDefault(query.trim().toLowerCase(), List.of())
                .stream()
                .map(zip -> new Location(finalQuery.trim().toUpperCase(), zip))
                .toList();
    }

    public Optional<Location> findByZip(String query) {
        query = query.trim();
        if (zipMap.containsKey(query)) {
            String city = zipMap.get(query.trim());
            return city != null ? Optional.of(new Location(city.toUpperCase(), query.trim())) : Optional.empty();
        }
        return Optional.empty();

    }

}
