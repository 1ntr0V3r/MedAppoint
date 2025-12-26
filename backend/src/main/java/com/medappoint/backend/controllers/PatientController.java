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

    @GetMapping("/appointments/{patientId}")
    public List<Appointment> getPatientAppointments(@PathVariable Long patientId) {
        // Liste codée en dur avec 3 rendez-vous fictifs pour le patient
        List<Appointment> appointments = new ArrayList<>();
        
        // Rendez-vous pour le patient avec ID 1 (Zouhair)
        if (patientId == 1L) {
            appointments.add(new Appointment(
                    1L, 
                    "2024-12-15", 
                    "10:00", 
                    "Consultation générale", 
                    "Zouhair", 
                    1L, 
                    "Dr. Ahmed Benali", 
                    "Terminé"
            ));
            appointments.add(new Appointment(
                    2L, 
                    "2024-12-22", 
                    "14:30", 
                    "Suivi médical", 
                    "Zouhair", 
                    1L, 
                    "Dr. Ahmed Benali", 
                    "À venir"
            ));
            appointments.add(new Appointment(
                    3L, 
                    "2024-12-10", 
                    "09:00", 
                    "Examen de routine", 
                    "Zouhair", 
                    2L, 
                    "Dr. Fatima Alami", 
                    "Terminé"
            ));
        } else {
            // Rendez-vous par défaut pour d'autres patients
            appointments.add(new Appointment(
                    4L, 
                    "2024-12-20", 
                    "11:00", 
                    "Consultation", 
                    "Patient " + patientId, 
                    1L, 
                    "Dr. Ahmed Benali", 
                    "À venir"
            ));
        }
        
        return appointments;
    }
}

