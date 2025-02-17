package com.kyoxsu.aide;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.kyoxsu.testandroidstudio.databinding.AideBinding;
//------------------------------------------------------------------------------
/**
 * Classe d'aide/mémo pour aider à développer sur ce projet
 */
//------------------------------------------------------------------------------
public class Aide extends AppCompatActivity
{
    /**
     * Permet de faire le lien entre un composant fxml et un composant java.
     * Il est impératif d'appeller ce binding pour faire des opérations sur les composant.
     */
    AideBinding binding;

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //----------------------------------------------------------------------
        /**
        Pour créer une activité proprement, il suffit de cliquer sur le package et de faire :
        -> New
        -> Activity
        -> Empty View Activity

        Il est possible de supprimer presque tout le code sauf la ligne ci-dessous
         */

        // Code nécéssaire pour créer l'activité
        super.onCreate(savedInstanceState);

        //----------------------------------------------------------------------
        /**
         * Pour switcher entre 2 fenêtres il suffit d'appeler la méthode ci-dessous
         * @see Helper.changerDeFenetre(Activity activity, Class<?> cl);
         *
         * Il faut lui passer en paramètres l'activité sur laquelle on l'on se trouve actuellement
         * et l'activité (la classe) que l'on souhaite atteindre (afficher).
         *
         * Exemple :
         * -> //Helper.changerDeFenetre([nomDeTonActivite].this, [classeCible].class);
         */

        //----------------------------------------------------------------------
        /**
         * Pour faire des listener (écouteurs d'évènement) il y a différentes façons.
         *
         * Pour les composants texte :
         *
         * Pour les boutons :
         */
    }
}