package com.example.recipefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeResultListActivity extends AppCompatActivity {

    List<RecipeResult> recipeResultList = new ArrayList<>();

    //-------------temp code ------------
    /*
    List<String> recipeTitlesList = new ArrayList<>(Arrays.asList());
    List<Integer> recipeImagesList = new ArrayList<>(Arrays.asList());
    List<String> recipeIngredientsList = new ArrayList<>(Arrays.asList());
    List<String> recipeDirectionsList = new ArrayList<>(Arrays.asList());
    List<String> cuisineList = new ArrayList<>(Arrays.asList());
    List<String> servingList = new ArrayList<>(Arrays.asList());
    List<String> prepTimeList = new ArrayList<>(Arrays.asList());
    List<String> cookTimeList = new ArrayList<>(Arrays.asList());
    List<String> totalTimeList = new ArrayList<>(Arrays.asList());
    */

    //-----------------------------------

    //SQLiteDatabase RecipesDb;
    RecipeFinderDBManager dbManager;
    int matchedPercent = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_result_list);

        Log.d("[HKKO]","__RecipeResultListActivity___Start");
        try {
            Bundle bundle = getIntent().getExtras();
            ArrayList<String> checkedIngredlist = bundle.getStringArrayList("KEYS");

            String checkedKeys = "";
            for(int i=0; i<checkedIngredlist.size(); i++){
                checkedKeys += checkedIngredlist.get(i) + ",";
            }

            //readCSVRecipes(); //Temp code
            //createDB();
            //createTables();

            RecipeResult recipeResult;
            //ArrayList<Integer> checkedlist = generateCheckBoxList();
            TextView txtViewCheckBoxListResult = findViewById(R.id.txtViewCheckBoxListResult);

            if(checkedIngredlist.size() == 0){
                txtViewCheckBoxListResult.setText("There is no matched result with " + checkedKeys);
                Toast.makeText(this, "Sorry!! No result found.", Toast.LENGTH_SHORT).show();
            } else {
                //txtViewCheckBoxListResult.setText("");

                //createRecipeResultList(checkedlist);
                int percent = selectRecipeFromTable(checkedIngredlist);

                String textStr;
                if(recipeResultList.size() == 0) {
                    textStr = "No result which matches over "+percent+"% \nwith "+checkedKeys;
                }
                else{
                    if(percent == 100)
                        textStr = "100% matched result with "+checkedKeys;
                    else
                        textStr = "Matched result over"+percent+"% \nwith "+checkedKeys;
                }

                txtViewCheckBoxListResult.setText(textStr);

                ListView listViewResultList = findViewById(R.id.listViewResultList);
                RecipeResultAdapter recipeResultAdapter = new RecipeResultAdapter(recipeResultList);

                listViewResultList.setAdapter(recipeResultAdapter);


                listViewResultList.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {

                    Bundle selectBundle = new Bundle();
                    selectBundle.putString("TITLE", recipeResultList.get(position).recipeName);
                    selectBundle.putInt("IMG", recipeResultList.get(position).recipeImgID);
                    selectBundle.putString("INGREDS", recipeResultList.get(position).ingredient);
                    selectBundle.putString("DIRECTIONS", recipeResultList.get(position).direction);
                    selectBundle.putString("SERVINGSZ", recipeResultList.get(position).servingsz);
                    selectBundle.putString("PREPTIME", recipeResultList.get(position).preptime);
                    selectBundle.putString("COOKTIME", recipeResultList.get(position).cooktime);
                    selectBundle.putString("TOTALTIME", recipeResultList.get(position).totaltime);

                   // Log.d("[HKKO]", "RecipeResultListActivity_ingredient:"+ recipeResultList.get(position).ingredient);
                  //  Log.d("[HKKO]", "RecipeResultListActivity_direction:"+ recipeResultList.get(position).direction);

                    Intent intent = new Intent(RecipeResultListActivity.this, RecipeDisplay.class);
                    intent.putExtras(selectBundle);

                    startActivity(intent);


                });
            }
        }catch(Exception ex){
            Log.d("[HKKO]","__RecipeResultListActivity_onCreate_"+ex);
        }

    }

    private int selectRecipeFromTable(ArrayList<String> keys){

        int i;
        String condition;
        String title, ingredients, direction, servingsz, preptime, cooktime, totaltime;
        int imageId;
        boolean noMatchedResult = true;
        //int count = 0;
        recipeResultList = new ArrayList<>();
        List<String> queryStrings = new ArrayList<>();
        String tempStr;
        int missedKeys = 0;
        int numOfResult = 0;

        condition ="%";
        for(i=0; i<keys.size(); i++){
            condition += keys.get(i) + "%";
        }

        Log.d("[HKKO]","__RecipeResultListActivity___read data from DB.");

        dbManager = RecipeFinderDBManager.getInstance(this);
        SQLiteDatabase recipesDB = dbManager.getWritableDatabase();

        while(noMatchedResult && (missedKeys < 2)) {

            //tempStr = "SELECT * FROM recipes WHERE ingredients LIKE '"+ condition +"';";
            queryStrings = setOfSelectQuries(missedKeys, keys);

            Cursor cursor = null;

            for(i=0; i<queryStrings.size(); i++) {
                try {
                    //Cursor cursor = RecipesDb.rawQuery(queryStrings.get(i), null);
                    cursor = recipesDB.rawQuery(queryStrings.get(i), null);

                    //Log.d("[HKKO]", "_NumOfSELECT=" + cursor.getCount() + "_");


                    if (cursor != null) {
                        cursor.moveToFirst();

                        while (!cursor.isAfterLast()) {
                            title = cursor.getString(0); //first column - title;
                            imageId = cursor.getInt(1); //second column - image drawable id;
                            ingredients = cursor.getString(2); // third column - ingredients;
                            direction = cursor.getString(3); //forth column - direction
                            servingsz = cursor.getString(5); // sixth column - serving size;
                            preptime = cursor.getString(6); // seventh column - prep time;
                            cooktime = cursor.getString(7); // eighth column - cook time;
                            totaltime = cursor.getString(8); // ninth column - total time;

                            RecipeResult recipeResult = new RecipeResult(title, imageId, ingredients, direction, servingsz, preptime, cooktime, totaltime);
                            recipeResultList.add(recipeResult);
                            numOfResult++;

                            cursor.moveToNext();

                        }
                    }
                } catch (Exception ex) {
                    Log.d("[HKKO]]", "Querying recipes select error " + ex.getMessage());
                    if(cursor != null)
                        cursor.close();
                }finally{
                    if(cursor != null)
                        cursor.close();
                }

            }
            if(numOfResult > 0)
                noMatchedResult = false;
            else
                missedKeys++;
        }

        if(numOfResult == 0)
            missedKeys--;
        int percent = (int)((keys.size() - missedKeys) *100/keys.size());


        return percent;
    }

    private List<String> setOfSelectQuries(int numOfMissedKeys, ArrayList<String> keys){
        List<String> queries = new ArrayList<>();
        String selectStr;
        String condition;
        int i, j;

        if(numOfMissedKeys == 0){
            condition ="%";
            for(i=0; i<keys.size(); i++){
                condition += keys.get(i) + "%";
            }

            selectStr = "SELECT * FROM recipes WHERE ingredients LIKE '"+ condition +"';";
            queries.add(selectStr);
        }else if(numOfMissedKeys == 1){

            for(i=0; i<keys.size();i++){
                condition ="%";
                for(j=0; j<keys.size(); j++){
                    if(i!=j)
                        condition += keys.get(j) + "%";
                }
                selectStr = "SELECT * FROM recipes WHERE ingredients LIKE '"+ condition +"';";
                queries.add(selectStr);

            }


        }



        return queries;
    }

        /*
   private void createDB(){
        try{
            RecipesDb = openOrCreateDatabase("Recipes.db",MODE_PRIVATE,null);
            //Toast.makeText(this, "Database ready", Toast.LENGTH_SHORT).show();
        } catch (Exception ex){
            Log.e("[HKKO]", ex.getMessage());
        }
    }

    private void createTables(){
        try{
            String setPRAGMAForeignKeysOn = "PRAGMA foreign_keys = ON;";
            String dropRecipesTableCmd = "DROP TABLE IF EXISTS " + "recipes;";
            String createRecipesTableCmd = "CREATE TABLE recipes (title TEXT PRIMARY KEY, imgDrawableId INTEGER,  ingredients TEXT, directions TEXT, "+
                                                                "cuisine TEXT, serving TEXT, prepTime TEXT, cookTime TEXT, totalTime TEXT);";

            RecipesDb.execSQL(setPRAGMAForeignKeysOn);
            RecipesDb.execSQL(dropRecipesTableCmd); //dropping recipes table
            RecipesDb.execSQL(createRecipesTableCmd); //creating recipes table

        }catch(Exception ex){
            Log.d("[HKKO]", "_createTables_"+ex.getMessage());
        }
    }
    */
        /*
    private void insertRecipesToTable(String title, int imgDrawableId, String ingredients, String directions,
                            String cuisine, String serving, String prepTime, String cookTime, String totalTime){
            long result;
            ContentValues val = new ContentValues();
            val.put("title", title);
            val.put("imgDrawableId", imgDrawableId);
            val.put("ingredients", ingredients);
            val.put("directions", directions);
            val.put("cuisine", cuisine);
            val.put("serving", serving);
            val.put("prepTime", prepTime);
            val.put("cookTime", cookTime);
            val.put("totalTime", totalTime);

            try{
                result = RecipesDb.insert("Recipes", null, val );
                if(result != -1){
                    //Log.d("[HKKO]", "rowid = " + result + " inserted recipes with title: " + title);
                }else{
                    Log.d("[HKKO]", "Error inserting Recipes for " + title);
                }


            }catch(Exception ex){
                Log.d("[HKKO]", "Error adding Recipes for " + title + " " + ex.getMessage());
            }
    }
    */


    /*
    private int selectRecipeFromTable(ArrayList<String> keys){

        int i;
        String condition;
        String title, ingredients, direction;
        int imageId;
        recipeResultList = new ArrayList<>();


        condition ="%";
        for(i=0; i<keys.size(); i++){
            condition += keys.get(i) + "%";
        }


        String queryStr = "SELECT * FROM recipes WHERE ingredients LIKE '"+ condition +"';";
        try {
            Cursor cursor = RecipesDb.rawQuery(queryStr, null);

            Log.d("[HKKO]", "_NumOfSELECT=" + cursor.getCount()+"_");


            if(cursor != null){
                cursor.moveToFirst();

                while(!cursor.isAfterLast()) {
                    title = cursor.getString(0); //first column - title;
                    ingredients = cursor.getString(1); // second column - ingredients;
                    direction = cursor.getString(2); //third column - direction
                    imageId = R.drawable.korean_bibimbap;
                    RecipeResult recipeResult = new RecipeResult(title, imageId, ingredients, direction);
                    recipeResultList.add(recipeResult);

                    cursor.moveToNext();
                }
            }

        }catch(Exception ex){
            Log.d("[HKKO]]", "Querying recipes select error " + ex.getMessage());
        }

    }
    */


    //-------temp code ---------------------------------------------------start
    /*
    private void createRecipeResultList( ArrayList<Integer> checkedlist){
        recipeResultList = new ArrayList<>();

        for(int i = 0; i< checkedlist.size(); i++){
            String title = recipeTitlesList.get(checkedlist.get(i) +1);
            Integer imgID = recipeImagesList.get(checkedlist.get(i) + 1);
            String ingredient = recipeIngredientsList.get(checkedlist.get(i) + 1);
            String direction = recipeDirectionsList.get(checkedlist.get(i) + 1);
            RecipeResult recipeResult = new RecipeResult(title, imgID, ingredient, direction );
            recipeResultList.add(recipeResult);
        }

    }
    */
    /*
    private ArrayList<Integer> generateCheckBoxList(){

        ArrayList<Integer> recipeList = new ArrayList<>();

        int numberOfList = (int)(Math.random() * 10) ;
        Log.d("[HKKO]", "__randomNumber Size =" + numberOfList);
        //recipeList[0] = (int)(Math.random() * 34);
        int randomNum = (int)(Math.random() * 33);
        recipeList.add(randomNum);

        for(int i=1; i < numberOfList; i++){

            randomNum += (int)(Math.random()*3 + 1);
            randomNum = randomNum % 34;
            recipeList.add(randomNum);
            //Log.d("[HKKO]", "__randomNum["+i+"]=" + randomNum);
        }

        return recipeList;
    }
    */
    /*
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
*/
    //-------temp code ---------------------------------------------------end

}