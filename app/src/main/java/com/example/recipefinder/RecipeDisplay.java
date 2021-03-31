package com.example.recipefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeDisplay extends AppCompatActivity {

    RecipeFinderDBManager dbManager;
    SharedPreferences sharedPreferences;
    String title ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_display);

        // declare all views for use
        TextView txtViewTitleDisplay = findViewById(R.id.txtViewTitleDisplay);
        ImageView imgViewDisplay = findViewById(R.id.imgViewDisplay);
        TextView txtViewIngredients = findViewById(R.id.txtViewIngredients);
        TextView txtViewIngred = findViewById(R.id.txtViewIngred);
        TextView txtViewDirections = findViewById(R.id.txtViewDirections);
        TextView txtViewDir = findViewById(R.id.txtViewDir);
        TextView txtViewRecipeInfo = findViewById(R.id.txtViewRecipeInfo);

        ImageView imgViewFavorite = findViewById(R.id.imgViewFavorite);

        String loginUser = "";
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.contains("LOGIN_SESSION")){
            loginUser = sharedPreferences.getString("LOGIN_SESSION","");
            Log.d("[HKKO]", "_login User is :"+loginUser);
        }
        else
            Log.d("[HKKO]", "_there is no user prefernce.");



        try {
            // declare bundle from intent and get information from bundle
            Bundle bundle = getIntent().getExtras();
            title = bundle.getString("TITLE");
            int imgId = bundle.getInt("IMG");
            String ingredients = bundle.getString("INGREDS");
            String directions = bundle.getString("DIRECTIONS");
            String servingsz = bundle.getString("SERVINGSZ");
            String preptime = bundle.getString("PREPTIME");
            String cooktime = bundle.getString("COOKTIME");
            String totaltime = bundle.getString("TOTALTIME");

            // set view using information obtained from bundle
            txtViewTitleDisplay.setText(title);
            //imgViewDisplay.setImageResource(recipeImagesList.get(imgId));
            imgViewDisplay.setImageResource(imgId); //for test by Hye Kyung Ko
            String fmtingd = convertIngredList(ingredients);
            txtViewIngredients.setText(fmtingd);
            txtViewIngred.setText(Html.fromHtml("<u>Ingredients:</u>"));
            String fmtdir = convertDirList(directions);
            txtViewDirections.setText(fmtdir);
            txtViewDir.setText(Html.fromHtml("<u>Directions:</u>"));

            // set textview for recipe info
            if (!servingsz.equals("")) {
                txtViewRecipeInfo.append("Serving Size: " + servingsz);
            }

            if (!preptime.equals("")) {
                txtViewRecipeInfo.append("\nPrep Time: " + preptime);
            }

            if (!cooktime.equals("")) {
                txtViewRecipeInfo.append("\nCook Time: " + cooktime);
            }

            if (!totaltime.equals("")) {
                txtViewRecipeInfo.append("\nTotal Time: " + totaltime);
            }

            drawFavorite(loginUser, title);

        } catch (Exception ex) {
            title = getIntent().getExtras().getString("TITLE");
            Log.d("ERR", "Error loading recipe for " + title);
            Toast.makeText(this, "Error loading recipe for " + title, Toast.LENGTH_SHORT).show();
        }

        imgViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginUser = "";
                if (sharedPreferences.contains("LOGIN_SESSION")){
                    loginUser = sharedPreferences.getString("LOGIN_SESSION","");
                    if(loginUser !="") {
                        Log.d("[HKKO]", "_login User is :"+loginUser);
                        dbManager = RecipeFinderDBManager.getInstance(RecipeDisplay.this);
                        if(isFavorite(loginUser, title)) {
                            dbManager.removeFavorite(loginUser, title);
                            imgViewFavorite.setImageResource(R.drawable.heartgrey);
                        }else{
                            dbManager.addFavorite(loginUser, title);
                            imgViewFavorite.setImageResource(R.drawable.heart);
                        }
                        Log.d("[HKKO]", "User ("+loginUser+")Select Favorite! ");
                    }else{
                        Log.d("[HKKO]", "This user is unknown.");
                        Toast.makeText(RecipeDisplay.this, "You need to login to use FAVOURITE service.", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Log.d("[HKKO]", "This user is unknown.");
                    Toast.makeText(RecipeDisplay.this, "You need to login to use FAVOURITE service.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            if (i != 0 && dir.charAt(i) == '$') {
                output += "\n\n";
            } else if (i == 0 && dir.charAt(i) == '$') {
                output += "";
            } else if (dir.charAt(i) == ';') {
                output += ",";
            } else {
                output += dir.charAt(i);
            }
        }

        return output;
    }

    private void drawFavorite(String loginUser, String title){

        dbManager = RecipeFinderDBManager.getInstance(this);
        if(isFavorite(loginUser, title)){
            ImageView imgViewFavorite = findViewById(R.id.imgViewFavorite);
            imgViewFavorite.setImageResource(R.drawable.heart);
        }

    }

    private boolean isFavorite(String loginUser, String title){

        boolean result = false;

        Log.d("[HKKO]","__isFavorite in.");

        dbManager = RecipeFinderDBManager.getInstance(this);
        SQLiteDatabase db = dbManager.getReadableDatabase();


            //tempStr = "SELECT * FROM recipes WHERE ingredients LIKE '"+ condition +"';";
            String queryStr = "SELECT * FROM favorites WHERE username = ? AND title = ?;";
            String[] args = new String[]{loginUser, title};
            Cursor cursor = null;
            try {
                 cursor = db.rawQuery(queryStr, args);

                if (cursor != null) {
                    if(cursor.getCount() > 0) {
                        Log.d("[HKKO]]", "_it is already favorite.");
                        result = true;
                    }
                    else {
                        Log.d("[HKKO]]", "_it is not yet favorite.");
                        result = false;
                    }
                }
                else
                    Log.d("[HKKO]]", "_favorite select is NULL.");

            } catch (Exception ex) {
                Log.d("[HKKO]]", "Querying favorite select error " + ex.getMessage());
                if(cursor != null)
                    cursor.close();
            }finally{
                if(cursor != null)
                    cursor.close();
            }


        return result;
    }


}