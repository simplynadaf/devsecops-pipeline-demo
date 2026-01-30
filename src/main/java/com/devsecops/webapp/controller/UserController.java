package com.devsecops.webapp.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    
    @GetMapping("/")
    public String home() {
        return "DevSecOps Demo Application - Version 1.0";
    }
    
    @GetMapping("/health")
    public String health() {
        return "Application is running";
    }
    
    // Weak input validation - Medium severity
    @GetMapping("/user/{id}")
    public String getUser(@PathVariable String id) {
        // Basic validation but still has issues
        if (id == null || id.isEmpty()) {
            return "Invalid user ID";
        }
        
        // Simulated user lookup without actual SQL injection
        if (id.matches("\\d+")) {
            return "User found: User-" + id;
        }
        return "User not found";
    }
    
    // Missing output encoding - Medium severity
    @PostMapping("/comment")
    public String addComment(@RequestParam String comment) {
        // No proper output encoding - potential XSS
        if (comment != null && comment.length() > 0) {
            return "<div>Comment added: " + comment + "</div>";
        }
        return "<div>Empty comment</div>";
    }
    
    // Information disclosure - Low/Medium severity
    @GetMapping("/debug")
    public String debug() {
        return "Debug mode enabled. Application version: 1.0.0";
    }
}
