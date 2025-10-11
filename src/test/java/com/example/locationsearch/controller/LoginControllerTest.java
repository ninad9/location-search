package com.example.locationsearch.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;

    @BeforeEach
    void setup() {
        session = new MockHttpSession();
    }

    @Test
    void testLoginPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testSuccessfulLoginSetsSessionAndReturns200() throws Exception {
        String json = """
                {
                  "userId": "user",
                  "password": "pass"
                }
                """;

        mockMvc.perform(post("/login")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));

        // After login, the session should have loggedInUser attribute
        Object attr = session.getAttribute("loggedInUser");
        assert attr != null && attr.equals("user");

    }

    @Test
    void testLoginWithBlankUserIdReturnsBadRequest() throws Exception {
        String json = """
                {
                  "userId": "",
                  "password": "pwd"
                }
                """;

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("{\"userId\":\"User ID is required\"}")));
    }

    @Test
    void testGetLoginWithWrongMethod_onLoginEndpoint() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void testLogoutInvalidatesSessionAndRedirects() throws Exception {
        // Pre-set a session attribute
        session.setAttribute("loggedInUser", "bob");

        mockMvc.perform(get("/logout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

}