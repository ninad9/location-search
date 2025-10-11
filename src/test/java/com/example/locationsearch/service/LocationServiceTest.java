package com.example.locationsearch.service;

import com.example.locationsearch.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocationServiceTest {

    private LocationService service;

    @BeforeEach
    void setUp() {
        service = new LocationService();
        service.init();
    }

    @Test
    void testInit_populatesMaps() {
        List<Location> berlinLocations = service.findByCity("berlin");
        assertFalse(berlinLocations.isEmpty(), "Berlin should yield some ZIPs after init");
        Optional<Location> maybe = service.findByZip("10115");
        assertTrue(maybe.isPresent(), "ZIP 10115 should resolve to a city after init");
    }

    @Test
    void findByCity_validCity() {
        List<Location> locs = service.findByCity("munich");
        assertFalse(locs.isEmpty(), "Expected non-empty result for known city");
        for (Location loc : locs) {
            assertEquals("MUNICH", loc.city(), "City name should be uppercased in Location DTO");
            assertNotNull(loc.zip(), "ZIP should not be null");
        }
    }

    @Test
    void findByCity_caseInsensitive() {
        List<Location> locs1 = service.findByCity("Berlin");
        List<Location> locs2 = service.findByCity("bErLiN");
        assertEquals(locs1, locs2, "City lookup should be case-insensitive");
    }

    @Test
    void findByCity_unknownCity_returnsEmpty() {
        List<Location> locs = service.findByCity("nonexistentcity");
        assertTrue(locs.isEmpty(), "Unknown city should return empty list");
    }

    @Test
    void findByCity_nullOrBlank_returnsEmpty() {
        assertTrue(service.findByCity(null).isEmpty(), "Null city should return empty list");
        assertTrue(service.findByCity("").isEmpty(), "Empty city should return empty list");
        assertTrue(service.findByCity("   ").isEmpty(), "Blank city should return empty list");
    }

    @Test
    void findByZip_validZip() {
        Optional<Location> locOpt = service.findByZip("10117");
        assertTrue(locOpt.isPresent(), "Known ZIP should return a result");
        Location loc = locOpt.get();
        assertEquals("BERLIN", loc.city(), "City should match for the ZIP");
        assertEquals("10117", loc.zip(), "ZIP in DTO should match input");
    }

    @Test
    void findByZip_unknownZip_returnsEmpty() {
        Optional<Location> locOpt = service.findByZip("99999");
        assertTrue(locOpt.isEmpty(), "Unknown ZIP should return empty Optional");
    }

    @Test
    void findByZip_nullOrBlank_returnsEmpty() {
        assertTrue(service.findByZip(null).isEmpty(), "Null ZIP should return empty");
        assertTrue(service.findByZip("").isEmpty(), "Empty ZIP should return empty");
        assertTrue(service.findByZip("   ").isEmpty(), "Blank ZIP should return empty");
    }

    @Test
    void consistency_cityZipInvertibility_forSampleEntry() {

        String city = "cologne";
        List<Location> zips = service.findByCity(city);
        assertFalse(zips.isEmpty(), "cologne should have some ZIPs");
        Location first = zips.get(0);
        String zip = first.zip();
        Optional<Location> back = service.findByZip(zip);
        assertTrue(back.isPresent(), "reverse lookup should find city for that ZIP");
        assertEquals(first.city(), back.get().city(), "city from reverse lookup should match forward city");
    }
}
