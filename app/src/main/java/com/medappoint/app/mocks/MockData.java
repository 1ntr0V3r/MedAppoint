package com.medappoint.app.mocks;

import com.medappoint.app.models.Appointment;
import com.medappoint.app.models.User;
import java.util.ArrayList;
import java.util.List;

public class MockData {
    public static List<User> users = new ArrayList<>();
    public static List<Appointment> appointments = new ArrayList<>();
    
    static {
        // --- 1. ADMIN USER ---
        User admin = new User();
        admin.setId(1L);
        admin.setFullName("Admin System");
        admin.setEmail("admin@test.com");
        admin.setPassword("admin123");
        admin.setRole("ADMIN");
        users.add(admin);

        // --- 2. DOCTOR USER ---
        User doc = new User();
        doc.setId(2L);
        doc.setFullName("Dr. Ahmed Benali");
        doc.setEmail("doctor@test.com");
        doc.setPassword("doctor123");
        doc.setRole("DOCTOR");
        users.add(doc);
        
        // --- 3. PATIENT USER ---
        User pat = new User();
        pat.setId(3L);
        pat.setFullName("Zouhair El");
        pat.setEmail("patient@test.com");
        pat.setPassword("patient123");
        pat.setRole("PATIENT");
        pat.setAge(25);
        pat.setSex("M");
        users.add(pat);

        // --- APPOINTMENTS ---
        Appointment app1 = new Appointment();
        app1.setId(1L);
        app1.setDate("2024-12-30");
        app1.setTime("09:00");
        app1.setReason("Consultation 1");
        app1.setPatientId(3L);
        app1.setPatientName("Zouhair El");
        app1.setDoctorId(2L);
        app1.setDoctorName("Dr. Ahmed Benali");
        app1.setStatus("CONFIRMED");
        appointments.add(app1);

        Appointment app2 = new Appointment();
        app2.setId(2L);
        app2.setDate("2025-01-05");
        app2.setTime("14:30");
        app2.setReason("Mal de tete");
        app2.setPatientId(3L);
        app2.setPatientName("Zouhair El");
        app2.setStatus("PENDING");
        appointments.add(app2);
    }
}
