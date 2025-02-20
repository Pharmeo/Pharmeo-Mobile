package com.kyoxsu.aide;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import androidx.viewbinding.ViewBinding;
import com.kyoxsu.logique.NetworkManager;
import com.kyoxsu.logique.TokenManager;
import org.json.JSONArray;
import org.json.JSONObject;
//------------------------------------------------------------------------------
/**
 * Cette classe contient les méthodes permettant de simplifier l'usage du code
 */
//------------------------------------------------------------------------------
public abstract class Helper
{
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /**
     * La méthode permet de basculer d'une fenêtre à l'autre. Elle simplifie le basculement.
     * @param activity La classe actuelle
     * @param cl La classe cible
     */
    public static void changerDeFenetre(Activity activity, Class<?> cl)
    {
        Intent intent = new Intent(activity, cl);
        activity.startActivity(intent);
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /**
     * La méthode permet de faire le lien entre le code java et le xml
     */
    public static <T extends ViewBinding> void associerEtAfficherLayout(Activity activity, Class<T> bindingClass) {
        T binding = null;

        try {
            // Utilisation de la réflexion pour appeler la méthode "inflate" spécifique de chaque binding
            binding = (T) bindingClass.getDeclaredMethod("inflate", LayoutInflater.class)
                    .invoke(null, activity.getLayoutInflater());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Binding Error", "Erreur lors de l'inflation du binding : " + e.getMessage());
        }
        // ---
        if (binding != null)
        {
            View view = binding.getRoot();
            activity.setContentView(view);
        } else {
            Log.e("Binding Error", "Le binding est null après inflation.");
        }
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /**
     * @param medicament - Le médicament récupéré
     * @return La description complète à afficher
     */
    public static String getDescription(JSONObject medicament)
    {
        int idMedicament = medicament.optInt("identifiant", 0);

        String description = "";
        description += medicament.optString("description", null);
        description += "\n\nComposition : "+medicament.optString("composition", null);
        description += "\n\nEffets secondaires : "+medicament.optString("effets_secondaires", null);
        String disponibilites = "";

        // --- On va chercher les informations complémentaires sur le médicament (soit sa disponibilité)
        NetworkManager networkManager = new NetworkManager();
        TokenManager token = TokenManager.getInstance(null);
        String tokenStr = token.getToken();
        JSONObject infosSupplementaires = networkManager.fetchDataSync(null, "GET", "/medicaments/infosSupplementaires/"+idMedicament, tokenStr);
        JSONArray jsonArrayInfos = infosSupplementaires.optJSONArray("infos_supplementaires");
        if (jsonArrayInfos != null)
        {
            for (int i=0; i<jsonArrayInfos.length(); i++)
            {
                JSONObject pharmacie = jsonArrayInfos.optJSONObject(i);
                int quantite = pharmacie.optInt("quantite", 0);
                String nomPharmacie = pharmacie.optString("nom", null);
                // ---
                disponibilites += "\n - "+nomPharmacie+" : "+quantite;
            }
            description += "\n\nDisponibilités : "+disponibilites;
        }
        return description;
    }
}
