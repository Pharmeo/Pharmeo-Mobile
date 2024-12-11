package com.kyoxsu.testandroidstudio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kyoxsu.logique.NetworkManager;
import com.kyoxsu.logique.TokenManager;
import com.kyoxsu.testandroidstudio.databinding.ActivityMain3Binding;
import com.kyoxsu.testandroidstudio.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity {

    ActivityMain3Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain3Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //----------------------------------------------------------------------
        // --- Gestion de la liste

        // Make the network request
        String token = TokenManager.getInstance(null).getToken();
        //System.out.println("MON TOKEN : "+token);
        NetworkManager networkManager = new NetworkManager();
        networkManager.fetchData(null, "GET", "/medicaments/1", token, new NetworkManager.NetworkCallback()
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
                Toast.makeText(MainActivity3.this,
                        "Error: " + error,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // TODO : Ajouter les éléments
        List<String> medicaments = new ArrayList<>();
        medicaments.add("Élément 1");
        medicaments.add("Élément 2");
        medicaments.add("Élément 3");

        ListView listeMedicaments = binding.listItem;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicaments);
        listeMedicaments.setAdapter(arrayAdapter);
    }
}