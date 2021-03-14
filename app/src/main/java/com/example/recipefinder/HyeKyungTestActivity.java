package com.example.recipefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;

// //Test for Hye Kyung-----------------------------------START
//        Intent testIntent = new Intent(MainActivity.this, HyeKyungTestActivity.class);
//        startActivity(testIntent);
//        //-----------------------------------------------------END

public class HyeKyungTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hye_kyung_test);

        Button testBtn = findViewById(R.id.TestBtn);

        testBtn.setOnClickListener((View v)->{
            //Create a bundle object with all the data

            /*
            Bundle bundle = new Bundle();
            bundle.putString("FOODIMG", themesAdapter.getThemeName(position));
            bundle.putInt("FOODNUM", themesAdapter.getThemeImg(position));
            bundle.putInt("SONGID", themesAdapter.getThemeSong(position));
            bundle.putDouble("PRICE", themesAdapter.getThemeHourlyPrice(position));

             */

            Intent testIntent = new Intent(HyeKyungTestActivity.this, RecipeResultListActivity.class);


            Bundle bundle = new Bundle();
            //bundle.putIntegerArrayList("CHECKEDLIST", checkedList);

            testIntent.putExtras(bundle);

            startActivity(testIntent);


        });


    }


}