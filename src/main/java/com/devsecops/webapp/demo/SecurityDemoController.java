package com.devsecops.webapp.demo;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

/**
 * DEMO CONTROLLER - Contains intentional security issues for demonstration
 * Shows various vulnerability types that SonarQube will detect
 */
@RestController
@RequestMapping("/api/demo")
public class SecurityDemoController {
    
    @Autowired
    private VulnerabilityDemoService demoService;
    
    // HIGH VULNERABILITY: XSS - No output encoding
    @GetMapping("/unsafe-comment")
    public String unsafeComment(@RequestParam String comment) {
        // Direct output without encoding - XSS vulnerability
        return "<div>User comment: " + comment + "</div>";
    }
    
    // HIGH VULNERABILITY: SQL Injection via service
    @GetMapping("/user/{id}")
    public String getUserUnsafe(@PathVariable String id) {
        try {
            List<String> userData = demoService.getUserData(id);
            return "User data: " + userData.toString();
        } catch (Exception e) {
            return "Database error: " + e.getMessage(); // Information disclosure
        }
    }
    
    // MEDIUM VULNERABILITY: Path traversal
    @GetMapping("/file")
    public String readFileUnsafe(@RequestParam String filename) {
        // No input validation - allows path traversal
        return demoService.readFile(filename);
    }
    
    // LOW VULNERABILITY: Information disclosure
    @GetMapping("/debug")
    public Map<String, Object> getDebugInfo() {
        // Exposes sensitive system information
        return demoService.getDebugInfo();
    }
    
    // MEDIUM VULNERABILITY: Weak password hashing
    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password) {
        // Uses weak MD5 hashing
        String hashedPassword = demoService.hashPassword(password);
        return "User registered with hash: " + hashedPassword;
    }
    
    // CODE SMELL: Too many parameters
    @PostMapping("/profile")
    public String updateProfile(@RequestParam String name, @RequestParam String email,
                               @RequestParam String phone, @RequestParam String address,
                               @RequestParam String city, @RequestParam String state,
                               @RequestParam String zip, @RequestParam String country,
                               @RequestParam String company, @RequestParam String department) {
        demoService.processUserData(name, email, phone, address, city, state, zip, country, company, department, "user");
        return "Profile updated";
    }
    
    // MAJOR BUG: Null pointer exception potential
    @PostMapping("/calculate")
    public String calculateTotal(@RequestBody List<Integer> numbers) {
        // No null checks - potential NPE
        int total = demoService.calculateTotal(numbers);
        return "Total: " + total;
    }
    
    // MEDIUM VULNERABILITY: ReDoS (Regular Expression Denial of Service)
    @GetMapping("/validate-email")
    public String validateEmail(@RequestParam String email) {
        boolean isValid = demoService.validateEmail(email);
        return "Email valid: " + isValid;
    }
    
    // CODE SMELL: Cognitive complexity
    @GetMapping("/user-level")
    public String getUserLevel(@RequestParam int score, @RequestParam boolean isPremium,
                              @RequestParam String userType, @RequestParam int yearsActive,
                              @RequestParam boolean hasReferrals) {
        String level = demoService.determineUserLevel(score, isPremium, userType, yearsActive, hasReferrals);
        return "User level: " + level;
    }
    
    // MINOR VULNERABILITY: Hardcoded credentials
    @GetMapping("/admin-check")
    public String adminCheck(@RequestParam String password) {
        if ("admin123".equals(password)) { // Hardcoded admin password
            return "Admin access granted";
        }
        return "Access denied";
    }
    
    // CODE SMELL: Duplicated string literals
    @GetMapping("/status")
    public String getStatus() {
        return "Service is running"; // Duplicated literal
    }
    
    @GetMapping("/health")
    public String getHealth() {
        return "Service is running"; // Duplicated literal
    }
}
