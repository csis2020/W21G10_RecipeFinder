package com.example.recipefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

public class RecipeDisplay extends AppCompatActivity {
//  recipe title should be passed from recipe list view
//  create list for ingredients with data from database
//  create list for directions with data from database
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_display);
    }
}