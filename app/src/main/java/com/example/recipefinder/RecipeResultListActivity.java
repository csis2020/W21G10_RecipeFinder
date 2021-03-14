package com.example.recipefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ListView;

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
    List<String> recipeTitlesList = new ArrayList<>(Arrays.asList());
    List<Integer> recipeImagesList = new ArrayList<>(Arrays.asList());
    List<String> recipeIngredientsList = new ArrayList<>(Arrays.asList());
    List<String> recipeDirectionsList = new ArrayList<>(Arrays.asList());
    //-----------------------------------



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_result_list);

        Log.d("[HKKO]","__RecipeResultListActivity___Start");
        try {
            Bundle bundle = getIntent().getExtras();
            ArrayList<String> checkedIngredlist = bundle.getStringArrayList("KEYS");


            readCSVStudents(); //Temp code

            ArrayList<Integer> checkedlist = generateCheckBoxList();
            createRecipeResultList(checkedlist);

        }catch(Exception ex){
            Log.d("[HKKO]","__RecipeResultListActivity_onCreate_"+ex);
        }

        ListView listViewResultList = findViewById(R.id.listViewResultList);
        RecipeResultAdapter recipeResultAdapter = new RecipeResultAdapter(recipeResultList);

        listViewResultList.setAdapter(recipeResultAdapter);




    }

    private void createRecipeResultList( ArrayList<Integer> checkedlist){
        recipeResultList = new ArrayList<>();

        for(int i = 0; i< checkedlist.size(); i++){
            String title = recipeTitlesList.get(checkedlist.get(i) +1);
            int imgID = recipeImagesList.get(checkedlist.get(i) + 1);
            RecipeResult recipeResult = new RecipeResult(title, imgID);
            recipeResultList.add(recipeResult);
        }

    }

    private ArrayList<Integer> generateCheckBoxList(){

        ArrayList<Integer> recipeList = new ArrayList<>();

        int numberOfList = (int)(Math.random() * 10) ;
        Log.d("[HKKO]", "__generateCheckBoxList_numberOfList=" + numberOfList);
        //recipeList[0] = (int)(Math.random() * 34);
        int randomNum = (int)(Math.random() * 34);
        recipeList.add(randomNum);

        for(int i=1; i < numberOfList; i++){
            //recipeList[i] = (recipeList[0] + (int)(Math.random()*3)) % 35;
            randomNum += (int)(Math.random()*3 + 1);
            randomNum = randomNum % 35;
            recipeList.add(randomNum);
        }

        return recipeList;
    }

    //-------temp code ---------------------------------------------------start
    private void readCSVStudents(){

        //populate the list
        InputStream inputStream = getResources().openRawResource(R.raw.recipes); //students.csv file
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try{
            int i=0;
            String csvLine;
            while((csvLine = reader.readLine()) != null){
                String[] fieldArray = csvLine.split(",");
                //fieldArray[0] : String,  Title
                //fieldArray[1] : String,  Ingredients
                //fieldArray[2] : String,  Directions
                //fieldArray[3] : String,  Cuisine
                //fieldArray[4] : int, Serving
                //fieldArray[5] : int, cook time
                //fieldArray[6] : int,  total time

                recipeTitlesList.add(fieldArray[0]);
                recipeImagesList.add(R.drawable.korean_bibimbap); //temporarily, we are using fixed number;
                recipeIngredientsList.add(fieldArray[1]);
                recipeDirectionsList.add(fieldArray[2]);
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
    //-------temp code ---------------------------------------------------end

}