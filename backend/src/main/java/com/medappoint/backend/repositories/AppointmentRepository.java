package com.medappoint.backend.repositories;

import com.medappoint.backend.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Trouver tous les RDV d'un médecin spécifique
    // SELECT * FROM appointments WHERE doctor_id = ?
    List<Appointment> findByDoctorId(Long doctorId);

    // Trouver tous les RDV d'un patient spécifique
    // SELECT * FROM appointments WHERE patient_id = ?
    List<Appointment> findByPatientId(Long patientId);
}
