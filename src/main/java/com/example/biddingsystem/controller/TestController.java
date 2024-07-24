package com.example.biddingsystem.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://192.168.1.33:8081", "http://localhost:8080"})
public class TestController {

    @GetMapping("/api/test")
    public String testEndpoint() {
        return "CORS is working!";
    }
}
