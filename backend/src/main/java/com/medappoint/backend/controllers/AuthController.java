package com.medappoint.backend.controllers;

import com.medappoint.backend.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    // Injection du Repository pour parler à la Base de Données
    @org.springframework.beans.factory.annotation.Autowired
    private com.medappoint.backend.repositories.UserRepository userRepository;

    @PostMapping("/register") 
    public ResponseEntity<?> inscription(@RequestBody RegisterRequest demandeInscription) {
        System.out.println(">>> REGISTER REQUEST: " + demandeInscription.getEmail());
        
        try {
            // 1. Vérifier si l'email existe déjà en base
            if (userRepository.findByEmail(demandeInscription.getEmail()).isPresent()) {
                System.out.println(">>> REGISTER FAILED: Email already exists - " + demandeInscription.getEmail());
                return ResponseEntity.badRequest()
                        .body(new ReponseErreur("Cet email est déjà utilisé ! (Server)"));
            }

            // 2. Créer le nouvel utilisateur
            User nouvelUtilisateur = new User();
            nouvelUtilisateur.setFullName(demandeInscription.getFullName());
            nouvelUtilisateur.setEmail(demandeInscription.getEmail());
            nouvelUtilisateur.setPassword(demandeInscription.getPassword()); 
            nouvelUtilisateur.setRole(demandeInscription.getRole());
            
            // 3. Sauvegarder dans la base de données PostgreSQL
            User utilisateurSauvegarde = userRepository.save(nouvelUtilisateur);
            System.out.println(">>> REGISTER SUCCESS: User ID " + utilisateurSauvegarde.getId());
            
            return ResponseEntity.ok(utilisateurSauvegarde);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(">>> REGISTER ERROR: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ReponseErreur("Erreur Serveur: " + e.getMessage()));
        }
    }

    @PostMapping("/login") 
    public ResponseEntity<?> connexion(@RequestBody LoginRequest demandeConnexion) {
        System.out.println(">>> LOGIN REQUEST: " + demandeConnexion.getEmail());
        try {
            User utilisateur = userRepository.findByEmail(demandeConnexion.getEmail()).orElse(null);
            
            if (utilisateur != null && utilisateur.getPassword().equals(demandeConnexion.getPassword())) {
                System.out.println(">>> LOGIN SUCCESS: " + utilisateur.getEmail() + " (" + utilisateur.getRole() + ")");
                return ResponseEntity.ok(utilisateur);
            } else {
                System.out.println(">>> LOGIN FAILED: Wrong creds for " + demandeConnexion.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ReponseErreur("Email inconnu ou mot de passe incorrect."));
            }
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ReponseErreur("Erreur Login: " + e.getMessage()));
        }
    }

    // --- classes utilitaires pour recevoir les données JSON ---

    static class RegisterRequest {
        private String fullName;
        private String email;
        private String password;
        private String role; // ADMIN, DOCTOR, PATIENT

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

