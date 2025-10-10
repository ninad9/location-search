package com.example.locationsearch.service;

import com.example.locationsearch.model.Location;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@link ILocationService}.
 * <p>
 * Keeps location data in memory.
 * Provides implementation for location search by ZIP and by City
 * </p>
 */
@Service
public class LocationService implements ILocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    private final Map<String, List<String>> cityMap = new HashMap<>();
    private Map<String, String> zipMap;

    /**
     * Initializes the in-memory maps after bean construction.
     * Populates cityMap with fixed data and builds zipMap for reverse lookup.
     */
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

        logger.info("Location data initialized with {} cities and {} ZIP mappings",
                cityMap.size(), zipMap.size());
    }

    /**
     * Finds a list of {@link Location} objects whose city matches the supplied query.
     *
     * @param query the city name
     * @return list of Location objects for each ZIP in that city, or empty list
     */
    @Override
    public List<Location> findByCity(String query) {
        logger.debug("Find location for City {}", query);
        return cityMap.getOrDefault(query.toLowerCase(), List.of())
                .stream()
                .map(zip -> new Location(query.toUpperCase(), zip))
                .toList();
    }

    /**
     * Finds the {@link Location} associated with the given ZIP code.
     *
     * @param query the ZIP code
     * @return Optional containing Location if ZIP found, or empty Optional
     */
    @Override
    public Optional<Location> findByZip(String query) {
        logger.debug("Find location for ZIP {}", query);
        if (zipMap.containsKey(query)) {
            String city = zipMap.get(query);
            return city != null ? Optional.of(new Location(city.toUpperCase(), query)) : Optional.empty();
        }
        logger.debug("No data  found for ZIP {}", query);
        return Optional.empty();
    }

}
