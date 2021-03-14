package com.example.recipefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // list for ingredients from csv to adapter
    List<String> ingredientsList = new ArrayList<>(Arrays.asList());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // call method to read ingredients csv into ingredientsList
        ingredientsList = ReadIngredients();

        // send ingredients list to grid view adapter to set checkboxes
        GridView gridViewIngredients = findViewById(R.id.gridViewIngredients);
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(ingredientsList);
        gridViewIngredients.setAdapter(ingredientsAdapter);

        // instantiate edit text and button for ingredient search
        EditText editTxtSearchIngredient = findViewById(R.id.editTxtSearchIngredient);
        Button btnSearchIngredients = findViewById(R.id.btnSearchIngredients);

        // setonclicklistener for search ingredients to check corresponding checkbox
        btnSearchIngredients.setOnClickListener((View view) -> {
            String searchIngred = editTxtSearchIngredient.getText().toString();
            if (ingredientsList.contains(searchIngred)) {
                int index = ingredientsList.indexOf(searchIngred);
                CheckBox search = findViewById(index);
                search.setChecked(true);
            } else {
                Toast.makeText(this, searchIngred + " not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // method to read ingredients csv
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