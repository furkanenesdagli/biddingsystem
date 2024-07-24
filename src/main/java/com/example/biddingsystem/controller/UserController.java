package com.example.biddingsystem.controller;

import com.example.biddingsystem.model.User;
import com.example.biddingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:8080","http://192.168.1.33:8081"}
)
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody User user) {
        userService.saveUser(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        return response;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {
        try {
            User existingUser = userService.findByUsername(user.getUsername());
            if (existingUser != null && user.getPassword().equals(existingUser.getPassword())) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "User logged in successfully");
                response.put("userId", existingUser.getId().toString());
                return response;
            } else {
                throw new Exception("Invalid credentials");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login failed");
            response.put("error", e.getMessage());
            return response;
        }
    }
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("User not found for id :: " + id);
        }
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setEmail(userDetails.getEmail());
            existingUser.setUsername(userDetails.getUsername());
            existingUser.setPassword(userDetails.getPassword());
            return userService.saveUser(existingUser);
        } else {
            throw new RuntimeException("User not found for id :: " + id);
        }
    }
}
