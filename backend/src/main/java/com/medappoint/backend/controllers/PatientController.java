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

    @GetMapping("/appointments/{patientId}")
    public List<Appointment> getPatientAppointments(@PathVariable Long patientId) {
        // Logique simplifiée : Repository -> Base de Données
        // Récupère tous les RDV où patient_id = patientId
        return appointmentRepository.findByPatientId(patientId);
    }
}

