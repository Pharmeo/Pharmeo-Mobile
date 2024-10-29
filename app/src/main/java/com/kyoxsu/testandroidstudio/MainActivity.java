package com.kyoxsu.testandroidstudio;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.kyoxsu.testandroidstudio.databinding.ActivityMainBinding;
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

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = binding.editTextText.getText().toString();
                String password = binding.editTextText2.getText().toString();

                String idEnDur = "root";
                String passwordEnDur = "1234";

                if (id.equals(idEnDur) && password.equals(passwordEnDur))
                {
                    Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                    //intent.putExtra("key", "value")   ; // Ajouter des données
                    startActivity(intent);
                }
            }
        });

        //----------------------------------------------------------------------
        // Gestion du bouton Mot de Passe Oublié
        binding.button4.setEnabled(false);
        TextWatcher textWatcher1 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isText = binding.editTextText.getText().toString().length() > 0;
                binding.button4.setEnabled(isText);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        binding.editTextText.addTextChangedListener(textWatcher1);

        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity4.class);
                //intent.putExtra("key", "value"); // Ajouter des données
                startActivity(intent);
            }
        });


        //----------------------------------------------------------------------
        // Gestion du changement d'Activité
        binding.button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                //intent.putExtra("key", "value"); // Ajouter des données
                startActivity(intent);
            }
        });
    }
}