package com.example.locationsearch.service;

import com.example.locationsearch.model.Location;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for location search operations.
 */
public interface ILocationService {
    Optional<Location> findByZip(String query);

    List<Location> findByCity(String query);
}
