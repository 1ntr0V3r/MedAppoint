package com.medappoint.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String date;
    private String time;
    private String reason;
    
    // ID du patient qui a pris ce rendez-vous (lien vers la table users)
    private Long patientId;
    
    // Nom du patient (pour affichage facile sans jointure complexe)
    private String patientName;
    
    // ID du médecin concerné (lien vers la table users)
    private Long doctorId;
    
    // Nom du médecin (pour affichage facile)
    private String doctorName;
    
    private String status;

    // Constructeur par défaut (pour JPA)
    public Appointment() {
    }

    // Constructeur simple
    public Appointment(Long id, String date, String time, String reason, String patientName, Long patientId, Long doctorId) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.patientName = patientName;
        this.patientId = patientId;
        this.doctorId = doctorId;
    }

    // Constructeur complet
    public Appointment(Long id, String date, String time, String reason, String patientName, Long patientId, Long doctorId, String doctorName, String status) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.patientName = patientName;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.status = status;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

