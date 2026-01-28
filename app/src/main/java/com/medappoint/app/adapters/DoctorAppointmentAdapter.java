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

public class DoctorAppointmentAdapter extends RecyclerView.Adapter<DoctorAppointmentAdapter.VueRendezVousHolder> {
    
    // Liste des rendez-vous
    private List<Appointment> listeRendezVous;

    public DoctorAppointmentAdapter(List<Appointment> listeRendezVous) {
        this.listeRendezVous = listeRendezVous;
    }

    // Création de la vue pour un élément de la liste
    @NonNull
    @Override
    public VueRendezVousHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vue = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor_appointment, parent, false);
        return new VueRendezVousHolder(vue);
    }

    // Mise à jour de la vue avec les infos du RDV
    // Mise à jour de la vue avec les infos du RDV
    @Override
    public void onBindViewHolder(@NonNull VueRendezVousHolder holder, int position) {
        Appointment leRendezVous = listeRendezVous.get(position);
        
        String infoPatient = leRendezVous.getPatientName() + ", " + 
                             leRendezVous.getPatientAge() + " ans, " + 
                             leRendezVous.getPatientSex();
        
        holder.textePatient.setText(infoPatient);
        holder.texteHeure.setText(leRendezVous.getTime());
        holder.texteMotif.setText(leRendezVous.getReason());
        holder.texteDate.setText(leRendezVous.getDate());
        
        holder.btnComplete.setOnClickListener(v -> {
             com.medappoint.app.api.ApiService api = com.medappoint.app.api.RetrofitClient.getInstance(v.getContext()).getApiService();
             api.completeAppointment(leRendezVous.getId()).enqueue(new retrofit2.Callback<Void>() {
                 @Override
                 public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                     if (response.isSuccessful()) {
                         android.widget.Toast.makeText(v.getContext(), "Consultation terminée", android.widget.Toast.LENGTH_SHORT).show();
                         listeRendezVous.remove(position);
                         notifyItemRemoved(position);
                         notifyItemRangeChanged(position, listeRendezVous.size());
                     }
                 }
                 @Override
                 public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                      android.widget.Toast.makeText(v.getContext(), "Erreur réseau", android.widget.Toast.LENGTH_SHORT).show();
                 }
             });
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

    // Le ViewHolder qui tient les références aux TextViews
    static class VueRendezVousHolder extends RecyclerView.ViewHolder {
        TextView textePatient;
        TextView texteHeure;
        TextView texteMotif;
        TextView texteDate;
        android.widget.Button btnComplete;

        VueRendezVousHolder(@NonNull View itemView) {
            super(itemView);
            textePatient = itemView.findViewById(R.id.textViewPatientName);
            texteHeure = itemView.findViewById(R.id.textViewTime);
            texteMotif = itemView.findViewById(R.id.textViewReason);
            texteDate = itemView.findViewById(R.id.textViewDate);
            btnComplete = itemView.findViewById(R.id.btnComplete);
        }
    }
}

