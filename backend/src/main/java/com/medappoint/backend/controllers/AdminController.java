package com.medappoint.backend.controllers;

import com.medappoint.backend.models.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/users")
    public List<User> getAllUsers() {
        // Liste codée en dur avec 3 utilisateurs fictifs
        List<User> users = new ArrayList<>();
        
        users.add(new User(1L, "Dr. Ahmed Benali", "ahmed.benali@medappoint.com", "DOCTOR"));
        users.add(new User(2L, "Fatima Alami", "fatima.alami@medappoint.com", "PATIENT"));
        users.add(new User(3L, "Admin System", "admin@medappoint.com", "ADMIN"));
        
        return users;
    }
}

