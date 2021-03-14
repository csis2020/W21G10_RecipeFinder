package com.example.recipefinder;

import java.util.List;

public class RecipeResult {
    String recipeName;
    int recipeImgID;

    public RecipeResult(String recipeName, int recipeImgID) {
        this.recipeName = recipeName;
        this.recipeImgID = recipeImgID;
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
}
