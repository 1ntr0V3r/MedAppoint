package com.medappoint.backend.controllers;

import com.medappoint.backend.models.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @org.springframework.beans.factory.annotation.Autowired
    private com.medappoint.backend.repositories.UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @org.springframework.web.bind.annotation.DeleteMapping("/users/{id}")
    public void deleteUser(@org.springframework.web.bind.annotation.PathVariable Long id) {
        userRepository.deleteById(id);
    }
    
    @org.springframework.beans.factory.annotation.Autowired
    private com.medappoint.backend.repositories.AppointmentRepository appointmentRepository;

    @GetMapping("/appointments/pending")
    public List<com.medappoint.backend.models.Appointment> getPendingAppointments() {
        List<com.medappoint.backend.models.Appointment> all = appointmentRepository.findAll();
        List<com.medappoint.backend.models.Appointment> pending = new ArrayList<>();
        for (com.medappoint.backend.models.Appointment app : all) {
            if ("PENDING".equals(app.getStatus())) {
                pending.add(app);
            }
        }
        return pending;
    }

    @org.springframework.web.bind.annotation.PutMapping("/appointments/{id}/assign")
    public com.medappoint.backend.models.Appointment assignDoctor(
            @org.springframework.web.bind.annotation.PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestParam Long doctorId) {
        
        com.medappoint.backend.models.Appointment app = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rendez-vous introuvable"));
        
        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Docteur introuvable"));

        app.setDoctorId(doctor.getId());
        app.setDoctorName(doctor.getFullName());
        app.setStatus("CONFIRMED");
        
        return appointmentRepository.save(app);
    }
}

