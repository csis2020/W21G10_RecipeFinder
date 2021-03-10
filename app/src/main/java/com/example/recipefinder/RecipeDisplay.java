package com.example.recipefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeDisplay extends AppCompatActivity {

    // declare image list here to get image
    List<Integer> recipeImagesList = new ArrayList<>(Arrays.asList());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_display);

        // declare all views for use
        TextView txtViewTitleDisplay = findViewById(R.id.txtViewTitleDisplay);
        ImageView imgViewDisplay = findViewById(R.id.imgViewDisplay);
        TextView txtViewIngredients = findViewById(R.id.txtViewIngredients);
        TextView txtViewDirections = findViewById(R.id.txtViewDirections);

        try {
            // declare bundle from intent and get information from bundle
            Bundle bundle = getIntent().getExtras();
            String title = bundle.getString("TITLE");
            int imgId = bundle.getInt("IMG");
            String ingredients = bundle.getString("INGREDS");
            String directions = bundle.getString("DIRECTIONS");

            // set view using information obtained from bundle
            txtViewTitleDisplay.setText(title);
            imgViewDisplay.setImageResource(recipeImagesList.get(imgId));
            txtViewIngredients.setText(ingredients);
            txtViewDirections.setText(directions);
        } catch (Exception ex) {
            String title = getIntent().getExtras().getString("TITLE");
            Log.d("ERR", "Error loading recipe for " + title);
            Toast.makeText(this, "Error loading recipe for " + title, Toast.LENGTH_SHORT).show();
        }
    }

    public String convertIngredList(String str) {
        String output = "";

        String[] list = str.split(";");

        for (int i = 0; i < list.length; i++) {
            output += " - ";
            for (int k = 0; k < list[i].length(); k++) {
                if (list[i].charAt(k) != '$') {
                    output += list[i].charAt(k);
                } else {
                    output += ",";
                }
            }
            output += "\n";
        }

        return output;
    }

    private String convertDirList(String dir) {
        String output = "";

        for (int i = 0; i < dir.length(); i++){
            if (dir.charAt(i) == '$') {
                output += "\n\n";
            } else if (dir.charAt(i) == ';') {
                output += ",";
            } else {
                output += dir.charAt(i);
            }
        }

        return output;
    }
}