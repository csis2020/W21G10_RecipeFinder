package com.example.recipefinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // list for ingredients from csv to adapter
    TextView name,email,id;
    ImageView imageView;
    Button btnSignOut;
    GoogleSignInClient mGoogleSignInClient;

    List<String> ingredientsList = new ArrayList<>(Arrays.asList());
    //For recipes.csv -------------------------------------------------------
    List<String> recipeTitlesList = new ArrayList<>(Arrays.asList());
    List<Integer> recipeImagesList = new ArrayList<>(Arrays.asList());
    List<String> recipeIngredientsList = new ArrayList<>(Arrays.asList());
    List<String> recipeDirectionsList = new ArrayList<>(Arrays.asList());
    List<String> cuisineList = new ArrayList<>(Arrays.asList());
    List<String> servingList = new ArrayList<>(Arrays.asList());
    List<String> prepTimeList = new ArrayList<>(Arrays.asList());
    List<String> cookTimeList = new ArrayList<>(Arrays.asList());
    List<String> totalTimeList = new ArrayList<>(Arrays.asList());
    //------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnSignOut = findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            name.setText(personName);
            email.setText(personEmail);
            id.setText(personId);
            Glide.with(this).load(String.valueOf(personPhoto)).into(imageView);
        }



        //Insert Recipes' data to the recipes table in DB.
        addRecipesToDB();

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
                try {
                    search.setChecked(true);
                } catch (Exception e) {
                    Log.d("CHKBOXERR", "Error checking " + searchIngred);
                }
                editTxtSearchIngredient.setText(""); // clear edit text after saving
            } else {
                Toast.makeText(this, searchIngred + " not found", Toast.LENGTH_SHORT).show();
            }
        });

        // instantiate button for search recipes
        Button btnSearchRecipe = findViewById(R.id.btnSearchRecipes);
        // create bundle to pass checked ingredients to recipe list page
        Bundle ingredientsBundle = new Bundle();

        // setonclicklistener for search recipe to collect checked ingredients, put into bundle and pass to next intent
        btnSearchRecipe.setOnClickListener((View view) -> {
            // create array to store keys for bundle
            ArrayList<String> keys = new ArrayList<>(Arrays.asList());
            for (int i = 0; i < gridViewIngredients.getChildCount(); i++) {
                CheckBox child = (CheckBox)gridViewIngredients.getChildAt(i);
                if (child.isChecked()) {
                    //String key = "key" + i;
                    String key = ingredientsList.get(i);
                    keys.add(key);
                    ingredientsBundle.putString(key, ingredientsList.get(i));
                }
            }
//             NEXT INTENT SHOULD BE RECIPELIST.CLASS -- RecipeDisplay used for now
            ingredientsBundle.putStringArrayList("KEYS", keys);
            Intent intent = new Intent(this, RecipeResultListActivity.class);
            intent.putExtras(ingredientsBundle);

            startActivity(intent);
        });
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Signed out succesfully", Toast.LENGTH_LONG).show();

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

    //insert recipes from Recipes.csv fle to Recipes table in DB.
    private void addRecipesToDB(){
        boolean result;
        Log.d("[HKKO]", " addRecipesToDB in.");
        Log.d("[HKKO]", " addRecipesToDB Before calling dbInsert.");
        //call method to read Recipes.csv.
        readCSVRecipes();

        RecipeFinderDBManager dbManager = RecipeFinderDBManager.getInstance(this);

        Log.d("[HKKO]", " _MainActivity_add Recipes into Table.");
        for(int i = 1; i < recipeTitlesList.size() ; i++){
            result = dbManager.addRecipe(recipeTitlesList.get(i), recipeImagesList.get(i), recipeIngredientsList.get(i), recipeDirectionsList.get(i),
                    //result = dbManager.updateRecipe(recipeTitlesList.get(i), recipeImagesList.get(i), recipeIngredientsList.get(i), recipeDirectionsList.get(i),
                    cuisineList.get(i), servingList.get(i), prepTimeList.get(i), cookTimeList.get(i), totalTimeList.get(i));

            if(!result){
                Log.d("[HKKO]", " addRecipesToDB["+i+"] is failed.");
            }
            else{
                Log.d("[HKKO]", " addRecipesToDB["+i+"] is successful.");
            }
        }

        Log.d("[HKKO]", " addRecipesToDB after calling dbInsert.");


    }

    // method to read recipes csv
    private void readCSVRecipes(){

        //populate the list
        InputStream inputStream = getResources().openRawResource(R.raw.recipes); //students.csv file
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try{
            int i=0;
            String csvLine;
            String imgDrawableName;
            int imgID;
            while((csvLine = reader.readLine()) != null){
                String[] fieldArray = csvLine.split(",");
                //fieldArray[0] : String,  Title
                //fieldArray[1] : String,  image
                //fieldArray[2] : String,  Ingredients
                //fieldArray[3] : String,  Directions
                //fieldArray[4] : String,  Cuisine
                //fieldArray[5] : int, Serving
                //fieldArray[6] : int, cook time
                //fieldArray[7] : int,  total time

                recipeTitlesList.add(fieldArray[0]);
                imgDrawableName = fieldArray[1].toLowerCase();
                imgID = getResources().getIdentifier(imgDrawableName, "drawable", getPackageName());
                recipeImagesList.add(imgID); //temporarily, we are using fixed number;
                recipeIngredientsList.add(fieldArray[2]);
                recipeDirectionsList.add(fieldArray[3]);
                cuisineList.add(fieldArray[4]);
                servingList.add(fieldArray[5]);
                prepTimeList.add(fieldArray[6]);
                cookTimeList.add(fieldArray[7]);
                totalTimeList.add(fieldArray[8]);

                //Log.d("[HKKO]", "["+i+"][0]: "+ fieldArray[0] + ", ["+i+"][1]: "+ fieldArray[1]);
                //Log.d("[HKKO]", "["+i+"][2]: "+ fieldArray[2]);
                //Log.d("[HKKO]", "["+i+"][3]: "+ fieldArray[3]);
                //Log.d("[HKKO]", "["+i+"][4]: "+ fieldArray[4] + ", ["+i+"][5]:" +fieldArray[5]+
                //                            ", ["+i+"][6]:" +fieldArray[6]+", ["+i+"][7]:" +fieldArray[7]+", ["+i+"][8]:" +fieldArray[8] );

                i++;
            }
        }catch(IOException ex){
            Log.d("[HKKO]","__RecipeResultListActivity_ex1_"+ex);
            throw new RuntimeException("Error reading CSV file " + ex);
        }finally{
            try{
                inputStream.close();
            }catch(IOException ex){
                Log.d("[HKKO]","__RecipeResultListActivity_ex2_"+ex);
                throw new RuntimeException("Error closing input stream " + ex);
            }
        }
    }

}