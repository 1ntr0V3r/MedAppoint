package com.medappoint.backend.repositories;

import com.medappoint.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Annotation Repository indique à Spring que c'est une classe qui gère les données
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Spring Data JPA génère automatiquement la requête SQL : 
    // SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);
}
