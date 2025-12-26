package com.medappoint.app.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // Pour l'émulateur Android, on utilise 10.0.2.2 qui pointe vers le localhost de votre PC.
    // Si vous testez sur un VRAI téléphone (Samsung, Xiaomi...), remplacez par l'IP locale de votre PC (ex: "http://192.168.1.15:8080/")
    // IP locale pour test sur téléphone réel (selon votre config ipconfig)
    private static final String ADRESSE_SERVEUR = "http://26.10.0.108:8080/";
    
    private static RetrofitClient instance_unique = null;
    private ApiService serviceApi;

    // Constructeur privé : Personne ne peut créer un 'new RetrofitClient()' à l'extérieur
    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ADRESSE_SERVEUR)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        serviceApi = retrofit.create(ApiService.class);
    }

    // Méthode pour obtenir l'instance unique (Singleton)
    public static synchronized RetrofitClient getInstance() {
        if (instance_unique == null) {
            instance_unique = new RetrofitClient();
        }
        return instance_unique;
    }

    // Récupérer le service pour faire les appels
    public ApiService getApiService() {
        return serviceApi;
    }
}

