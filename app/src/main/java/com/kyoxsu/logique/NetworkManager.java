package com.kyoxsu.logique;

import android.os.StrictMode;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
//------------------------------------------------------------------------------
/**
 * Cette classe permet de gérer toutes les requêtes http
 */
//------------------------------------------------------------------------------
public class NetworkManager
{
    public static String baseUrl; // Replace with your API URL

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public NetworkManager() {}

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public JSONObject fetchDataSync(JSONObject requestBody, String method, String route, String token) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        HttpURLConnection connection = null;
        try {
            URL url = new URL(baseUrl + route);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json");
            if (token != null) {
                connection.setRequestProperty("Authorization", token);
            }
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            // ---
            if (requestBody != null)
            {
                connection.setDoOutput(true);
                try (BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(connection.getOutputStream()))) {
                    writer.write(requestBody.toString());
                    writer.flush();
                }
            }
            // ---
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                {
                    response.append(line);
                }
                reader.close();

                return new JSONObject(response.toString());
            }
            else
            {
                Log.e("fetchDataSync", "Erreur : code HTTP " + responseCode);
            }
        }
        catch (IOException | JSONException e)
        {
            Log.e("fetchDataSync", "Erreur lors de la requête : " + e.getMessage());
        }
        catch (Exception e)
        {

        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }
        return null;
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public static void setBaseUrl(String trois, String quatre)
    {
        // http://192.168.210.87:3000
        baseUrl = "http://192.168."+trois+"."+quatre+":3000";
    }
}