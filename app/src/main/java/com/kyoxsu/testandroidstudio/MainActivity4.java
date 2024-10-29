package com.kyoxsu.testandroidstudio;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.kyoxsu.testandroidstudio.databinding.ActivityMain4Binding;

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
                // Ma fonction pour envoyer un mail

                //Email email = new Email(mail, "Test", "test d'envoie de mot de passe");

                Intent intent = new Intent(MainActivity4.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}