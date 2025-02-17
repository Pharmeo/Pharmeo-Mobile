package com.kyoxsu.testandroidstudio;

import android.content.Intent;
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

        //Helper.associerEtAfficherLayout(Connexion.this, ConnexionBinding.class);

        NetworkManager networkManager = new NetworkManager();

        //----------------------------------------------------------------------
        // Gestion du bouton Se Connecter
        binding.connexionBT.setEnabled(false);
        TextWatcher textWatcher = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                boolean isBothFieldsFilled = !binding.indentifiantET.getText().toString().isEmpty() &&
                        !binding.motDePasseET.getText().toString().isEmpty();
                // Activer ou désactiver le bouton en fonction du remplissage des deux champs
                binding.connexionBT.setEnabled(isBothFieldsFilled);
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        };
        binding.indentifiantET.addTextChangedListener(textWatcher);
        binding.motDePasseET.addTextChangedListener(textWatcher);

        binding.connexionBT.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String idForm = binding.indentifiantET.getText().toString();
                String passwordForm = binding.motDePasseET.getText().toString();

                nomCompte = idForm;

                JSONObject body = new JSONObject();
                try {
                    body.put("name", idForm);
                    body.put("password", passwordForm);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                //--------------------------------------------------------------
                // --- Gestion de la connexion à l'api
                networkManager.fetchData(body, "POST", "/login", null, new NetworkManager.NetworkCallback()
                {
                    @Override
                    public void onSuccess(JSONObject response)
                    {
                        try
                        {
                            String data = response.toString(3);
                            JSONObject result = new JSONObject(data);
                            String strToken = result.optString("token");
                            TokenManager tokenManager = TokenManager.getInstance(strToken);

                            //--------------------------------------------------
                            // --- Sauvegarde du l'id du compte
                            JSONObject body = new JSONObject();
                            try
                            {
                                body.put("nom_compte", nomCompte);
                            } catch (JSONException e)
                            {
                                throw new RuntimeException(e);
                            }

                            //--------------------------------------------------
                            // --- Récupération de l'id du compte
                            networkManager.fetchData(body, "POST", "/id", strToken, new NetworkManager.NetworkCallback()
                            {
                                @Override
                                public void onSuccess(JSONObject response)
                                {
                                    idCompte = response.optInt("id_compte", 0);
                                }

                                @Override
                                public void onError(String error)
                                {
                                    Toast.makeText(Connexion.this,
                                            "Error: " + error,
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                            //--------------------------------------------------
                            // --- On vérifie si l'on à récupéré un token
                            if (tokenManager != null && tokenManager.getToken() != null)
                            {
                                Helper.changerDeFenetre(Connexion.this, MenuPrincipal.class);
                            }
                            else
                            {
                                Toast.makeText(Connexion.this,
                                        "Error: Identifiant ou mot de passe incorrect",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error)
                    {
                        Toast.makeText(Connexion.this,
                                "Error: " + error,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //----------------------------------------------------------------------
        // --- Gestion du bouton Mot de Passe Oublié
        binding.motDePasseOublieBT.setEnabled(false);
        TextWatcher textWatcher1 = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                boolean isText = binding.indentifiantET.getText().toString().length() > 0;
                binding.motDePasseOublieBT.setEnabled(isText);
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        };
        binding.indentifiantET.addTextChangedListener(textWatcher1);

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
        binding.buttonValider.setEnabled(false);
        TextWatcher textWatcher2 = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                boolean isBothFieldsFilled = !binding.editTextText12.getText().toString().isEmpty() &&
                        !binding.editTextText13.getText().toString().isEmpty();
                // Activer ou désactiver le bouton en fonction du remplissage des deux champs
                binding.buttonValider.setEnabled(isBothFieldsFilled);
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        };
        binding.editTextText12.addTextChangedListener(textWatcher2);
        binding.editTextText13.addTextChangedListener(textWatcher2);

        //----------------------------------------------------------------------
        // --- Gestion de la selection du serveur API
        binding.buttonValider.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String trois = binding.editTextText12.getText().toString();
                String quatre = binding.editTextText13.getText().toString();

                NetworkManager.setBaseUrl(trois, quatre);

                Toast.makeText(Connexion.this,
                        "[OK] : Serveur API selectionné",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}