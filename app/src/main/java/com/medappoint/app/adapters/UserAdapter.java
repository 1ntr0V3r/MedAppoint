package com.medappoint.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.medappoint.app.R;
import com.medappoint.app.models.User;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.VueUtilisateurHolder> {
    
    // Liste des données (les utilisateurs) fournie par l'activité
    private List<User> listeUtilisateurs;

    // Constructeur : on reçoit la liste au début
    public UserAdapter(List<User> listeUtilisateurs) {
        this.listeUtilisateurs = listeUtilisateurs;
    }

    // Étape 1 : Création de la "boîte" vide (la vue d'une ligne)
    @NonNull
    @Override
    public VueUtilisateurHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // On "gonfle" (inflate) le fichier XML item_user.xml pour en faire une vraie vue Java
        View vue = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new VueUtilisateurHolder(vue);
    }

    // Étape 2 : Remplissage de la boîte avec les données
    @Override
    public void onBindViewHolder(@NonNull VueUtilisateurHolder holder, int position) {
        // On récupère l'utilisateur à la position demandée
        User utilisateur = listeUtilisateurs.get(position);
        
        // On remplit les textes
        holder.texteNom.setText(utilisateur.getFullName());
        holder.texteEmail.setText(utilisateur.getEmail());
        holder.texteRole.setText(utilisateur.getRole());
    }

    @Override
    public int getItemCount() {
        return listeUtilisateurs != null ? listeUtilisateurs.size() : 0;
    }

    // Méthode pour mettre à jour la liste quand les données changent
    public void updateUserList(List<User> nouvelleListe) {
        this.listeUtilisateurs = nouvelleListe;
        notifyDataSetChanged(); // "Hey RecyclerView, tout a changé, redessine-toi !"
    }

    // Classe interne : Elle garde les références vers les éléments graphiques (pour aller vite)
    static class VueUtilisateurHolder extends RecyclerView.ViewHolder {
        TextView texteNom;
        TextView texteEmail;
        TextView texteRole;

        VueUtilisateurHolder(@NonNull View itemView) {
            super(itemView);
            // On fait le lien avec les IDs du fichier item_user.xml
            texteNom = itemView.findViewById(R.id.textViewName);
            texteEmail = itemView.findViewById(R.id.textViewEmail);
            texteRole = itemView.findViewById(R.id.textViewRole);
        }
    }
}

