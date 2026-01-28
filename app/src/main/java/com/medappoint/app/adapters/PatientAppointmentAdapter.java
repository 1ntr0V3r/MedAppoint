package com.medappoint.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.medappoint.app.R;
import com.medappoint.app.models.Appointment;
import java.util.List;

public class PatientAppointmentAdapter extends RecyclerView.Adapter<PatientAppointmentAdapter.VueRendezVousHolder> {
    private List<Appointment> listeRendezVous;

    public PatientAppointmentAdapter(List<Appointment> listeRendezVous) {
        this.listeRendezVous = listeRendezVous;
    }

    @NonNull
    @Override
    public VueRendezVousHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vue = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_patient_appointment, parent, false);
        return new VueRendezVousHolder(vue);
    }

    @Override
    public void onBindViewHolder(@NonNull VueRendezVousHolder holder, int position) {
        Appointment rdv = listeRendezVous.get(position);
        
        holder.texteDate.setText(rdv.getDate());
        holder.texteHeure.setText(rdv.getTime());
        holder.texteMotif.setText(rdv.getReason());
        // Gestion du statut (Couleurs dynamiques)
        String statut = rdv.getStatus(); // "PENDING", "CONFIRMED"
        holder.texteStatut.setText(statut);
        
        if ("CONFIRMED".equalsIgnoreCase(statut)) {
            holder.texteStatut.setTextColor(android.graphics.Color.GREEN);
            holder.texteDocteur.setText("Dr. " + rdv.getDoctorName());
        } else {
            holder.texteStatut.setTextColor(android.graphics.Color.parseColor("#FF9800")); // Orange
            holder.texteDocteur.setText("En attente d'attribution");
        }

        holder.itemView.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(v.getContext(), com.medappoint.app.activities.AppointmentDetailActivity.class);
            intent.putExtra("APPOINTMENT", rdv);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listeRendezVous != null ? listeRendezVous.size() : 0;
    }

    public void updateAppointmentList(List<Appointment> nouvelleListe) {
        this.listeRendezVous = nouvelleListe;
        notifyDataSetChanged();
    }

    static class VueRendezVousHolder extends RecyclerView.ViewHolder {
        TextView texteDate;
        TextView texteHeure;
        TextView texteDocteur;
        TextView texteMotif;
        TextView texteStatut;

        VueRendezVousHolder(@NonNull View itemView) {
            super(itemView);
            texteDate = itemView.findViewById(R.id.textViewDate);
            texteHeure = itemView.findViewById(R.id.textViewTime);
            texteDocteur = itemView.findViewById(R.id.textViewDoctorName);
            texteMotif = itemView.findViewById(R.id.textViewReason);
            texteStatut = itemView.findViewById(R.id.textViewStatus);
        }
    }
}

