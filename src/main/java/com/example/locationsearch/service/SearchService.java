package com.example.locationsearch.service;

import com.example.locationsearch.model.Location;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class responsible for location lookup based on city names or ZIP/postal codes.
 * This class maintains two in-memory maps:
 *  a. cityMap — keyed by city name
 *  b. zipMap — keyed by ZIP code
 * It provides methods to initialize location data and perform lookup operations.
 */
@Service
public class SearchService {
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    private final Map<String, Location> cityMap = new HashMap<>();
    private final Map<String, Location> zipMap = new HashMap<>();

    /**
     * Initializes the in-memory maps after bean construction and creates two in-memory maps:
     *  a. cityMap — keyed by city name
     *  b. zipMap — keyed by ZIP code
     */
    @PostConstruct
    public void init() {

        List<Location> locations = loadLocationData();
        for (Location location : locations) {
            cityMap.put(location.getCity(), location);
            for (String zip : location.getZipCodes()) {
                zipMap.put(zip, new Location(location.getCity(), location.getState(),
                        location.getCountry()));
            }
        }
        logger.debug("Location data initialized with {} cities and {} ZIP mappings",
                cityMap.size(), zipMap.size());
    }

    /**
     * Loads a predefined list of {@link Location} objects representing major cities
     * and their associated ZIP codes, regions, and countries.
     */
    private List<Location> loadLocationData() {
        List<Location> locations = new ArrayList<>();
        locations.add(new Location(
                "New York",
                Arrays.asList("10001", "10002", "10003", "10004", "10005"),
                "New York",
                "USA"
        ));

        locations.add(new Location(
                "Paris",
                Arrays.asList("75001", "75002", "75003", "75004", "75005"),
                "Île-de-France",
                "France"
        ));

        locations.add(new Location(
                "Berlin",
                Arrays.asList("10115", "10117", "10119", "10178", "10179"),
                "Berlin",
                "Germany"
        ));

        locations.add(new Location(
                "Rome",
                Arrays.asList("00118", "00119", "00120", "00121", "00122"),
                "Lazio",
                "Italy"
        ));

        locations.add(new Location(
                "Madrid",
                Arrays.asList("28001", "28002", "28003", "28004", "28005"),
                "Community of Madrid",
                "Spain"
        ));

        locations.add(new Location(
                "Amsterdam",
                Arrays.asList("1011", "1012", "1013", "1014", "1015"),
                "North Holland",
                "Netherlands"
        ));

        locations.add(new Location(
                "Vienna",
                Arrays.asList("1010", "1020", "1030", "1040", "1050"),
                "Vienna",
                "Austria"
        ));
        locations.add(new Location(
                "Zurich",
                Arrays.asList("8001", "8002", "8003", "8004", "8005"),
                "Canton of Zurich",
                "Switzerland"
        ));

        locations.add(new Location(
                "London",
                Arrays.asList("SW1A 1AA", "W1A 1AA", "EC1A 1BB", "WC2N 5DU", "N1 9GU"),
                "Greater London",
                "United Kingdom"
        ));
        return locations;
    }

    /**
     * Searches for a {@link Location} based on the provided input string.
     *
     * @param input the search key, which may be a ZIP code or city name; must not be null
     * @return the matching {@link Location} if found; otherwise, an empty {@link Location}
     */
    public Location findLocation(String input) {
        logger.debug("Finding location for input {}", input);
        Location location = null;
        if (input != null) {
            if (zipMap.containsKey(input)) {
                location = zipMap.get(input);
            } else if (cityMap.containsKey(input)) {
                location = cityMap.get(input);
            }
        }
        return location;
    }
}
