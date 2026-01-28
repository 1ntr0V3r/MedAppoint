package com.medappoint.app.api;

import com.medappoint.app.models.Appointment;
import com.medappoint.app.models.LoginRequest;
import com.medappoint.app.models.RegisterRequest;
import com.medappoint.app.models.User;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api/admin/users")
    Call<List<User>> getAllUsers();
    
    @GET("api/doctor/appointments/{doctorId}")
    Call<List<Appointment>> getDoctorAppointments(@Path("doctorId") Long doctorId);
    
    @GET("api/patient/appointments/{patientId}")
    Call<List<Appointment>> getPatientAppointments(@Path("patientId") Long patientId);
    
    @POST("api/auth/register")
    Call<User> register(@Body RegisterRequest request);
    
    @POST("api/auth/login")
    Call<User> login(@Body LoginRequest request);

    @GET("api/patient/doctors")
    Call<List<User>> getAllDoctors();

    @POST("api/patient/book")
    Call<Appointment> bookAppointment(@Body Appointment appointment);
}

