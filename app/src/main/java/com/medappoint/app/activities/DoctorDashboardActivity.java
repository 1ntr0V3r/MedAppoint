package com.medappoint.app.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.medappoint.app.R;
import com.medappoint.app.adapters.DoctorAppointmentAdapter;
import com.medappoint.app.api.ApiService;
import com.medappoint.app.api.RetrofitClient;
import com.medappoint.app.models.Appointment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class DoctorDashboardActivity extends AppCompatActivity {
    // La liste qui contient les rendez-vous
    private RecyclerView listeRendezVous;
    // L'adaptateur qui transforme les données en éléments visuels
    private DoctorAppointmentAdapter adaptateurRendezVous;
    // Les données (liste des RDV)
    private List<Appointment> mesRendezVous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        // Liaison XML
        listeRendezVous = findViewById(R.id.listeRendezVous); // ID corrigé en français
        mesRendezVous = new ArrayList<>();
        
        // Configuration
        adaptateurRendezVous = new DoctorAppointmentAdapter(mesRendezVous);
        listeRendezVous.setLayoutManager(new LinearLayoutManager(this));
        listeRendezVous.setAdapter(adaptateurRendezVous);

        // Charger les données
        chargerLePlanning();
    }

    private void chargerLePlanning() {
        ApiService serviceApi = RetrofitClient.getInstance(DoctorDashboardActivity.this).getApiService();
        
        // Récupérer l'ID depuis la connexion
        long userId = getIntent().getLongExtra("USER_ID", -1);
        if (userId == -1) {
             Toast.makeText(this, "Erreur: ID Docteur introuvable", Toast.LENGTH_SHORT).show();
             return;
        }

        Call<List<Appointment>> appel = serviceApi.getDoctorAppointments(userId);

        appel.enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> appel, Response<List<Appointment>> reponse) {
                if (reponse.isSuccessful() && reponse.body() != null) {
                    mesRendezVous.clear();
                    mesRendezVous.addAll(reponse.body());
                    adaptateurRendezVous.updateAppointmentList(mesRendezVous);
                    
                    // Mise à jour de l'en-tête (Date du jour et nombre de patients)
                    android.widget.TextView tvHeader = findViewById(R.id.tvPlanningHeader);
                    String dateJour = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(new java.util.Date());
                    tvHeader.setText("Planning du : " + dateJour + " (" + mesRendezVous.size() + " Patients)");
                    
                } else {
                    Toast.makeText(DoctorDashboardActivity.this, 
                            "Erreur de chargement.", 
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> appel, Throwable erreur) {
                Toast.makeText(DoctorDashboardActivity.this, 
                        "Erreur réseau.", 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
