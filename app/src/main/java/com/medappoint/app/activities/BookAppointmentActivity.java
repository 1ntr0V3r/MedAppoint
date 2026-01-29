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

        // Désactiver l'édition manuelle
        inputDate.setFocusable(false);
        inputDate.setClickable(true);
        inputTime.setFocusable(false);
        inputTime.setClickable(true);

        // Date Picker
        inputDate.setOnClickListener(v -> {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            new android.app.DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                // Formatting YYYY-MM-DD
                String selectedDate = String.format(java.util.Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                inputDate.setText(selectedDate);
            }, cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH), cal.get(java.util.Calendar.DAY_OF_MONTH)).show();
        });

        // Time Picker
        inputTime.setOnClickListener(v -> {
             java.util.Calendar cal = java.util.Calendar.getInstance();
             new android.app.TimePickerDialog(this, (view, hourOfDay, minute) -> {
                 // Formatting HH:MM
                 String selectedTime = String.format(java.util.Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                 inputTime.setText(selectedTime);
             }, cal.get(java.util.Calendar.HOUR_OF_DAY), cal.get(java.util.Calendar.MINUTE), true).show();
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

        // --- VALIDATION DES HORAIRES ---
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
            java.util.Date dateObj = sdf.parse(date);
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(dateObj);
            int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK); // Sun=1, Mon=2, ... Sat=7

            if (dayOfWeek == java.util.Calendar.SUNDAY) {
                Toast.makeText(this, "Le cabinet est fermé le Dimanche.", Toast.LENGTH_LONG).show();
                return;
            }

            int hour = Integer.parseInt(time.split(":")[0]);
            int minute = Integer.parseInt(time.split(":")[1]);

            if (dayOfWeek == java.util.Calendar.SATURDAY) {
                // Samedi: 09:00 - 12:00
                if (hour < 9 || hour > 12 || (hour == 12 && minute > 0)) {
                    Toast.makeText(this, "Samedi : Ouvert de 09h à 12h uniquement.", Toast.LENGTH_LONG).show();
                    return;
                }
            } else {
                // Lun - Ven: 09:00 - 16:00
                if (hour < 9 || hour > 16 || (hour == 16 && minute > 0)) {
                    Toast.makeText(this, "Horaires en semaine : 09h - 16h.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // -------------------------------

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
