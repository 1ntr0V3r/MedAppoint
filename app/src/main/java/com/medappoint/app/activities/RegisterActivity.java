package com.medappoint.app.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.medappoint.app.R;
import com.medappoint.app.api.ApiService;
import com.medappoint.app.api.RetrofitClient;
import com.medappoint.app.models.RegisterRequest;
import com.medappoint.app.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    // Déclaration des champs du formulaire
    private EditText champNomComplet;
    private EditText champEmail;
    private EditText champMotDePasse;
    private Spinner selecteurRole;
    private Button boutonInscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Liaison avec l'interface graphique (XML)
        champNomComplet = findViewById(R.id.champNomComplet);
        champEmail = findViewById(R.id.champEmail);
        champMotDePasse = findViewById(R.id.champMotDePasse);
        selecteurRole = findViewById(R.id.selecteurRole);
        boutonInscription = findViewById(R.id.boutonInscription);

        // Configuration du menu déroulant (Spinner) pour choisir son rôle
        ArrayAdapter<CharSequence> adaptateurPourListe = ArrayAdapter.createFromResource(
                this,
                R.array.roles_array, // Liste définie dans strings.xml
                android.R.layout.simple_spinner_item);
        adaptateurPourListe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selecteurRole.setAdapter(adaptateurPourListe);

        // Clic sur le bouton valider
        boutonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lancerInscription();
            }
        });
    }

    /**
     * Vérifie les informations et envoie la demande d'inscription au serveur
     */
    private void lancerInscription() {
        String nomComplet = champNomComplet.getText().toString().trim();
        String email = champEmail.getText().toString().trim();
        String motDePasse = champMotDePasse.getText().toString().trim();
        String roleChoisi = selecteurRole.getSelectedItem().toString();

        // 1. Vérifications de sécurité (Validation)
        if (TextUtils.isEmpty(nomComplet)) {
            champNomComplet.setError(getString(R.string.error_name_required));
            return;
        }

        if (TextUtils.isEmpty(email)) {
            champEmail.setError(getString(R.string.error_email_required));
            return;
        }

        // On vérifie le format de l'email (ex: test@gmail.com)
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            champEmail.setError(getString(R.string.error_email_invalid));
            return;
        }

        if (TextUtils.isEmpty(motDePasse)) {
            champMotDePasse.setError(getString(R.string.error_password_required));
            return;
        }

        if (motDePasse.length() < 6) {
            champMotDePasse.setError(getString(R.string.error_password_length));
            return;
        }

        // 2. On désactive le bouton pour éviter le double-clic
        boutonInscription.setEnabled(false);
        boutonInscription.setText(getString(R.string.loading_register));

        // 3. Préparation des données
        RegisterRequest requeteInscription = new RegisterRequest(nomComplet, email, motDePasse, roleChoisi);

        // 4. Envoi au serveur
        ApiService serviceApi = RetrofitClient.getInstance().getApiService();
        Call<User> appel = serviceApi.register(requeteInscription);

        appel.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> appel, Response<User> reponse) {
                boutonInscription.setEnabled(true);
                boutonInscription.setText(getString(R.string.btn_register));

                if (reponse.isSuccessful() && reponse.body() != null) {
                    Toast.makeText(RegisterActivity.this,
                            getString(R.string.success_register),
                            Toast.LENGTH_LONG).show();

                    // On ferme cette page pour revenir à la connexion
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this,
                            getString(R.string.error_register),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> appel, Throwable erreur) {
                boutonInscription.setEnabled(true);
                boutonInscription.setText(getString(R.string.btn_register));

                Toast.makeText(RegisterActivity.this,
                        getString(R.string.error_network),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
