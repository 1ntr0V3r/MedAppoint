package com.medappoint.app.mocks;

import com.medappoint.app.api.ApiService;
import com.medappoint.app.models.Appointment;
import com.medappoint.app.models.LoginRequest;
import com.medappoint.app.models.RegisterRequest;
import com.medappoint.app.models.User;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;

public class MockApiService implements ApiService {

    @Override
    public Call<List<User>> getAllUsers() {
        return new MockCall<>(MockData.users);
    }

    @Override
    public Call<List<Appointment>> getDoctorAppointments(Long doctorId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : MockData.appointments) {
            if (a.getDoctorId() != null && a.getDoctorId().equals(doctorId)) {
                result.add(a);
            }
        }
        return new MockCall<>(result);
    }

    @Override
    public Call<List<Appointment>> getPatientAppointments(Long patientId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : MockData.appointments) {
            if (a.getPatientId() != null && a.getPatientId().equals(patientId)) {
                result.add(a);
            }
        }
        return new MockCall<>(result);
    }

    @Override
    public Call<User> register(RegisterRequest request) {
        // Check duplicate
        for (User u : MockData.users) {
            if (u.getEmail().equalsIgnoreCase(request.getEmail())) {
                // Ideally return error, but for mock we can just return null or throw
                // We'll return null to simulate failure, triggering logic in Activity?
                // Or better, creating a wrapper error.
                // For simplicity, let's just create it anyway or append RAND to email
            }
        }
        User newUser = new User();
        newUser.setId((long) (MockData.users.size() + 1));
        newUser.setFullName(request.getFullName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());
        newUser.setRole(request.getRole());
        MockData.users.add(newUser);
        return new MockCall<>(newUser);
    }

    @Override
    public Call<User> login(LoginRequest request) {
        for (User u : MockData.users) {
            if (u.getEmail().equalsIgnoreCase(request.getEmail()) && u.getPassword().equals(request.getPassword())) {
                return new MockCall<>(u);
            }
        }
        // Return null body to simulate 401/404? Retrofit treats null body 
        // in success callback as OK with null body usually, needing checks.
        // Let's return null to indicate failure
        return new MockCall<>(null);
    }

    @Override
    public Call<List<User>> getAllDoctors() {
        List<User> docs = new ArrayList<>();
        for (User u : MockData.users) {
            if ("DOCTOR".equals(u.getRole())) {
                docs.add(u);
            }
        }
        return new MockCall<>(docs);
    }

    @Override
    public Call<Appointment> bookAppointment(Appointment appointment) {
        appointment.setId((long) (MockData.appointments.size() + 1));
        appointment.setStatus("PENDING"); // Default logic
        MockData.appointments.add(appointment);
        return new MockCall<>(appointment);
    }

    @Override
    public Call<Void> completeAppointment(Long id) {
        for (Appointment a : MockData.appointments) {
            if (a.getId().equals(id)) {
                a.setStatus("COMPLETED");
                break;
            }
        }
        return new MockCall<>(null);
    }

    @Override
    public Call<List<Appointment>> getPendingAppointments() {
        List<Appointment> pending = new ArrayList<>();
        for (Appointment a : MockData.appointments) {
            if ("PENDING".equals(a.getStatus())) {
                pending.add(a);
            }
        }
        return new MockCall<>(pending);
    }

    @Override
    public Call<Void> assignDoctor(Long appointmentId, Long doctorId) {
        for (Appointment a : MockData.appointments) {
            if (a.getId().equals(appointmentId)) {
                a.setDoctorId(doctorId);
                // Look up doc name
                for (User u : MockData.users) {
                    if (u.getId().equals(doctorId)) {
                        a.setDoctorName(u.getFullName());
                        break;
                    }
                }
                a.setStatus("CONFIRMED");
                break;
            }
        }
        return new MockCall<>(null);
    }
}
