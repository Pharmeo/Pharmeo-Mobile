package com.kyoxsu.testandroidstudio;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.kyoxsu.aide.Helper;
import com.kyoxsu.logique.NetworkManager;
import com.kyoxsu.logique.TokenManager;
import com.kyoxsu.testandroidstudio.databinding.EnregistrementBinding;
import org.json.JSONException;
import org.json.JSONObject;
//------------------------------------------------------------------------------
/**
 * Cette classe permet de s'enregistrer et de créer un compte.
 */
//------------------------------------------------------------------------------
public class Enregistrement extends AppCompatActivity
{
    EnregistrementBinding binding;

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = EnregistrementBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //----------------------------------------------------------------------
        // --- Gestion du bouton Enregistrer
        binding.buttonSave.setEnabled(false);
        TextWatcher textWatcher = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                boolean isText = binding.editTextText3.getText().toString().length() > 0
                        && binding.editTextText4.getText().toString().length() > 0
                        && binding.editTextText5.getText().toString().length() > 0
                        && binding.editTextText6.getText().toString().length() > 0
                        && binding.editTextText7.getText().toString().length() > 0
                        && binding.editTextText8.getText().toString().length() > 0
                        && binding.editTextText9.getText().toString().length() > 0
                        && binding.editTextText10.getText().toString().length() > 0
                        && binding.editTextText11.getText().toString().length() > 0
                        ;
                binding.buttonSave.setEnabled(isText);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
        binding.editTextText3.addTextChangedListener(textWatcher);
        binding.editTextText4.addTextChangedListener(textWatcher);
        binding.editTextText5.addTextChangedListener(textWatcher);
        binding.editTextText6.addTextChangedListener(textWatcher);
        binding.editTextText7.addTextChangedListener(textWatcher);
        binding.editTextText8.addTextChangedListener(textWatcher);
        binding.editTextText9.addTextChangedListener(textWatcher);
        binding.editTextText10.addTextChangedListener(textWatcher);
        binding.editTextText11.addTextChangedListener(textWatcher);

        //----------------------------------------------------------------------
        // --- Gestion du bouton enregistrer
        binding.buttonSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //--------------------------------------------------------------
                // --- Récupération de toutes les informations se trouvant dans
                // les différents champs du formulaire
                JSONObject body = new JSONObject();
                try
                {
                    body.put("fk_profil", 2);
                    body.put("nom_compte", binding.editTextText3.getText().toString());
                    body.put("mot_de_passe", binding.editTextText4.getText().toString());
                    body.put("nom", binding.editTextText5.getText().toString());
                    body.put("prenom", binding.editTextText6.getText().toString());
                    body.put("numero_telephone", binding.editTextText7.getText().toString());
                    body.put("mail", binding.editTextText8.getText().toString());
                    body.put("adresse", binding.editTextText9.getText().toString());
                    body.put("ville", binding.editTextText10.getText().toString());
                    body.put("code_postal", binding.editTextText11.getText().toString());

                    System.out.println(body.toString(3));
                }
                catch (JSONException e)
                {
                    throw new RuntimeException(e);
                }

                //--------------------------------------------------------------
                // --- On crée un nouveau client
                NetworkManager networkManager = new NetworkManager();
                String token = TokenManager.getInstance(null).getToken();
                JSONObject response = networkManager.fetchDataSync(body, "POST", "/createClient", null);

                //--------------------------------------------------------------
                // --- On rebascule sur le menu de connexion
                Helper.changerDeFenetre(Enregistrement.this, Connexion.class);
            }
        });
    }
}