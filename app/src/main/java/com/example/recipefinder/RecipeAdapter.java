package com.example.recipefinder;

import android.media.Image;

import java.util.List;


// recipe adapter to get and set information from database to view
public class RecipeAdapter {
    String recipeTitle; // recipe title
    Image recipeImage; // recipe image
    List<String> ingredientsList; // list of ingredients for recipe
    List<String> directions; // list of directions for recipe

    // constructor with just recipeTitle as parameter
    // recipeImage, ingredientsList and directions should be retrieved from database
    public RecipeAdapter(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public Image getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(Image recipeImage) {
        this.recipeImage = recipeImage;
    }

    public List<String> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(List<String> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public List<String> getDirections() {
        return directions;
    }

    public void setDirections(List<String> directions) {
        this.directions = directions;
    }
}
