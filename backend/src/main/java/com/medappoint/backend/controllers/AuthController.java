package com.medappoint.backend.controllers;

import com.medappoint.backend.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    // --- EXPLICATION ---
    // Ce contrôleur gère l'inscription et la connexion des utilisateurs.
    // C'est comme le portier de l'immeuble qui vérifie l'identité des gens.

    @PostMapping("/register") // Quand l'application envoie une requête POST sur /register
    public ResponseEntity<User> inscription(@RequestBody RegisterRequest demandeInscription) {
        
        // 1. On crée le nouvel utilisateur
        User nouvelUtilisateur = new User();
        nouvelUtilisateur.setId(System.currentTimeMillis()); // On génère un ID unique avec l'heure
        nouvelUtilisateur.setFullName(demandeInscription.getFullName());
        nouvelUtilisateur.setEmail(demandeInscription.getEmail());
        nouvelUtilisateur.setRole(demandeInscription.getRole());
        
        // 2. Dans une vraie application, on ferait 'userRepository.save(nouvelUtilisateur)'
        // Ici, on le renvoie juste au mobile pour dire "C'est bon, j'ai bien reçu !"
        return ResponseEntity.ok(nouvelUtilisateur);
    }

    @PostMapping("/login") // Quand l'application envoie une requête POST sur /login
    public ResponseEntity<?> connexion(@RequestBody LoginRequest demandeConnexion) {
        String email = demandeConnexion.getEmail();
        String motDePasse = demandeConnexion.getPassword();
        
        // 1. Vérification de base (Est-ce que les champs sont remplis ?)
        if (email == null || motDePasse == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ReponseErreur("Il manque l'email ou le mot de passe !"));
        }
        
        // 2. Logique SIMPLIFIÉE pour le test (pas de base de données complexe ici)
        // On vérifie juste si c'est les comptes de test prévus
        
        if ("admin@test.com".equals(email)) {
            // C'est l'Admin !
            User admin = new User(1L, "Admin System", email, "ADMIN");
            return ResponseEntity.ok(admin);
            
        } else if ("doctor@test.com".equals(email)) {
            // C'est le Docteur !
            User docteur = new User(2L, "Dr. Ahmed Benali", email, "DOCTOR");
            return ResponseEntity.ok(docteur);
            
        } else if ("patient@test.com".equals(email)) { // Ajout du cas Patient
            // C'est un Patient !
            User patient = new User(3L, "Patient Test", email, "PATIENT");
            return ResponseEntity.ok(patient);
            
        } else {
            // 3. Si on ne connait pas cet email
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ReponseErreur("Email inconnu ou mot de passe incorrect."));
        }
    }

    // --- classes utilitaires pour recevoir les données JSON ---
    // Elles servent juste à "porter" les données du mobile vers ici.

    static class RegisterRequest {
        private String fullName;
        private String email;
        private String password;
        private String role; // ADMIN, DOCTOR, PATIENT

        // Getters et Setters (nécessaires pour que Spring Boot lise le JSON)
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    static class ReponseErreur {
        private String message;
        public ReponseErreur(String message) { this.message = message; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}

