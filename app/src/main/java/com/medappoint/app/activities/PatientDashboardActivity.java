package com.medappoint.app.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.medappoint.app.R;
import com.medappoint.app.adapters.PatientAppointmentAdapter;
import com.medappoint.app.api.ApiService;
import com.medappoint.app.api.RetrofitClient;
import com.medappoint.app.models.Appointment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class PatientDashboardActivity extends AppCompatActivity {
    private RecyclerView listeRendezVous;
    private PatientAppointmentAdapter adaptateurRendezVous;
    private List<Appointment> mesRendezVous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        // Liaison
        listeRendezVous = findViewById(R.id.listeRendezVous);
        mesRendezVous = new ArrayList<>();
        
        // Configuration de la liste
        adaptateurRendezVous = new PatientAppointmentAdapter(mesRendezVous);
        listeRendezVous.setLayoutManager(new LinearLayoutManager(this));
        listeRendezVous.setAdapter(adaptateurRendezVous);

        // Chargement des données
        chargerMesRendezVous();
    }

    private void chargerMesRendezVous() {
        ApiService serviceApi = RetrofitClient.getInstance().getApiService();
        // ID patient 3L en dur pour l'exemple
        Call<List<Appointment>> appel = serviceApi.getPatientAppointments(3L);

        appel.enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> appel, Response<List<Appointment>> reponse) {
                if (reponse.isSuccessful() && reponse.body() != null) {
                    mesRendezVous.clear();
                    mesRendezVous.addAll(reponse.body());
                    adaptateurRendezVous.updateAppointmentList(mesRendezVous);
                } else {
                    Toast.makeText(PatientDashboardActivity.this, 
                            "Erreur chargement RDV", 
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> appel, Throwable erreur) {
                Toast.makeText(PatientDashboardActivity.this, 
                        "Serveur non joignable.", 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
