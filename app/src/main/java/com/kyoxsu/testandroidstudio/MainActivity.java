package com.kyoxsu.testandroidstudio;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;

import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.kyoxsu.logique.NetworkManager;
import com.kyoxsu.logique.TokenManager;
import com.kyoxsu.testandroidstudio.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;
//------------------------------------------------------------------------------
/**
 *
 */
//------------------------------------------------------------------------------
public class MainActivity extends AppCompatActivity
{
    ActivityMainBinding binding;
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //----------------------------------------------------------------------
        // Gestion du bouton Se Connecter
        binding.button.setEnabled(false);
        TextWatcher textWatcher = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                boolean isBothFieldsFilled = !binding.editTextText.getText().toString().isEmpty() &&
                        !binding.editTextText2.getText().toString().isEmpty();
                // Activer ou désactiver le bouton en fonction du remplissage des deux champs
                binding.button.setEnabled(isBothFieldsFilled);
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        };
        binding.editTextText.addTextChangedListener(textWatcher);
        binding.editTextText2.addTextChangedListener(textWatcher);

        binding.button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String idForm = binding.editTextText.getText().toString();
                String passwordForm = binding.editTextText2.getText().toString();

                JSONObject body = new JSONObject();
                try {
                    body.put("name", idForm);
                    body.put("password", passwordForm);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                //--------------------------------------------------------------
                // Make the network request
                NetworkManager networkManager = new NetworkManager();
                networkManager.fetchData(body, "POST", "/login", null, new NetworkManager.NetworkCallback()
                {
                    @Override
                    public void onSuccess(JSONObject response)
                    {
                        // Handle successful response
                        try
                        {
                            String data = response.toString(3);
                            System.out.println("DATA : "+data);
                            JSONObject result = new JSONObject(data);
                            TokenManager tokenManager = TokenManager.getInstance(result.optString("token"));

                            // On vérifie si l'on à récupéré un token
                            if (tokenManager != null && tokenManager.getToken() != null)
                            {
                                Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,
                                        "Error: Identifiant ou mot de passe incorrect",
                                        Toast.LENGTH_SHORT).show();
                            }
                            // Update UI with the data
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        // Handle error
                        Toast.makeText(MainActivity.this,
                                "Error: " + error,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //----------------------------------------------------------------------
        // Gestion du bouton Mot de Passe Oublié
        binding.button4.setEnabled(false);
        TextWatcher textWatcher1 = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                boolean isText = binding.editTextText.getText().toString().length() > 0;
                binding.button4.setEnabled(isText);
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        };
        binding.editTextText.addTextChangedListener(textWatcher1);

        binding.button4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity4.class);
                //intent.putExtra("key", "value"); // Ajouter des données
                startActivity(intent);
            }
        });


        //----------------------------------------------------------------------
        // Gestion du changement d'Activité
        binding.button3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                //intent.putExtra("key", "value"); // Ajouter des données
                startActivity(intent);
            }
        });

        //----------------------------------------------------------------------
        // Gestion du serveur API
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

        binding.buttonValider.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String trois = binding.editTextText12.getText().toString();
                String quatre = binding.editTextText13.getText().toString();

                NetworkManager.setBaseUrl(trois, quatre);

                Toast.makeText(MainActivity.this,
                        "[OK] : Serveur API selectionné",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}