package com.kyoxsu.aide;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import androidx.viewbinding.ViewBinding;
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

        // Vérifier si le binding a été correctement initialisé
        if (binding != null) {
            // Obtenir la vue racine du binding
            View view = binding.getRoot();
            // Utiliser l'activité passée en paramètre pour appeler setContentView
            activity.setContentView(view);
        } else {
            Log.e("Binding Error", "Le binding est null après inflation.");
        }
    }
}
