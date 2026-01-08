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
        
        // 1. Vérifier si l'email existe déjà en base
        if (userRepository.findByEmail(demandeInscription.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(new ReponseErreur("Cet email est déjà utilisé !"));
        }

        // 2. Créer le nouvel utilisateur
        User nouvelUtilisateur = new User();
        nouvelUtilisateur.setFullName(demandeInscription.getFullName());
        nouvelUtilisateur.setEmail(demandeInscription.getEmail());
        nouvelUtilisateur.setPassword(demandeInscription.getPassword()); // Attention : Pas de hashage demandé pour l'instant
        nouvelUtilisateur.setRole(demandeInscription.getRole());
        
        // 3. Sauvegarder dans la base de données PostgreSQL
        User utilisateurSauvegarde = userRepository.save(nouvelUtilisateur);
        
        return ResponseEntity.ok(utilisateurSauvegarde);
    }

    @PostMapping("/login") 
    public ResponseEntity<?> connexion(@RequestBody LoginRequest demandeConnexion) {
        String email = demandeConnexion.getEmail();
        String motDePasse = demandeConnexion.getPassword();
        
        // 1. Chercher l'utilisateur dans la base par son email
        // .orElse(null) retourne null si l'utilisateur n'est pas trouvé
        User utilisateur = userRepository.findByEmail(email).orElse(null);
        
        // 2. Vérifier si l'utilisateur existe ET si le mot de passe correspond
        if (utilisateur != null && utilisateur.getPassword().equals(motDePasse)) {
            // C'est bon ! On renvoie l'utilisateur (avec son ID et son Rôle)
            return ResponseEntity.ok(utilisateur);
        } else {
            // Mauvais identifiants
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ReponseErreur("Email inconnu ou mot de passe incorrect."));
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

