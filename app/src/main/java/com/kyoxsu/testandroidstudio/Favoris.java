package com.kyoxsu.testandroidstudio;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.kyoxsu.aide.Helper;
import com.kyoxsu.logique.NetworkManager;
import com.kyoxsu.logique.TokenManager;
import com.kyoxsu.testandroidstudio.databinding.FavorisBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
//------------------------------------------------------------------------------
/**
 * Cette classe représente les favoris des utilisateurs
 */
//------------------------------------------------------------------------------
public class Favoris extends AppCompatActivity
{
    FavorisBinding binding;

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = FavorisBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //----------------------------------------------------------------------
        // --- Remplissage de la liste des favoris
        String token = TokenManager.getInstance(null).getToken();
        NetworkManager networkManager = new NetworkManager();
        JSONObject response = networkManager.fetchDataSync(null, "GET", "/favoris/"+ Connexion.idCompte, token);
        if (response != null)
        {
            ListView listeFavoris = binding.listViewFavoris;
            List<String> favoris = new ArrayList<>();
            // ---
            try
            {
                JSONArray medicamentsFavoris = response.optJSONArray("favoris");
                for (int i=0; i<medicamentsFavoris.length(); i++)
                {
                    favoris.add(medicamentsFavoris.getJSONObject(i).optString("nom"));
                }
            }
            catch (JSONException e)
            {
                throw new RuntimeException(e);
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Favoris.this, android.R.layout.simple_list_item_1, favoris);
            listeFavoris.setAdapter(arrayAdapter);

            //----------------------------------------------------------------------
            // --- Gestion du clique sur un des favoris de la liste
            listeFavoris.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    // --- On récupère l'item selectionné
                    String selectedItem = (String) parent.getItemAtPosition(position);

                    // --- On récupère le médicament
                    JSONObject medicamentRenvoye = networkManager.fetchDataSync(null, "GET", "/medicaments?name="+selectedItem, token);
                    int idMedicament = 0;
                    JSONArray medicamentArray = medicamentRenvoye.optJSONArray("medicaments");
                    if (medicamentArray != null)
                    {
                        JSONObject medicament = null;
                        try
                        {
                            medicament = medicamentArray.getJSONObject(0);
                            idMedicament = medicament.optInt("identifiant", 0);
                        }
                        catch (JSONException e)
                        {
                            throw new RuntimeException(e);
                        }

                        // --- On affiche la description du médicament
                        int finalIdMedicament = idMedicament;
                        String nom = medicament.optString("nom", null);
                        String description = Helper.getDescription(medicament);
                        // ---
                        AlertDialog.Builder builder = new AlertDialog.Builder(Favoris.this);
                        builder.setTitle("Nom : "+nom)
                                .setMessage("Description : "+description)
                                //.setIcon(); // Rajouter l'image
                                .setPositiveButton("Quitter", (dialog, which) -> {
                                    dialog.dismiss();
                                })
                                .setNegativeButton("Supprimer", (dialog, which) -> {
                                    // --- On supprime le médicament en question
                                    networkManager.fetchDataSync(null, "DELETE", "/favoris/delete/"+Connexion.idCompte+"/"+finalIdMedicament, token);
                                    Toast.makeText(Favoris.this,
                                            "Suppression des favoris",
                                            Toast.LENGTH_SHORT).show();

                                    // --- On rafraîchit la liste
                                    recreate();
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            });
        }

        //----------------------------------------------------------------------
        // --- Gestion du bouton de retour
        Button boutonRetour = binding.button6;
        boutonRetour.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // --- Changement de fenêtre
                Helper.changerDeFenetre(Favoris.this, MenuPrincipal.class);
                finish();
            }
        });
    }
}