package com.medappoint.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.medappoint.app.R;
import com.medappoint.app.api.ApiService;
import com.medappoint.app.api.RetrofitClient;
import com.medappoint.app.models.Appointment;
import com.medappoint.app.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class BookAppointmentActivity extends AppCompatActivity {

    private EditText inputDate, inputTime, inputReason, inputAge, inputSex;
    private Button btnBook;
    
    private long patientId;
    private String patientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        // Récupérer les infos du patient
        patientId = getIntent().getLongExtra("USER_ID", -1);
        patientName = getIntent().getStringExtra("USER_NAME");

        inputAge = findViewById(R.id.inputAge);
        inputSex = findViewById(R.id.inputSex);
        inputDate = findViewById(R.id.inputDate);
        inputTime = findViewById(R.id.inputTime);
        inputReason = findViewById(R.id.inputReason);
        btnBook = findViewById(R.id.btnBook);

        // Formatage automatique pour la Date (YYYY-MM-DD)
        inputDate.addTextChangedListener(new android.text.TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "YYYYMMDD";
            private java.util.Calendar cal = java.util.Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        String yyyymmdd = ddmmyyyy.substring(clean.length());
                       clean = clean + yyyymmdd;
                    } else {
                       int year  = Integer.parseInt(clean.substring(0,4));
                       int mon  = Integer.parseInt(clean.substring(4,6));
                       int day  = Integer.parseInt(clean.substring(6,8));

                       mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                       cal.set(java.util.Calendar.MONTH, mon-1);
                       year = (year<1900)?1900:(year>2100)?2100:year;
                       cal.set(java.util.Calendar.YEAR, year);
                       
                       day = (day > cal.getActualMaximum(java.util.Calendar.DATE))? cal.getActualMaximum(java.util.Calendar.DATE):day;
                       clean = String.format("%02d%02d%02d",year, mon, day);
                    }

                    clean = String.format("%s-%s-%s", clean.substring(0, 4),
                            clean.substring(4, 6),
                            clean.substring(6, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    inputDate.setText(current);
                    inputDate.setSelection(sel < current.length() ? sel : current.length());
                }
            }
            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
        
        // Formatage automatique pour l'Heure (HH:MM)
        inputTime.addTextChangedListener(new android.text.TextWatcher() {
             private String current = "";
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
             
             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
                 if (!s.toString().equals(current)) {
                     String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                     String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                     int cl = clean.length();
                     int sel = cl;
                     for (int i = 2; i <= cl && i < 4; i += 2) {
                         sel++;
                     }
                     if (clean.equals(cleanC)) sel--;

                     if (clean.length() < 4){
                        clean = clean + "0000".substring(clean.length());
                     } else {
                        clean = clean.substring(0,4);
                     }
                     
                     clean = String.format("%s:%s", clean.substring(0, 2), clean.substring(2, 4));

                     sel = sel < 0 ? 0 : sel;
                     current = clean;
                     inputTime.setText(current);
                     inputTime.setSelection(sel < current.length() ? sel : current.length());
                 }
             }

             @Override
             public void afterTextChanged(android.text.Editable s) {}
        });

        btnBook.setOnClickListener(v -> bookAppointment());
    }

    private void bookAppointment() {
        String date = inputDate.getText().toString();
        String time = inputTime.getText().toString();
        String reason = inputReason.getText().toString();
        String ageStr = inputAge.getText().toString();
        String sex = inputSex.getText().toString();

        if (date.isEmpty() || time.isEmpty() || reason.isEmpty() || ageStr.isEmpty() || sex.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        Appointment app = new Appointment();
        app.setDate(date);
        app.setTime(time);
        app.setReason(reason);
        app.setPatientId(patientId);
        app.setPatientName(patientName != null ? patientName : "Patient");
        
        try {
            app.setPatientAge(Integer.parseInt(ageStr));
        } catch (NumberFormatException e) {
            app.setPatientAge(0);
        }
        app.setPatientSex(sex);
        
        // Pas de médecin sélectionné
        app.setDoctorId(null);
        app.setDoctorName(null);
        
        // Appel API
        ApiService api = RetrofitClient.getInstance(this).getApiService();
        api.bookAppointment(app).enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(BookAppointmentActivity.this, "Demande envoyée au secrétariat !", Toast.LENGTH_LONG).show();
                    finish(); // Retour au dashboard
                } else {
                    Toast.makeText(BookAppointmentActivity.this, "Erreur lors de la demande", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable t) {
                Toast.makeText(BookAppointmentActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
