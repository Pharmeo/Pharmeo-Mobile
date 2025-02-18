package com.kyoxsu.testandroidstudio;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.kyoxsu.aide.Helper;
import com.kyoxsu.logique.NetworkManager;
import com.kyoxsu.logique.TokenManager;
import com.kyoxsu.testandroidstudio.databinding.MotDePasseOublieBinding;
import org.json.JSONException;
import org.json.JSONObject;
//------------------------------------------------------------------------------
/**
 * Cette classe représente le menu mot de passe oublie. Il permet de renvoyer le mot de passe hashe de l'utilisateur
 */
//------------------------------------------------------------------------------
public class MotDePasseOublie extends AppCompatActivity
{
    MotDePasseOublieBinding binding;

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = MotDePasseOublieBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //----------------------------------------------------------------------
        // --- Gestion du bouton renvoie du mot de passe
        binding.buttonRenvoyerMotDePasse.setEnabled(false);
        TextWatcher textWatcher = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                boolean isText = binding.champEmail.getText().toString().length() > 0;
                binding.buttonRenvoyerMotDePasse.setEnabled(isText);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
        binding.champEmail.addTextChangedListener(textWatcher);

        //----------------------------------------------------------------------
        // --- Gestion du bouton pour renvoyer le mot de passe
        binding.buttonRenvoyerMotDePasse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String mail = binding.champEmail.getText().toString();
                JSONObject body = new JSONObject();
                try
                {
                    body.put("to", mail);
                    body.put("subject", "Test");
                    body.put("text", "Test mot de passe");
                }
                catch (JSONException e)
                {
                    throw new RuntimeException(e);
                }

                // --- Envoie de la requête
                NetworkManager networkManager = new NetworkManager();
                String token = TokenManager.getInstance(null).getToken();
                JSONObject response = networkManager.fetchDataSync(body, "POST", "/sendmail", token);

                // --- Changement de fenêtre
                Helper.changerDeFenetre(MotDePasseOublie.this, Connexion.class);
            }
        });
    }
}