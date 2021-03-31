package com.example.recipefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    // declare views required
    EditText editTextUserName, editTextEmail, editTextPassword, editTextConfirmPassword;

    // Get a RecipeFinderDBManger object
    RecipeFinderDBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // initialize db object
        dbManager = RecipeFinderDBManager.getInstance(this);
        // For login session managing
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // initialize required views
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        // set on click listener for register button
        // verifies input information and redirects to main activity page
        Button regBtn = findViewById(R.id.btnRegister);
        regBtn.setOnClickListener((View view) -> {
            // get all users' username and email to check if user is already in db
            Cursor cursor = dbManager.getAllUsers();
            List<String[]> users = new ArrayList<>(Arrays.asList()); // create list of users from db
            if (cursor.moveToFirst()) {
                do {
                    String username = cursor.getString(0);
                    String email = cursor.getString(1);
                    String[] curUser = {username, email};
                    users.add(curUser);
                } while (cursor.moveToNext());
            }

            boolean exists = false; // set boolean value for whether or not user already exists
            // iterate through users to check whether username or email is already in use
            for (String[] user : users) {
                // check given username to username in db
                if (user[0].equals(editTextUserName.getText().toString())) {
                    exists = true;
                    Log.d("DB", "Username " + editTextUserName.getText().toString() + " already in use");
                    Toast.makeText(this, editTextUserName.getText().toString() + " already in use", Toast.LENGTH_SHORT).show();
                // check given email to email in db
                } else if (user[1].equals(editTextEmail.getText().toString())) {
                    exists = true;
                    Log.d("DB", "Email " + editTextEmail.getText().toString() + " already in use");
                    Toast.makeText(this, "User already exists with email " + editTextEmail.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            // if username and email is not found in db
            if (!exists) {
                boolean added = false; // set boolean value for whether or not user was added successfully
                // check that passwords matches
                if (editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
                    // validate passwords
                    boolean valid = validatePassword(editTextPassword.getText().toString());

                    // add user to db if valid
                    if (valid) {
                        added = dbManager.addUser(editTextUserName.getText().toString(), editTextEmail.getText().toString(), editTextPassword.getText().toString());

                    }
                } else if (!editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
                    Log.d("DB", "Passwords do not match");
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }

                // start new activity on successful add to db
                if (added) {
                    Log.d("DB", "Added " + editTextUserName.getText().toString() + " successfully");

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("LOGIN_SESSION", editTextUserName.getText().toString());
                    editor.commit();
                    startActivity(new Intent(this, MainActivity.class));
                }
            }
        });

        // set on click listener for login button to redirect back to login page
        Button btn = findViewById(R.id.btnToLogin);
        btn.setOnClickListener((View view) -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });

        // set on click listener for skip button to redirect to main activity page
        Button button = findViewById(R.id.btnSkipLogin);
        button.setOnClickListener((View view) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("LOGIN_SESSION", "");
            editor.commit();
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        });

    }

    private boolean validatePassword(String password) {
        // check that password has at least eight characters, consists of only letters and digits, and must contain at least two digits
        if (password.length() < 8) {
            Toast.makeText(this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            char c;
            int count = 1;
            for (int i = 0; i < password.length() - 1; i++) {
                c = password.charAt(i);
                if (!Character.isLetterOrDigit(c)) {
                    Toast.makeText(this, "Password can only contain letters and digits", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (Character.isDigit(c)) {
                    count++;
                }
            }
            if (count < 2) {
                Toast.makeText(this, "Password must contain at least two digits", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
