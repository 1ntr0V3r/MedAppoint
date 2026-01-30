package com.medappoint.app.models;

public class User {
    private Long id;
    private String fullName;
    private String email;
    private String role;

    // Constructeur par défaut
    public User() {
    }

    // Constructeur avec paramètres
    public User(Long id, String fullName, String email, String role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    private String password; // Only used in Mock mode
    private Integer age;
    private String sex;
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    
    @Override
    public String toString() {
        return fullName; // Pour l'affichage dans le Spinner
    }
}

