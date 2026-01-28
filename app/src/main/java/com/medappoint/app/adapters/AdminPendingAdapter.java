package com.medappoint.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.medappoint.app.R;
import com.medappoint.app.models.Appointment;
import java.util.List;

public class AdminPendingAdapter extends RecyclerView.Adapter<AdminPendingAdapter.PendingHolder> {

    private List<Appointment> appointments;
    private OnAssignClickListener assignListener;

    public interface OnAssignClickListener {
        void onAssignClick(Appointment app);
    }

    public AdminPendingAdapter(List<Appointment> appointments, OnAssignClickListener listener) {
        this.appointments = appointments;
        this.assignListener = listener;
    }

    @NonNull
    @Override
    public PendingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_appointment, parent, false);
        return new PendingHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingHolder holder, int position) {
        Appointment app = appointments.get(position);
        
        holder.tvPatient.setText(app.getPatientName() + "(" + app.getPatientAge() + "a, " + app.getPatientSex() + ")");
        holder.tvReason.setText("Motif: " + app.getReason());
        holder.tvDate.setText("Le " + app.getDate() + " à " + app.getTime());
        
        holder.btnAssign.setOnClickListener(v -> {
            if (assignListener != null) {
                assignListener.onAssignClick(app);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }
    
    public void updateList(List<Appointment> newList) {
        this.appointments = newList;
        notifyDataSetChanged();
    }

    static class PendingHolder extends RecyclerView.ViewHolder {
        TextView tvPatient, tvReason, tvDate;
        Button btnAssign;

        public PendingHolder(@NonNull View itemView) {
            super(itemView);
            tvPatient = itemView.findViewById(R.id.tvPatient);
            tvReason = itemView.findViewById(R.id.tvReason);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnAssign = itemView.findViewById(R.id.btnAssign);
        }
    }
}
