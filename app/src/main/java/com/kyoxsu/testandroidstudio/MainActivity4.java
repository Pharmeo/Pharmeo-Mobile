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
import com.kyoxsu.testandroidstudio.databinding.ActivityMain4Binding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity4 extends AppCompatActivity
{
    ActivityMain4Binding binding;

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityMain4Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //----------------------------------------------------------------------
        // Gestion du bouton renvoie du mot de passe
        binding.buttonRenvoyerMotDePasse.setEnabled(false);
        TextWatcher textWatcher = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                boolean isText = binding.champEmail.getText().toString().length() > 0;
                binding.buttonRenvoyerMotDePasse.setEnabled(isText);
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        };
        binding.champEmail.addTextChangedListener(textWatcher);

        binding.buttonRenvoyerMotDePasse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // TODO : Ajouter le code pour envoyer un mail
                String mail = binding.champEmail.getText().toString();

                JSONObject body = new JSONObject();
                try {
                    body.put("to", mail);
                    body.put("subject", "Test");
                    body.put("text", "Test mot de passe");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                // Make the network request
                String token = TokenManager.getInstance(null).getToken();
                //System.out.println("MON TOKEN : "+token);
                NetworkManager networkManager = new NetworkManager();
                networkManager.fetchData(body, "POST", "/sendmail", token, new NetworkManager.NetworkCallback()
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

                            // TODO : Faire le traitement du json
                            JSONArray medicaments = result.optJSONArray("medicaments");

                            // TODO : Créer les cards et les rajouter dans la liste etc...
                            // TODO : Voir si je ne peux pas mettre ma liste en dehors de cette instruction

                            // Update UI with the data
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        // Handle error
                        Toast.makeText(MainActivity4.this,
                                "Error: " + error,
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Intent intent = new Intent(MainActivity4.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}