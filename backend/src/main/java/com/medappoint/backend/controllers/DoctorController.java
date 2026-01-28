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
        List<Appointment> allAppointments = appointmentRepository.findByDoctorIdOrderByTimeAsc(doctorId);
        List<Appointment> filtered = new ArrayList<>();
        
        // Planning: 09h00 - 16h00 uniquement ET Status CONFIRMED
        for (Appointment app : allAppointments) {
            String t = app.getTime();
            if (t != null && t.compareTo("09:00") >= 0 && t.compareTo("16:00") <= 0) {
                 if ("CONFIRMED".equals(app.getStatus())) {
                     filtered.add(app);
                 }
            }
        }
        return filtered;
    }

    @org.springframework.web.bind.annotation.PutMapping("/appointments/{id}/complete")
    public void completeAppointment(@PathVariable Long id) {
        Appointment app = appointmentRepository.findById(id).orElse(null);
        if (app != null) {
            app.setStatus("COMPLETED");
            appointmentRepository.save(app);
        }
    }
}

