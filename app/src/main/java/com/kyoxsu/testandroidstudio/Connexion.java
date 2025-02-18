package com.kyoxsu.testandroidstudio;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.kyoxsu.aide.Helper;
import com.kyoxsu.logique.NetworkManager;
import com.kyoxsu.logique.TokenManager;
import com.kyoxsu.testandroidstudio.databinding.ConnexionBinding;
import org.json.JSONException;
import org.json.JSONObject;
//------------------------------------------------------------------------------
/**
 *  Cette classe représente le menu de connexion
 */
//------------------------------------------------------------------------------
public class Connexion extends AppCompatActivity
{
    ConnexionBinding binding;

    // --- Mise en mémoire des informations les plus importantes
    public static int idCompte;
    public static String nomCompte;

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ConnexionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //----------------------------------------------------------------------
        // --- Initialisation du networkManager pour effectuer les requêtes
        NetworkManager networkManager = new NetworkManager();

        //----------------------------------------------------------------------
        // --- Gestion du comportement bouton se Connecter
        binding.connexionBT.setEnabled(false);
        TextWatcher textWatcher = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                boolean isBothFieldsFilled = !binding.indentifiantET.getText().toString().isEmpty() &&
                        !binding.motDePasseET.getText().toString().isEmpty();
                // Activer ou désactiver le bouton en fonction du remplissage des deux champs
                binding.connexionBT.setEnabled(isBothFieldsFilled);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
        binding.indentifiantET.addTextChangedListener(textWatcher);
        binding.motDePasseET.addTextChangedListener(textWatcher);

        //----------------------------------------------------------------------
        // --- Gestion dub outon se connecter
        binding.connexionBT.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String idForm = binding.indentifiantET.getText().toString();
                String passwordForm = binding.motDePasseET.getText().toString();
                nomCompte = idForm;
                // ---
                JSONObject body = new JSONObject();
                try
                {
                    body.put("name", idForm);
                    body.put("password", passwordForm);
                }
                catch (JSONException e)
                {
                    throw new RuntimeException(e);
                }

                //--------------------------------------------------------------
                // --- Gestion de la connexion à l'api
                JSONObject response = networkManager.fetchDataSync(body, "POST", "/login", null);
                String strToken = "";
                if (response != null && response.has("token"))
                {
                    strToken =  response.optString("token");
                    TokenManager.getInstance(strToken);

                    // --- Sauvegarde du l'id du compte
                    body = new JSONObject();
                    try
                    {
                        body.put("nom_compte", nomCompte);
                    }
                    catch (JSONException e)
                    {
                        throw new RuntimeException(e);
                    }

                    // --- Récupération de l'id du compte
                    JSONObject jIdCompte = networkManager.fetchDataSync(body, "POST", "/id", strToken);
                    idCompte = jIdCompte.optInt("id_compte", 0);

                    // --- Changement de fenêtre
                    Helper.changerDeFenetre(Connexion.this, MenuPrincipal.class);
                }
                else
                {
                    Toast.makeText(Connexion.this,
                            "[Error] - Connexion a l'API impossible ou identifiant/mot de passe incorrect",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //----------------------------------------------------------------------
        // --- Gestion du comportement du bouton Mot de Passe Oublié
        binding.motDePasseOublieBT.setEnabled(false);
        TextWatcher textWatcher1 = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                boolean isText = binding.indentifiantET.getText().toString().length() > 0;
                binding.motDePasseOublieBT.setEnabled(isText);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
        binding.indentifiantET.addTextChangedListener(textWatcher1);

        //----------------------------------------------------------------------
        // --- Gestion du bouton mot de passe oublié
        binding.motDePasseOublieBT.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Helper.changerDeFenetre(Connexion.this, MotDePasseOublie.class);
            }
        });

        //----------------------------------------------------------------------
        // --- Gestion du changement d'Activité
        binding.pasDeCompteBT.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Helper.changerDeFenetre(Connexion.this, Enregistrement.class);
            }
        });

        //----------------------------------------------------------------------
        // --- Gestion du comportement du bouton valide
        binding.validerBT.setEnabled(false);
        TextWatcher textWatcher2 = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                boolean isBothFieldsFilled = !binding.ip3ET.getText().toString().isEmpty() &&
                        !binding.ip4ET.getText().toString().isEmpty();
                // Activer ou désactiver le bouton en fonction du remplissage des deux champs
                binding.validerBT.setEnabled(isBothFieldsFilled);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
        binding.ip3ET.addTextChangedListener(textWatcher2);
        binding.ip4ET.addTextChangedListener(textWatcher2);

        //----------------------------------------------------------------------
        // --- Gestion de la selection du serveur API
        binding.validerBT.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String trois = binding.ip3ET.getText().toString();
                String quatre = binding.ip4ET.getText().toString();

                NetworkManager.setBaseUrl(trois, quatre);

                Toast.makeText(Connexion.this,
                        "[OK] : Serveur API selectionné",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}