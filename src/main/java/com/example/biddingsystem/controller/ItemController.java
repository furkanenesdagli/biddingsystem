package com.example.biddingsystem.controller;

import com.example.biddingsystem.model.Item;
import com.example.biddingsystem.model.User;
import com.example.biddingsystem.service.ItemService;
import com.example.biddingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = {"http://192.168.1.33:8081", "http://localhost:8080"})
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @PostMapping
    public Item createItem(@RequestBody Item item, @RequestParam Long userId) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent()) {
            item.setUser(user.get()); // Set the user
            Item savedItem = itemService.saveItem(item);
            System.out.println("Item saved: " + savedItem);
            return savedItem;
        } else {
            throw new RuntimeException("User not found for id :: " + userId);
        }
    }

    @GetMapping("/{id}")
    public Item getItemById(@PathVariable Long id) {
        Optional<Item> item = itemService.getItemById(id);
        if (item.isPresent()) {
            return item.get();
        } else {
            throw new RuntimeException("Item not found for id :: " + id);
        }
    }

    @GetMapping
    public List<Item> getItems() {
        List<Item> items = itemService.getAllItems();
        System.out.println("Fetched items: " + items);
        return items;
    }
}
