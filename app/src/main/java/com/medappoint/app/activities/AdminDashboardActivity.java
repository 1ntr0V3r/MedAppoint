package com.medappoint.app.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.medappoint.app.R;
import com.medappoint.app.adapters.UserAdapter;
import com.medappoint.app.api.ApiService;
import com.medappoint.app.api.RetrofitClient;
import com.medappoint.app.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {
    private RecyclerView listePrincipale;
    private android.widget.Button btnTabRequests, btnTabDoctors, btnTabPatients;
    
    private com.medappoint.app.adapters.AdminPendingAdapter pendingAdapter;
    private com.medappoint.app.adapters.UserAdapter userAdapter;
    
    // Data
    private List<com.medappoint.app.models.Appointment> pendingList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    private List<User> doctorsList = new ArrayList<>(); // For assignment
    
    private String currentTab = "PENDING"; // PENDING, DOCTORS, PATIENTS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        listePrincipale = findViewById(R.id.listePrincipale);
        btnTabRequests = findViewById(R.id.btnTabRequests);
        btnTabDoctors = findViewById(R.id.btnTabDoctors);
        btnTabPatients = findViewById(R.id.btnTabPatients);
        
        listePrincipale.setLayoutManager(new LinearLayoutManager(this));
        
        // Setup Adatapters
        pendingAdapter = new com.medappoint.app.adapters.AdminPendingAdapter(pendingList, this::showAssignDialog);
        userAdapter = new com.medappoint.app.adapters.UserAdapter(userList); // Reuse existing user adapter
        
        // Listeners
        btnTabRequests.setOnClickListener(v -> switchTab("PENDING"));
        btnTabDoctors.setOnClickListener(v -> switchTab("DOCTORS"));
        btnTabPatients.setOnClickListener(v -> switchTab("PATIENTS"));
        
        // Load initial data
        loadDoctorsForAssignment(); // Need this for the dialog
        switchTab("PENDING");
    }

    private void switchTab(String tab) {
        currentTab = tab;
        resetTabColors();
        if ("PENDING".equals(tab)) {
            btnTabRequests.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#FF9800")));
            listePrincipale.setAdapter(pendingAdapter);
            loadPendingAppointments();
        } else if ("DOCTORS".equals(tab)) {
            btnTabDoctors.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#4CAF50")));
            listePrincipale.setAdapter(userAdapter);
            loadUsers("DOCTOR");
        } else {
            btnTabPatients.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#2196F3")));
            listePrincipale.setAdapter(userAdapter);
            loadUsers("PATIENT");
        }
    }
    
    private void resetTabColors() {
        int gray = android.graphics.Color.LTGRAY;
        btnTabRequests.setBackgroundTintList(android.content.res.ColorStateList.valueOf(gray));
        btnTabDoctors.setBackgroundTintList(android.content.res.ColorStateList.valueOf(gray));
        btnTabPatients.setBackgroundTintList(android.content.res.ColorStateList.valueOf(gray));
    }

    private void loadPendingAppointments() {
         ApiService api = RetrofitClient.getInstance(this).getApiService();
         api.getPendingAppointments().enqueue(new Callback<List<com.medappoint.app.models.Appointment>>() {
             @Override
             public void onResponse(Call<List<com.medappoint.app.models.Appointment>> call, Response<List<com.medappoint.app.models.Appointment>> response) {
                 if (response.isSuccessful() && response.body() != null) {
                     pendingList = response.body();
                     pendingAdapter.updateList(pendingList);
                 }
             }
             @Override
             public void onFailure(Call<List<com.medappoint.app.models.Appointment>> call, Throwable t) {
                 Toast.makeText(AdminDashboardActivity.this, "Erreur chargement demandes", Toast.LENGTH_SHORT).show();
             }
         });
    }

    private void loadUsers(String roleFilter) {
         ApiService api = RetrofitClient.getInstance(this).getApiService();
         api.getAllUsers().enqueue(new Callback<List<User>>() {
             @Override
             public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                 if (response.isSuccessful() && response.body() != null) {
                     userList.clear();
                     for (User u : response.body()) {
                         if (u.getRole().equalsIgnoreCase(roleFilter)) {
                             userList.add(u);
                         }
                     }
                     userAdapter.updateUserList(userList);
                 }
             }
             @Override
             public void onFailure(Call<List<User>> call, Throwable t) { }
         });
    }
    
    private void loadDoctorsForAssignment() {
        // Pre-load doctors so they are ready for the spinner/dialog
        ApiService api = RetrofitClient.getInstance(this).getApiService();
        api.getAllDoctors().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                 if(response.isSuccessful()) doctorsList = response.body();
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {}
        });
    }

    private void showAssignDialog(com.medappoint.app.models.Appointment app) {
        if (doctorsList == null || doctorsList.isEmpty()) {
            Toast.makeText(this, "Aucun médecin disponible", Toast.LENGTH_SHORT).show();
            // Try reloading
            loadDoctorsForAssignment();
            return;
        }

        String[] docNames = new String[doctorsList.size()];
        for (int i=0; i<doctorsList.size(); i++) {
            docNames[i] = doctorsList.get(i).getFullName();
        }

        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Assigner un médecin pour " + app.getPatientName())
            .setItems(docNames, (dialog, which) -> {
                User selectedDoc = doctorsList.get(which);
                assignDoctor(app, selectedDoc);
            })
            .show();
    }
    
    private void assignDoctor(com.medappoint.app.models.Appointment app, User doctor) {
        ApiService api = RetrofitClient.getInstance(this).getApiService();
        api.assignDoctor(app.getId(), doctor.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AdminDashboardActivity.this, "Médecin assigné !", Toast.LENGTH_SHORT).show();
                    loadPendingAppointments(); // Refresh list
                } else {
                     Toast.makeText(AdminDashboardActivity.this, "Erreur assignation", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AdminDashboardActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


