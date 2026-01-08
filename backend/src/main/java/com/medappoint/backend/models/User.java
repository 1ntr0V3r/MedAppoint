import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Le nom complet de l'utilisateur
    private String fullName;
    
    // L'email servira d'identifiant de connexion
    private String email;
    
    // Le mot de passe (stocké en clair pour cet exercice simple)
    // @JsonIgnore empêche d'envoyer le mot de passe dans la réponse JSON vers le mobile
    @com.fasterxml.jackson.annotation.JsonIgnore
    private String password;
    
    // Le rôle : ADMIN, DOCTOR, ou PATIENT
    private String role;

    // Constructeur par défaut (nécessaire pour JPA)
    public User() {
    }

    // Constructeur avec paramètres
    public User(Long id, String fullName, String email, String password, String role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

