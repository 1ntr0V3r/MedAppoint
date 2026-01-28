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
        // Logique simple pour l'instant : on sauvegarde directement
        // Idéalement, on vérifierait si le créneau est libre
        appointment.setStatus("PENDING");
        return appointmentRepository.save(appointment);
    }
}

