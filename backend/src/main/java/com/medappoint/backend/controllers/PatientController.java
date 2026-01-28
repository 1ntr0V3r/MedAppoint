package com.medappoint.backend.controllers;

import com.medappoint.backend.models.Appointment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/patient")
public class PatientController {
    
    @org.springframework.beans.factory.annotation.Autowired
    private com.medappoint.backend.repositories.AppointmentRepository appointmentRepository;

    @org.springframework.beans.factory.annotation.Autowired
    private com.medappoint.backend.repositories.UserRepository userRepository;

    @GetMapping("/appointments/{patientId}")
    public List<Appointment> getPatientAppointments(@PathVariable Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }
    
    @GetMapping("/doctors")
    public List<com.medappoint.backend.models.User> getAllDoctors() {
        return userRepository.findByRole("DOCTOR");
    }
    
    @org.springframework.web.bind.annotation.PostMapping("/book")
    public Appointment bookAppointment(@org.springframework.web.bind.annotation.RequestBody Appointment appointment) {
        // Le patient soumet une demande SANS docteur.
        // On met le statut EN ATTENTE (PENDING)
        appointment.setStatus("PENDING");
        appointment.setDoctorId(null); // Force null au cas où
        appointment.setDoctorName(null);
        
        return appointmentRepository.save(appointment);
    }
}

