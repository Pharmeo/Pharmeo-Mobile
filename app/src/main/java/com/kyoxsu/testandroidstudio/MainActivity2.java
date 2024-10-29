package com.kyoxsu.testandroidstudio;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kyoxsu.testandroidstudio.databinding.ActivityMain2Binding;
import com.kyoxsu.testandroidstudio.databinding.ActivityMainBinding;

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
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                //intent.putExtra("key", "value"); // Ajouter des données
                startActivity(intent);

                // TODO : Ajouter le code pour ajouter un utilisateur
            }
        });
    }
}