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

    @org.springframework.beans.factory.annotation.Autowired
    private com.medappoint.backend.repositories.UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        // Cette méthode récupère la liste complète des utilisateurs depuis la base de données
        // SELECT * FROM users;
        return userRepository.findAll();
    }
}

