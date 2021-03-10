package com.example.recipefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Test to push from Hye Kyung Ko local repo
    }
}