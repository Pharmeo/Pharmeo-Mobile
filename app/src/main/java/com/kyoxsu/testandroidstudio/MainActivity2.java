package com.kyoxsu.testandroidstudio;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kyoxsu.logique.NetworkManager;
import com.kyoxsu.logique.TokenManager;
import com.kyoxsu.testandroidstudio.databinding.ActivityMain2Binding;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity2 extends AppCompatActivity
{
    ActivityMain2Binding binding;

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //----------------------------------------------------------------------
        // Gestion du bouton Enregistrer
        binding.buttonSave.setEnabled(false);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
            public void afterTextChanged(Editable s) {

            }
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
        // Gestion du bouton enregistrer
        binding.buttonSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO : Récupérer les informations des champs pour construite le json
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

                String token = TokenManager.getInstance(null).getToken();
                System.out.println("TOKEN RECUPERE : "+token);

                // Make the network request
                NetworkManager networkManager = new NetworkManager();
                networkManager.fetchData(body, "POST", "/createClient", null, new NetworkManager.NetworkCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        // Handle successful response
                        try {

                            String data = response.toString(3);
                            System.out.println("DATA : "+data);
                            // Update UI with the data
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        // Handle error
                        Toast.makeText(MainActivity2.this,
                                "Error: " + error,
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}