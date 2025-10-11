package com.example.locationsearch.controller;

import com.example.locationsearch.model.Location;
import com.example.locationsearch.service.ILocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ILocationService locationService;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
    }

    /**
     * Tests that accessing /search without a logged-in user redirects to the login page.
     */
    @Test
    void whenNotLoggedIn_thenSearchRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/search"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    /**
     * Tests that accessing /search with a valid session shows the search view.
     */
    @Test
    void whenLoggedIn_thenSearchPageView() throws Exception {
        session.setAttribute("loggedInUser", "user123");

        mockMvc.perform(get("/search").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("search"));
    }

    /**
     * Tests that accessing /result without a logged-in user redirects to the login page.
     */
    @Test
    void result_whenNotLoggedIn_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/result")
                        .param("query", "Berlin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    /**
     * Tests that submitting a blank query to /result shows an error message in the model.
     */
    @Test
    void result_whenBlankQuery_showsErrorModel() throws Exception {
        session.setAttribute("loggedInUser", "user123");
        mockMvc.perform(get("/result").session(session).param("query", "   "))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Enter valid input"));
    }

    /**
     * Tests that a zip code match returns a Location and sets it in the model under 'searchByZip'.
     */
    @Test
    void result_whenZipMatch_modelHasSearchByZip() throws Exception {
        session.setAttribute("loggedInUser", "user123");
        Location loc = new Location("Berlin", "10115");
        when(locationService.findByZip("10115")).thenReturn(Optional.of(loc));

        mockMvc.perform(get("/result").session(session).param("query", "10115"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attributeExists("searchByZip"))
                .andExpect(model().attribute("searchByZip", loc));
    }

    /**
     * Tests that a city name match returns multiple Locations and sets them in the model under 'searchByCity'.
     */
    @Test
    void result_whenCityMatch_modelHasSearchByCity() throws Exception {
        session.setAttribute("loggedInUser", "user123");
        Location loc1 = new Location("Berlin", "10115");
        Location loc2 = new Location("Berlin", "10117");
        when(locationService.findByZip(anyString())).thenReturn(Optional.empty());
        when(locationService.findByCity("Berlin")).thenReturn(List.of(loc1, loc2));

        mockMvc.perform(get("/result").session(session).param("query", "Berlin"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attributeExists("searchByCity"))
                .andExpect(model().attribute("cityName", "Berlin"));
    }

    /**
     * Tests that no match for a query shows an appropriate error message in the model.
     */
    @Test
    void result_whenNoMatch_showsNoResultError() throws Exception {
        session.setAttribute("loggedInUser", "user123");
        when(locationService.findByZip(anyString())).thenReturn(Optional.empty());
        when(locationService.findByCity(anyString())).thenReturn(List.of());

        String query = "Unknown";
        mockMvc.perform(get("/result").session(session).param("query", query))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "No location found for input: " + query));
    }
}
