package com.kyoxsu.testandroidstudio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kyoxsu.logique.NetworkManager;
import com.kyoxsu.logique.TokenManager;
import com.kyoxsu.testandroidstudio.databinding.ActivityMain3Binding;
import com.kyoxsu.testandroidstudio.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainActivity3 extends AppCompatActivity {

    ActivityMain3Binding binding;
    JSONObject data;
    String selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain3Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //----------------------------------------------------------------------
        // --- Gestion de la liste

        // TODO : Appeler la méthode avec, null, GET, /medicaments, token

        // Make the network request
        String token = TokenManager.getInstance(null).getToken();
        NetworkManager networkManager = new NetworkManager();
        networkManager.fetchData(null, "GET", "/medicaments", token, new NetworkManager.NetworkCallback()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
                //System.out.println(response);
                if (response != null)
                {
                    data = response;
                    afficheMedicaments(data);
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

        //----------------------------------------------------------------------
        // --- Gestion de la recherche
        binding.button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                String name = binding.editTextText15.getText().toString();
                System.out.println("NAME : "+name);
                String systeme = getSelectedItem();

                String url = "/medicaments";

                int count = 0;
                if (!name.equals(""))
                {
                    count++;
                }
                // ---
                if (!systeme.equals("Aucun"))
                {
                    count++;
                }

                if (count == 1)
                {
                    if (!name.equals(""))
                    {
                        url+="?name="+name;
                    }
                    // ---
                    if (!systeme.equals("Aucun"))
                    {
                        url+="?system="+systeme;
                    }
                }
                else if (count == 2)
                {
                    url+="?name="+name+"&system="+systeme+"";
                }

                System.out.println("URL : "+url);

                networkManager.fetchData(null, "GET", url, token, new NetworkManager.NetworkCallback()
                {
                    @Override
                    public void onSuccess(JSONObject response)
                    {
                        // Handle successful response
                        if (response != null)
                        {
                            data = response;
                            afficheMedicaments(data);
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
            }
        });

        //----------------------------------------------------------------------
        // --- Gestion du Spinner

        Spinner spinner = binding.spinner2;
        String[] items = getResources().getStringArray(R.array.options_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = "";
                switch (position)
                {
                    case 0:
                        item = "Aucun";
                        break;
                    case 1:
                        item = "Système cardiovasculaire";
                        break;
                    case 2:
                        item = "Système dermatologique";
                        break;
                    case 3:
                        item = "Système digestif";
                        break;
                    case 4:
                        item = "Système endocrinien";
                        break;
                    case 5:
                        item = "Système fongique";
                        break;
                    case 6:
                        item = "Système hépatique";
                        break;
                    case 7:
                        item = "Système immunitaire";
                        break;
                    case 8:
                        item = "Système infectieux";
                        break;
                    case 9:
                        item = "Système musculo-squelettique";
                        break;
                    case 10:
                        item = "Système nerveux central";
                        break;
                    case 11:
                        item = "Système oncologique";
                        break;
                    case 12:
                        item = "Système osseux";
                        break;
                    case 13:
                        item = "Système respiratoire";
                        break;
                    case 14:
                        item = "Système rénal";
                        break;
                    case 15:
                        item = "Système urinaire";
                        break;
                    case 16:
                        item = "Système vasculaire";
                }

                setSelectedItem(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //----------------------------------------------------------------------
        // --- Gestion du clique sur les items de la liste
        binding.listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = view.getContext();

                // TODO : Récupérer l'item en question


                //System.out.println(data.toString(3));

                JSONArray medicaments = data.optJSONArray("medicaments");
                JSONObject medicament;
                try {
                    medicament = medicaments.getJSONObject(position);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


                showDialog(medicament, context);
            }
        });
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private void afficheMedicaments(JSONObject data)
    {
        JSONArray jsonArrayMedicaments = data.optJSONArray("medicaments");
        List<String> medicaments = new ArrayList<>();
        for (int i=0; i<jsonArrayMedicaments.length(); i++)
        {
            JSONObject medicament = null;
            try {
                medicament = jsonArrayMedicaments.getJSONObject(i);

                String nomMedicaments = medicament.optString("nom", null);
                //String descriptionMedicament = medicament.optString("description", null);
                //String idMedicament = medicament.optString("identifiant", null);
                String infos = nomMedicaments;
                medicaments.add(infos);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        // --- Afficher la lsite de médicaments
        ListView listeMedicaments = binding.listItem;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicaments);
        listeMedicaments.setAdapter(arrayAdapter);
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private void creationUrl()
    {

    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String getSelectedItem()
    {
        return this.selectedItem;
    }
    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void showDialog(JSONObject medicament, Context context)
    {
        // TODO : Modifier pour ajouter la disponibilité
        String nom = medicament.optString("nom", null);
        String description = medicament.optString("description", null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Nom : "+nom)
                .setMessage("Description : "+description)
                //.setIcon();
                .setPositiveButton("Favoris", (dialog, which) -> {
                    // Code à exécuter quand on clique sur Favoris
                    // TODO : Créer le système de favoris
                    dialog.dismiss();
                })
                .setNegativeButton("Retour", (dialog, which) -> {
                    // Code à exécuter quand on clique sur Annuler
                    dialog.dismiss();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}