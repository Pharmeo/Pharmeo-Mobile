package com.kyoxsu.logique;

import android.content.Context;
import android.widget.Toast;
import org.json.JSONObject;
//------------------------------------------------------------------------------
/**
 * Classe abstraite regroupant des méthodes permettant de faciliter la lecture du code
 */
//------------------------------------------------------------------------------
public abstract class PharmeoHelper
{
    // Cette methode permet de faire simplement une requête http
    // TODO : A optimiser car il va falloir que je récupère le token à tout les coups
    public static void executeRequete(JSONObject body, String method, String url, Context activity, NetworkManager.NetworkCallback callback)
    {
        String token = TokenManager.getInstance(null).getToken();
        NetworkManager networkManager = new NetworkManager();
        networkManager.fetchData(body, method, url, null, new NetworkManager.NetworkCallback()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
                // TODO : Trouver un moyen de faire sortir la réponse
                callback.onSuccess(response);
            }

            @Override
            public void onError(String error)
            {
                // Handle error
                Toast.makeText(activity,
                        "Error: " + error,
                        Toast.LENGTH_SHORT).show();

                callback.onError(error);
            }
        });
    }
}
