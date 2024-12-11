package com.kyoxsu.logique;

// Make sure to add Internet permission in AndroidManifest.xml
// <uses-permission android:name="android.permission.INTERNET" />

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.kyoxsu.testandroidstudio.MainActivity2;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkManager {
    public static String baseUrl; // Replace with your API URL
    private final ExecutorService executorService;
    private final Handler mainHandler;

    public NetworkManager()
    {
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void fetchData(JSONObject requestBody, String method, String route, String token, final NetworkCallback callback)
    {
        executorService.execute(() -> {
            HttpURLConnection connection = null;
            try {
                // Create connection
                URL url = new URL(baseUrl + route);
                connection = (HttpURLConnection) url.openConnection();

                // Setup request
                connection.setRequestMethod(method);
                connection.setRequestProperty("Content-Type", "application/json");
                if (token != null)
                {
                    // TODO : Vérifier pourquoi il ne faut pas rajouter Bearer devant le token
                    connection.setRequestProperty("Authorization", token);
                }
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                if (requestBody != null)
                {
                    connection.setDoOutput(true);
                    try (BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(connection.getOutputStream()))) {
                        writer.write(requestBody.toString());
                        writer.flush();
                    }
                }

                // Check response code
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read response
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream())
                    );
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    //----------------------------------------------------------

                    // Parse JSON response
                    final JSONObject jsonResponse = new JSONObject(response.toString());

                    // Return result on main thread
                    mainHandler.post(() -> callback.onSuccess(jsonResponse));
                } else {
                    // Handle error response
                    mainHandler.post(() ->
                            callback.onError("Server returned code: " + responseCode)
                    );
                }
            } catch (IOException | JSONException e) {
                final String errorMessage = e.getMessage();
                mainHandler.post(() -> callback.onError(errorMessage));
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

    // Cleanup method
    public void cleanup() {
        executorService.shutdown();
    }

    // Callback interface
    public interface NetworkCallback
    {
        void onSuccess(JSONObject response);
        void onError(String error);
    }

    public static void setBaseUrl(String trois, String quatre)
    {
        // http://192.168.210.87:3000
        baseUrl = "http://192.168."+trois+"."+quatre+":3000";
    }
}