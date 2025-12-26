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
    // Liste pour afficher les utilisateurs
    private RecyclerView listeUtilisateurs;
    private UserAdapter adaptateurUtilisateur; // "Bridge" entre les données et la liste
    private List<User> mesUtilisateurs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialisation de la liste
        listeUtilisateurs = findViewById(R.id.listeUtilisateurs);
        mesUtilisateurs = new ArrayList<>();
        
        // Configuration simple
        adaptateurUtilisateur = new UserAdapter(mesUtilisateurs);
        listeUtilisateurs.setLayoutManager(new LinearLayoutManager(this));
        listeUtilisateurs.setAdapter(adaptateurUtilisateur);

        // Chargement des données depuis le serveur
        chargerTousLesUtilisateurs();
    }

    /**
     * Demande au serveur la liste de tout le monde (Docteurs, Patients...)
     */
    private void chargerTousLesUtilisateurs() {
        ApiService serviceApi = RetrofitClient.getInstance().getApiService();
        Call<List<User>> appel = serviceApi.getAllUsers();

        appel.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> appel, Response<List<User>> reponse) {
                if (reponse.isSuccessful() && reponse.body() != null) {
                    // On vide la liste actuelle et on met les nouveaux
                    mesUtilisateurs.clear();
                    mesUtilisateurs.addAll(reponse.body());
                    
                    // On dit à la liste de se rafraichir
                    adaptateurUtilisateur.updateUserList(mesUtilisateurs);
                } else {
                    Toast.makeText(AdminDashboardActivity.this, 
                            "Impossible de récupérer les utilisateurs.", 
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> appel, Throwable erreur) {
                Toast.makeText(AdminDashboardActivity.this, 
                        "Erreur réseau : Vérifiez que le Backend tourne.", 
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}

