package com.example.recipefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // set on click listener for login button to redirect back to login page
        Button btn = findViewById(R.id.btnToLogin);
        btn.setOnClickListener((View view) -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });

        // set on click listener for skip button to redirect to main activity page
        Button button = findViewById(R.id.btnSkipLogin);
        button.setOnClickListener((View view) -> {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        });

    }
    }
