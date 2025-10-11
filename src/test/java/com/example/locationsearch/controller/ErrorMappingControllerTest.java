package com.example.locationsearch.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test for {@link ErrorMappingController}.
 */
@WebMvcTest(ErrorMappingController.class)
class ErrorMappingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Tests that accessing the /error endpoint results in a redirection to the root ("/") path.
     **/
    @Test
    void whenGetError_thenRedirectToRoot() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
