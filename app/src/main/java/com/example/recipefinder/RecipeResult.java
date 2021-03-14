package com.example.recipefinder;

import java.util.List;

public class RecipeResult {
    String recipeName;
    int recipeImgID;
    String ingredient;
    String direction;

    public RecipeResult(String recipeName, int recipeImgID, String ingredient, String direction) {
        this.recipeName = recipeName;
        this.recipeImgID = recipeImgID;
        this.ingredient = ingredient;
        this.direction = direction;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public int getRecipeImgID() {
        return recipeImgID;
    }

    public void setRecipeImgID(int recipeImgID) {
        this.recipeImgID = recipeImgID;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
