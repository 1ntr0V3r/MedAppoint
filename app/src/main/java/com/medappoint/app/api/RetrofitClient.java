package com.medappoint.app.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String PREFS_NAME = "MedAppointPrefs";
    private static final String KEY_SERVER_URL = "server_url";
    // Valeur par défaut (à changer via l'app ou si nécessaire)
    private static final String DEFAULT_URL = "https://098e5ece4e4b781f-196-70-217-107.serveousercontent.com/";

    private static RetrofitClient instance_unique = null;
    private ApiService serviceApi;
    private static String currentUrl = null;

    // Constructeur privé
    private RetrofitClient(String baseUrl) {
        
        retrofit2.Retrofit.Builder builder = new retrofit2.Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create());

        // Use Unsafe HttpClient to avoid SSL errors with Serveo/Localtunnel
        builder.client(getUnsafeOkHttpClient());
        
        Retrofit retrofit = builder.build();
        serviceApi = retrofit.create(ApiService.class);
    }
    
    // --- BYPASS SSL SECURITY (DEV ONLY) ---
    private static okhttp3.OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[] {
                new javax.net.ssl.X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {}
                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {}
                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[]{}; }
                }
            };

            // Install the all-trusting trust manager
            final javax.net.ssl.SSLContext sslContext = javax.net.ssl.SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (javax.net.ssl.X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
            
            // Timeout settings (Serveo can be slow)
            builder.connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS);
            builder.readTimeout(30, java.util.concurrent.TimeUnit.SECONDS);

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        // --- OFFLINE MODE FOR DEMO (PROFESSOR) ---
        // Return the fake service instead of the real one
        // If you want to use the real server, change this boolean to false
        boolean useOfflineMode = false; 
        
        if (useOfflineMode) {
             return new com.medappoint.app.mocks.MockApiService();
        }

        return serviceApi;
    }
}

