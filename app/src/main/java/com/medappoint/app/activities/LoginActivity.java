package com.medappoint.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.medappoint.app.R;
import com.medappoint.app.api.ApiService;
import com.medappoint.app.api.RetrofitClient;
import com.medappoint.app.models.LoginRequest;
import com.medappoint.app.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    // Déclaration des éléments de l'interface (Vue)
    private EditText champEmail;
    private EditText champMotDePasse;
    private Button boutonConnexion;
    private TextView lienInscription;
    private android.widget.ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Liaison avec les IDs du fichier XML
        imgLogo = findViewById(R.id.imgLogo);
        champEmail = findViewById(R.id.champEmail);
        champMotDePasse = findViewById(R.id.champMotDePasse);
        boutonConnexion = findViewById(R.id.boutonConnexion);
        lienInscription = findViewById(R.id.lienInscription);
        
        // Hidden Settings: Long press on logo to change server URL
        if(imgLogo != null) {
            imgLogo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    afficherBoiteDeDialogueServeur();
                    return true;
                }
            });
        }

        // Action quand on clique sur le bouton "Se connecter"
        boutonConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lancerLaConnexion();
            }
        });

        // Action quand on clique sur "S'inscrire"
        lienInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On navigue vers la page d'inscription (Activity)
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void afficherBoiteDeDialogueServeur() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title_server_settings));
        builder.setMessage(getString(R.string.msg_server_settings));

        final EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_URI);
        input.setText(RetrofitClient.getBaseUrl(this));
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.btn_save), new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                String newUrl = input.getText().toString().trim();
                RetrofitClient.setBaseUrl(LoginActivity.this, newUrl);
                Toast.makeText(LoginActivity.this, getString(R.string.server_ip_saved), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(getString(R.string.btn_cancel), new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**
     * Cette méthode récupère l'email et le mot de passe, vérifie qu'ils ne sont pas vides
     * et envoie la demande au serveur.
     */
    private void lancerLaConnexion() {
        String email = champEmail.getText().toString().trim();
        String motDePasse = champMotDePasse.getText().toString().trim();

        // Étape 1 : Vérification simple (Est-ce que l'utilisateur a écrit quelque chose ?)
        if (TextUtils.isEmpty(email)) {
            champEmail.setError(getString(R.string.error_email_required));
            champEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(motDePasse)) {
            champMotDePasse.setError(getString(R.string.error_password_required));
            champMotDePasse.requestFocus();
            return;
        }

        // Étape 2 : On montre à l'utilisateur que ça charge
        boutonConnexion.setEnabled(false);
        boutonConnexion.setText(getString(R.string.loading_login));

        // Étape 3 : On prépare les données pour l'envoi
        LoginRequest requeteConnexion = new LoginRequest(email, motDePasse);

        // Étape 4 : Appel au serveur via Retrofit
        ApiService serviceApi = RetrofitClient.getInstance(LoginActivity.this).getApiService();
        Call<User> appel = serviceApi.login(requeteConnexion);

        appel.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> appel, Response<User> reponse) {
                // Le serveur a répondu (succès ou erreur 404/401/500...)
                boutonConnexion.setEnabled(true);
                boutonConnexion.setText(getString(R.string.btn_login));

                if (reponse.isSuccessful() && reponse.body() != null) {
                    // C'est un succès ! On récupère l'utilisateur
                    User utilisateur = reponse.body();
                    String role = utilisateur.getRole();

                    // On décide vers quelle page aller selon le rôle (Admin, Docteur, Patient)
                    redirigerSelonRole(role, utilisateur);
                    
                } else {
                    // Le serveur a refusé la connexion (mauvais mot de passe ?)
                    String messageErreur = getString(R.string.error_login_failed);
                    try {
                        if (reponse.errorBody() != null) {
                            String errorBody = reponse.errorBody().string();
                            // Simple parsing of {"message":"..."}
                            if (errorBody.contains("message")) {
                                int start = errorBody.indexOf(":") + 2;
                                int end = errorBody.lastIndexOf("\"");
                                if (start < end) {
                                    messageErreur = errorBody.substring(start, end);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    Toast.makeText(LoginActivity.this, messageErreur, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> appel, Throwable erreur) {
                // Problème technique (Pas d'internet, serveur éteint...)
                boutonConnexion.setEnabled(true);
                boutonConnexion.setText(getString(R.string.btn_login));
                
                // Message amical et clair pour l'utilisateur
                Toast.makeText(LoginActivity.this, 
                        getString(R.string.error_network), 
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Méthode utilitaire pour gérer la navigation après une connexion réussie
     */
    private void redirigerSelonRole(String role, User utilisateur) {
        Intent intent;
        
        if ("ADMIN".equals(role)) {
            intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
        } else if ("DOCTOR".equals(role)) {
            intent = new Intent(LoginActivity.this, DoctorDashboardActivity.class);
        } else if ("PATIENT".equals(role)) {
            intent = new Intent(LoginActivity.this, PatientDashboardActivity.class);
        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.error_role_unknown) + role, Toast.LENGTH_SHORT).show();
            return;
        }

        // On passe les infos importantes à la page suivante
        intent.putExtra("USER_ID", utilisateur.getId());
        intent.putExtra("USER_NAME", utilisateur.getFullName());
        intent.putExtra("roleUtilisateur", utilisateur.getRole());
        intent.putExtra("emailUtilisateur", utilisateur.getEmail());
        
        startActivity(intent);
        finish(); // On ferme la page de login pour qu'on ne puisse pas revenir en arrière avec "Retour"
    }
}

