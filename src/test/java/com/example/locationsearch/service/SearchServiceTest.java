package com.example.locationsearch.service;

import com.example.locationsearch.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class SearchServiceTest {

    @MockitoBean
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        searchService = new SearchService();
        searchService.init();
    }

    /**
     * Test: Verify that when input is a known ZIP code then return correct Location
     */
    @Test
    void shouldFindLocationByZipCode() {
        Location result = searchService.findLocation("10115"); // Berlin
        assertNotNull(result);
        assertEquals("Berlin", result.getCity());
        assertEquals("Germany", result.getCountry());
    }

    /**
     * Test: Verify that when input is a known city name then return correct Location
     */
    @Test
    void shouldFindLocationByCityName() {
        Location result = searchService.findLocation("Paris");
        assertNotNull(result);
        assertEquals("Paris", result.getCity());
        assertEquals("France", result.getCountry());
    }

    /**
     * Test: Verify that when input is unknown then return null
     */
    @Test
    void shouldReturnNullForUnknownInput() {
        Location result = searchService.findLocation("UnknownCity");
        assertNull(result);
    }

    /**
     * Test: Verify that when input is null then return null
     */
    @Test
    void shouldReturnNullForNullInput() {
        Location result = searchService.findLocation(null);
        assertNull(result);
    }

}