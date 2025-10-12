package com.example.locationsearch.controller;

import com.example.locationsearch.model.Location;
import com.example.locationsearch.service.SearchService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
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
    private SearchService searchService;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute("loggedInUser", "testUser");
    }

    /**
     * Test: Verify that accessing /search without a logged-in user redirects to the login page.
     */
    @Test
    void shouldSearchRedirectsToLoginIfNotLoggedIn() throws Exception {
        mockMvc.perform(get("/search"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    /**
     * Test: Verify that accessing /search with a valid session shows the search view.
     */
    @Test
    void shouldSearchPageViewIfLoggedIn() throws Exception {
        session.setAttribute("loggedInUser", "user123");

        mockMvc.perform(get("/search").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("search"));
    }

    /**
     * Test: Verify that Unauthenticated access should redirect to /login
     */
    @Test
    void shouldRedirectToLoginIfUnauthenticated() throws Exception {
        mockMvc.perform(get("/result").param("input", "Dortmund"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    /**
     * Test: Verify that valid input with matching location should populate model with result
     */
    @Test
    void shouldReturnLocationResultForValidInput() throws Exception {
        Location mockDto = new Location();
        mockDto.setCity("Dortmund");
        Mockito.when(searchService.findLocation("Dortmund")).thenReturn(mockDto);

        mockMvc.perform(get("/result").param("input", "Dortmund").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("result"))
                .andExpect(view().name("search"));
    }

    /**
     * Test: Verify that valid input with no matching location should populate model with error
     */
    @Test
    void shouldReturnErrorIfLocationNotFound() throws Exception {
        Mockito.when(searchService.findLocation(anyString())).thenReturn(null);

        mockMvc.perform(get("/result").param("input", "UnknownCity").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "No location found for input: UnknownCity"))
                .andExpect(view().name("search"));
    }

    /**
     * Test: Verify that null input should return error message
     */
    @Test
    void shouldReturnErrorWhenInputIsMissing() throws Exception {
        mockMvc.perform(get("/result")
                        .session(session))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test: Verify that empty input should return error message
     */
    @Test
    void shouldReturnErrorWhenInputIsEmpty() throws Exception {
        mockMvc.perform(get("/result")
                        .param("input", "")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Input is required"));
    }

    /**
     * Test: Verify that blank input (spaces only) should return error message
     */
    @Test
    void shouldReturnErrorWhenInputIsBlank() throws Exception {
        mockMvc.perform(get("/result")
                        .param("input", "   ")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Input is required"));
    }

    /**
     * Test: Verify that valid input with spaces is processed correctly (trimmed)
     */
    @Test
    void shouldProcessValidInputWithSpaces() throws Exception {
        Location mockDto = new Location();
        mockDto.setCity("New York");
        Mockito.when(searchService.findLocation("New York")).thenReturn(mockDto);

        mockMvc.perform(get("/result").param("input", "  New York  ").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attributeExists("result"));
    }

}
