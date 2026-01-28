package com.medappoint.app.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.medappoint.app.R;
import com.medappoint.app.models.Appointment;

public class AppointmentDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);

        Appointment app = (Appointment) getIntent().getSerializableExtra("APPOINTMENT");

        TextView tvStatus = findViewById(R.id.tvStatus);
        TextView tvPatientInfo = findViewById(R.id.tvPatientInfo);
        TextView tvDateTime = findViewById(R.id.tvDateTime);
        TextView tvReason = findViewById(R.id.tvReason);
        TextView tvDoctor = findViewById(R.id.tvDoctor);

        if (app != null) {
            tvStatus.setText("STATUT : " + (app.getStatus() != null ? app.getStatus() : "INCONNU"));
            
            if ("CONFIRMED".equalsIgnoreCase(app.getStatus())) {
                tvStatus.setTextColor(Color.GREEN);
            } else {
                tvStatus.setTextColor(Color.parseColor("#FF9800")); // Orange
            }

            String patientInfo = (app.getPatientName() != null ? app.getPatientName() : "Moi") +
                    " (" + app.getPatientAge() + " ans, " + app.getPatientSex() + ")";
            tvPatientInfo.setText(patientInfo);

            tvDateTime.setText("Le " + app.getDate() + " à " + app.getTime());
            tvReason.setText("Motif: " + app.getReason());

            if (app.getDoctorName() != null && !app.getDoctorName().isEmpty()) {
                tvDoctor.setText("Dr. " + app.getDoctorName());
                tvDoctor.setTextColor(Color.BLACK);
            } else {
                tvDoctor.setText("En attente d'attribution par le secrétariat");
                tvDoctor.setTextColor(Color.GRAY);
            }
        }

        findViewById(R.id.btnClose).setOnClickListener(v -> finish());
    }
}
