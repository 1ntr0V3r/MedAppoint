package com.medappoint.backend.controllers;

import com.medappoint.backend.models.Appointment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @GetMapping("/appointments/{doctorId}")
    public List<Appointment> getDoctorAppointments(@PathVariable Long doctorId) {
        // Liste codée en dur avec des rendez-vous fictifs pour le médecin
        List<Appointment> appointments = new ArrayList<>();
        
        // Rendez-vous pour le médecin avec ID 1 (Dr. Ahmed Benali)
        if (doctorId == 1L) {
            appointments.add(new Appointment(1L, "2024-12-20", "09:00", "Consultation générale", "Fatima Alami", 1L));
            appointments.add(new Appointment(2L, "2024-12-20", "10:30", "Suivi post-opératoire", "Mohamed Tazi", 1L));
            appointments.add(new Appointment(3L, "2024-12-20", "14:00", "Examen de routine", "Aicha Bensaid", 1L));
            appointments.add(new Appointment(4L, "2024-12-21", "09:30", "Consultation urgente", "Hassan El Amrani", 1L));
            appointments.add(new Appointment(5L, "2024-12-21", "11:00", "Vaccination", "Sara Idrissi", 1L));
        } else {
            // Rendez-vous par défaut pour d'autres médecins
            appointments.add(new Appointment(6L, "2024-12-20", "08:00", "Consultation", "Patient 1", doctorId));
            appointments.add(new Appointment(7L, "2024-12-20", "10:00", "Consultation", "Patient 2", doctorId));
        }
        
        return appointments;
    }
}

