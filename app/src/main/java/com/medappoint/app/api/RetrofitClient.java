package com.medappoint.app.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String PREFS_NAME = "MedAppointPrefs";
    private static final String KEY_SERVER_URL = "server_url";
    // Valeur par défaut (à changer via l'app ou si nécessaire)
    private static final String DEFAULT_URL = "http://192.168.137.1:8080/";

    private static RetrofitClient instance_unique = null;
    private ApiService serviceApi;
    private static String currentUrl = null;

    // Constructeur privé
    private RetrofitClient(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        serviceApi = retrofit.create(ApiService.class);
    }

    // Méthode pour obtenir l'instance unique (Singleton) avec Context pour charger l'URL
    public static synchronized RetrofitClient getInstance(android.content.Context context) {
        android.content.SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE);
        String savedUrl = prefs.getString(KEY_SERVER_URL, DEFAULT_URL);

        // Sécurité : Retrofit exige un '/' à la fin
        if (!savedUrl.endsWith("/")) {
            savedUrl += "/";
        }

        // Si l'instance n'existe pas OU si l'URL a changé, on recrée tout
        if (instance_unique == null || !savedUrl.equals(currentUrl)) {
            currentUrl = savedUrl;
            instance_unique = new RetrofitClient(currentUrl);
        }
        return instance_unique;
    }

    // Méthode statique pour changer l'URL et forcer la reconnexion
    public static void setBaseUrl(android.content.Context context, String newUrl) {
        android.content.SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_SERVER_URL, newUrl).apply();
        // On force la recréation au prochain appel de getInstance
        instance_unique = null;
    }

    // Récupérer l'URL actuelle pour l'afficher dans les réglages
    public static String getBaseUrl(android.content.Context context) {
        android.content.SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE);
        return prefs.getString(KEY_SERVER_URL, DEFAULT_URL);
    }

    // Récupérer le service pour faire les appels
    public ApiService getApiService() {
        return serviceApi;
    }
}

