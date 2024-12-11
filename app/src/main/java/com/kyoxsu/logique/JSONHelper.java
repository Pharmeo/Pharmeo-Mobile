package com.kyoxsu.logique;

import org.json.JSONArray;
import java.util.ArrayList;

public class JSONHelper
{
    public static ArrayList<Card> extractionMedicaments(JSONArray tableauMedicaments)
    {
        ArrayList<Card> listeMedicaments = new ArrayList<Card>();

        for (int i=0; i<tableauMedicaments.length(); i++)
        {
            // TODO : Modifier de manière a set les bonnes ressources
            listeMedicaments.add(new Card(0, null, null));
        }

        return listeMedicaments;
    }
}
