package com.example.recipefinder;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Dimension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeResultAdapter extends BaseAdapter {

    List<RecipeResult> recipeResultList;

    public RecipeResultAdapter(List<RecipeResult> recipeResultList) {
        this.recipeResultList = recipeResultList;
    }

    @Override
    public int getCount() {
        return recipeResultList.size();
    }

    @Override
    public Object getItem(int position) {
        return recipeResultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.layout_recipe_result, parent, false);
        }

        TextView txtViewRecipeResultTitle = convertView.findViewById(R.id.txtViewRecipeResultTitle);
        ImageView imgViewRecipeResult = convertView.findViewById(R.id.imgViewRecipeResult);

        txtViewRecipeResultTitle.setText(recipeResultList.get(position).getRecipeName());
        txtViewRecipeResultTitle.setTextSize(Dimension.SP, 18);
        txtViewRecipeResultTitle.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

        imgViewRecipeResult.setImageResource(recipeResultList.get(position).getRecipeImgID());

        return convertView;
    }


}
