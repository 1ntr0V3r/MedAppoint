package com.medappoint.app.models;

public class Appointment {
    private Long id;
    private String date;
    private String time;
    private String reason;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private String status;

    // Constructeur par défaut
    public Appointment() {
    }

    // Constructeur avec paramètres
    public Appointment(Long id, String date, String time, String reason, String patientName, Long doctorId) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.patientName = patientName;
        this.doctorId = doctorId;
    }

    // Constructeur complet avec doctorName et status
    public Appointment(Long id, String date, String time, String reason, String patientName, Long doctorId, String doctorName, String status) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.patientName = patientName;
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
    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}

