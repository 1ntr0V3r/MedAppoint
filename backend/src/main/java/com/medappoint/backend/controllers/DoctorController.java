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

    @org.springframework.beans.factory.annotation.Autowired
    private com.medappoint.backend.repositories.AppointmentRepository appointmentRepository;

    @GetMapping("/appointments/{doctorId}")
    public List<Appointment> getDoctorAppointments(@PathVariable Long doctorId) {
        // Cette méthode va chercher dans la base de données tous les rendez-vous
        // associés à l'ID du médecin fourni.
        return appointmentRepository.findByDoctorId(doctorId);
    }
}

