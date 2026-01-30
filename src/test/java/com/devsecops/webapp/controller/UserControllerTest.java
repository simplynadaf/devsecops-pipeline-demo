package com.devsecops.webapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHomeEndpoint() throws Exception {
        mockMvc.perform(get("/api/"))
                .andExpect(status().isOk())
                .andExpect(content().string("DevSecOps Demo Application - Version 1.0"));
    }

    @Test
    public void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Application is running"));
    }

    @Test
    public void testGetUserWithValidId() throws Exception {
        mockMvc.perform(get("/api/user/123"))
                .andExpect(status().isOk())
                .andExpect(content().string("User found: User-123"));
    }

    @Test
    public void testGetUserWithInvalidId() throws Exception {
        mockMvc.perform(get("/api/user/abc"))
                .andExpect(status().isOk())
                .andExpect(content().string("User not found"));
    }

    @Test
    public void testDebugEndpoint() throws Exception {
        mockMvc.perform(get("/api/debug"))
                .andExpect(status().isOk())
                .andExpect(content().string("Debug mode enabled. Application version: 1.0.0"));
    }

    @Test
    public void testAddComment() throws Exception {
        mockMvc.perform(post("/api/comment")
                .param("comment", "Test comment"))
                .andExpect(status().isOk());
    }
}
