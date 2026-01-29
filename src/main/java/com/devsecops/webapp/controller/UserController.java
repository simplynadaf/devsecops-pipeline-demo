package com.devsecops.webapp.controller;

import org.springframework.web.bind.annotation.*;
import java.sql.*;

@RestController
@RequestMapping("/api")
public class UserController {
    
    // Hardcoded credentials - Security vulnerability
    private static final String DB_URL = "jdbc:mysql://localhost:3306/webapp";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "password123";
    
    @GetMapping("/")
    public String home() {
        return "DevSecOps Demo Application - Version 1.0";
    }
    
    @GetMapping("/health")
    public String health() {
        return "Application is running";
    }
    
    // SQL Injection vulnerability - for demo purposes
    @GetMapping("/user/{id}")
    public String getUser(@PathVariable String id) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            // Vulnerable SQL query
            String query = "SELECT * FROM users WHERE id = " + id;
            ResultSet rs = stmt.executeQuery(query);
            
            if (rs.next()) {
                return "User: " + rs.getString("name");
            }
            return "User not found";
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }
    
    // XSS vulnerability - for demo purposes
    @PostMapping("/comment")
    public String addComment(@RequestParam String comment) {
        // No input sanitization
        return "<div>Comment added: " + comment + "</div>";
    }
}
