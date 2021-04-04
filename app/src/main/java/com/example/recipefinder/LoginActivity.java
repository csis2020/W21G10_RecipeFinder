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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    // Get a RecipeFinderDBManger object
    RecipeFinderDBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // instantiate edit texts required for login
        EditText loginemail = findViewById(R.id.editTextEmailAddress);
        EditText loginpass = findViewById(R.id.editTextTextPassword3);

        // remember me function
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.contains("EMAIL")) {
            String email = sharedPreferences.getString("EMAIL", "");
            loginemail.setText(email);
            String pass = sharedPreferences.getString("PASSWORD", "");
            loginpass.setText(pass);
        }

        CheckBox cb = findViewById(R.id.checkBox);
        cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("EMAIL", loginemail.getText().toString());
            editor.putString("PASSWORD", loginpass.getText().toString());
            editor.commit();
        });

        // initialize db object
        //userdb = new RegisterDb(this);
        dbManager = RecipeFinderDBManager.getInstance(this);

        TextView btn = findViewById(R.id.textViewSignIn);
        btn.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        // set on click listener for log in with db
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener((View view) -> {
            // get all users' username and email to check if user is already in db
            Cursor cursor = dbManager.getAllUsers();
            List<String[]> users = new ArrayList<>(Arrays.asList()); // create list of users from db
            if (cursor.moveToFirst()) {
                do {
                    String user = cursor.getString(0);
                    String email = cursor.getString(1);
                    String pass = cursor.getString(2);
                    String[] curUser = {user, email, pass};
                    users.add(curUser);
                } while (cursor.moveToNext());
            }

            // iterate through db to check email and password
            boolean signedin = false; // set boolean for sign in, use only for toast
            for (int i = 0; i < users.size(); i++) {
                if (loginemail.getText().toString().equals(users.get(i)[1]) && loginpass.getText().toString().equals(users.get(i)[2])) {
                    signedin = true;
                    Log.d("DB", "Successful log in for " + users.get(i)[0]);
                    Toast.makeText(this, "Welcome " + users.get(i)[0], Toast.LENGTH_SHORT).show();
                    //for session managing
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("LOGIN_SESSION",users.get(i)[0]);
                    editor.commit();
                    startActivity(new Intent(this, MainActivity.class));
                }
            }

            if (!signedin) {
                Log.d("DB", "Unsuccessful log in for " + loginemail.getText().toString());
                Toast.makeText(this, "Username and email do not match", Toast.LENGTH_SHORT).show();
            }
        });

    }
}