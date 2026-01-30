package com.devsecops.webapp.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserController userController = new UserController();

    @Test
    public void testHomeEndpoint() {
        String result = userController.home();
        assertEquals("DevSecOps Demo Application - Version 1.0", result);
    }

    @Test
    public void testHealthEndpoint() {
        String result = userController.health();
        assertEquals("Application is running", result);
    }

    @Test
    public void testGetUserWithValidId() {
        String result = userController.getUser("123");
        assertEquals("User found: User-123", result);
    }

    @Test
    public void testGetUserWithInvalidId() {
        String result = userController.getUser("abc");
        assertEquals("User not found", result);
    }

    @Test
    public void testDebugEndpoint() {
        String result = userController.debug();
        assertEquals("Debug mode enabled. Application version: 1.0.0", result);
    }

    @Test
    public void testAddComment() {
        String result = userController.addComment("Test comment");
        assertTrue(result.contains("Test comment"));
    }
}
