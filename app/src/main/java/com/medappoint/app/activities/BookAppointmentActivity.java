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

    private Spinner spinnerDoctors;
    private EditText inputDate, inputTime, inputReason;
    private Button btnBook;
    
    private List<User> doctorsList;
    private long patientId;
    private String patientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        // Récupérer les infos du patient
        patientId = getIntent().getLongExtra("USER_ID", -1);
        patientName = getIntent().getStringExtra("USER_NAME");

        spinnerDoctors = findViewById(R.id.spinnerDoctors);
        inputDate = findViewById(R.id.inputDate);
        inputTime = findViewById(R.id.inputTime);
        inputReason = findViewById(R.id.inputReason);
        btnBook = findViewById(R.id.btnBook);

        loadDoctors();

        btnBook.setOnClickListener(v -> bookAppointment());
    }

    private void loadDoctors() {
        ApiService api = RetrofitClient.getInstance(this).getApiService();
        api.getAllDoctors().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    doctorsList = response.body();
                    ArrayAdapter<User> adapter = new ArrayAdapter<>(BookAppointmentActivity.this,
                            android.R.layout.simple_spinner_item, doctorsList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDoctors.setAdapter(adapter);
                } else {
                    Toast.makeText(BookAppointmentActivity.this, "Impossible de charger les médecins", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(BookAppointmentActivity.this, "Erreur réseau: Médecins", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bookAppointment() {
        User selectedDoctor = (User) spinnerDoctors.getSelectedItem();
        if (selectedDoctor == null) {
            Toast.makeText(this, "Veuillez choisir un médecin", Toast.LENGTH_SHORT).show();
            return;
        }

        String date = inputDate.getText().toString();
        String time = inputTime.getText().toString();
        String reason = inputReason.getText().toString();

        if (date.isEmpty() || time.isEmpty() || reason.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        Appointment app = new Appointment();
        app.setDate(date);
        app.setTime(time);
        app.setReason(reason);
        app.setPatientId(patientId);
        app.setPatientName(patientName != null ? patientName : "Patient");
        app.setDoctorId(selectedDoctor.getId());
        app.setDoctorName(selectedDoctor.getFullName());
        
        // Appel API
        ApiService api = RetrofitClient.getInstance(this).getApiService();
        api.bookAppointment(app).enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(BookAppointmentActivity.this, "Rendez-vous confirmé!", Toast.LENGTH_LONG).show();
                    finish(); // Retour au dashboard
                } else {
                    Toast.makeText(BookAppointmentActivity.this, "Erreur lors de la réservation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable t) {
                Toast.makeText(BookAppointmentActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
