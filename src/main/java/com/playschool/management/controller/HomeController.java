package com.playschool.management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/home")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to PlaySchool Management API");
        response.put("status", "success");
        response.put("version", "1.0.0");
        response.put("endpoints", Map.of(
            "auth", "/api/auth",
            "students", "/api/students",
            "test", "/api/test",
            "user-roles", "/api/user-roles"
        ));
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/ping")
    public ResponseEntity<Map<String, Object>> ping(@RequestBody(required = false) Map<String, Object> requestBody) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Pong! PlaySchool API is running");
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", "active");
        
        if (requestBody != null && !requestBody.isEmpty()) {
            response.put("receivedData", requestBody);
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/status")
    public ResponseEntity<Map<String, Object>> status(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("api", "PlaySchool Management Backend");
        response.put("status", "running");
        response.put("database", "PostgreSQL");
        response.put("authentication", "JWT");
        response.put("timestamp", java.time.LocalDateTime.now());
        
        // Echo back any client info
        if (request.containsKey("clientName")) {
            response.put("clientName", request.get("clientName"));
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "PlaySchool Management API");
        return ResponseEntity.ok(response);
    }
}