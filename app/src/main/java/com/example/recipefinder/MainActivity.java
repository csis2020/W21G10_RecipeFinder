package com.example.recipefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // lists for recipe titles, images, ingredients and directions
    // images list as int for resource id
    // ingredients and directions lists as array of arrays for each recipe
    List<String> recipeTitlesList = new ArrayList<>(Arrays.asList());
    List<Integer> recipeImagesList = new ArrayList<>(Arrays.asList());
    List<ArrayList<String>> recipeIngredientsList = new ArrayList<ArrayList<String>>(Arrays.asList());
    List<ArrayList<String>> recipeDirectionsList = new ArrayList<ArrayList<String>>(Arrays.asList());
    List<String> ingredientsList = new ArrayList<>(Arrays.asList());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Test to push from Hye Kyung Ko local repo

        ingredientsList = ReadIngredients();
        GridView gridViewIngredients = findViewById(R.id.gridViewIngredients);

        gridViewIngredients.setAdapter(new IngredientsAdapter(ingredientsList));
    }

    private List<String> ReadIngredients() {
        List<String> ingredList = new ArrayList<>(Arrays.asList());
        InputStream inputStream = getResources().openRawResource(R.raw.ingredients);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                ingredList.add(line);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error reading file " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                throw new RuntimeException("Error closing input stream " + ex);
            }
        }

        return ingredList;
    }
}