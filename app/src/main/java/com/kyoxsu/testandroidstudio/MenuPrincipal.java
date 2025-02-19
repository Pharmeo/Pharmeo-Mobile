package com.kyoxsu.testandroidstudio;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.kyoxsu.aide.Helper;
import com.kyoxsu.logique.NetworkManager;
import com.kyoxsu.logique.TokenManager;
import com.kyoxsu.testandroidstudio.databinding.MenuPrincipalBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
//------------------------------------------------------------------------------
/**
 * Cette classe représente le catalogue de médicaments disponibles
 */
//------------------------------------------------------------------------------
public class MenuPrincipal extends AppCompatActivity
{
    MenuPrincipalBinding binding;
    // ---
    JSONObject data;
    String selectedItem;
    String token;
    List<JSONObject> medicaments;

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = MenuPrincipalBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //----------------------------------------------------------------------
        // --- Récupération du token
        String token = TokenManager.getInstance(null).getToken();
        this.token = token;
        NetworkManager networkManager = new NetworkManager();

        //----------------------------------------------------------------------
        // --- Gestion de la liste
        JSONObject response = networkManager.fetchDataSync(null ,"GET", "/medicaments", token);
        data = response;
        afficheMedicaments(data);

        //----------------------------------------------------------------------
        // --- Gestion de la recherche
        binding.button2.setOnClickListener(new View.OnClickListener()
        {
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
                // ---
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

                // --- On effectue la requête mais cette fois-ci avec des filtres
                JSONObject response = networkManager.fetchDataSync(null ,"GET", url, token);
                data = response;
                afficheMedicaments(data);
            }
        });

        //----------------------------------------------------------------------
        // --- Gestion du Spinner
        Spinner spinner = binding.spinner2;
        String[] items = getResources().getStringArray(R.array.options_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
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
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        //----------------------------------------------------------------------
        // --- Gestion de l'item selectionné
        ListView listeView = binding.listItem;
        listeView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                JSONArray jsonArrayMedicaments = data.optJSONArray("medicaments");
                JSONObject medicament;
                try
                {
                    medicament = jsonArrayMedicaments.getJSONObject(position);
                }
                catch (JSONException e)
                {
                    throw new RuntimeException(e);
                }

                String name = medicament.optString("nom", null);
                int idMedicament = medicament.optInt("identifiant", 0);

                //--------------------------------------------------------------
                // --- Recherche d'un médicament par son nom
                JSONObject response = networkManager.fetchDataSync(null, "GET", "/medicaments?name="+name, token);
                JSONArray jsonArrayMedicament = response.optJSONArray("medicaments");
                JSONObject medicamentTrouve = jsonArrayMedicament.optJSONObject(0);
                showDialog(medicamentTrouve, MenuPrincipal.this, idMedicament);
            }
        });

        //----------------------------------------------------------------------
        // --- Gestion du clique sur les items de la liste
        Button buttonFavoris = binding.button5;
        buttonFavoris.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // --- Changement de fenêtre
                Helper.changerDeFenetre(MenuPrincipal.this, Favoris.class);
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
            try
            {
                medicament = jsonArrayMedicaments.getJSONObject(i);

                String nomMedicaments = medicament.optString("nom", null);
                String infos = nomMedicaments;
                medicaments.add(infos);
            }
            catch (JSONException e)
            {
                throw new RuntimeException(e);
            }
        }

        // --- Afficher la liste de médicaments
        ListView listeMedicaments = binding.listItem;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicaments);
        listeMedicaments.setAdapter(arrayAdapter);
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
    public void showDialog(JSONObject medicament, Context context, int idMedicament)
    {
        String nom = medicament.optString("nom", null);
        String description = Helper.getDescription(medicament);

        // --- On crée la fenêtre de dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Nom : "+nom)
                .setMessage("Description : "+description)
                //.setIcon();
                .setPositiveButton("Favoris", (dialog, which) ->
                {
                    JSONObject body = new JSONObject();
                    try
                    {
                        body.put("idMedicament", idMedicament);
                        body.put("idCompte", Connexion.idCompte);
                    }
                    catch (JSONException e)
                    {
                        throw new RuntimeException(e);
                    }

                    // --- On ajoute le favoris
                    NetworkManager networkManager = new NetworkManager();
                    JSONObject response = networkManager.fetchDataSync(body, "POST", "/addfavoris", this.token);
                    // TODO : modifier pour faire un test pas plus appronfondi
                    if (response.length() > 0)
                    {
                        Toast.makeText(MenuPrincipal.this, "Ajout aux favoris", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                })
                .setNegativeButton("Retour", (dialog, which) ->
                {
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}