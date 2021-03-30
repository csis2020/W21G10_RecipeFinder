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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    int RC_SIGN_IN = 0;
    SignInButton signin;
    GoogleSignInClient mGoogleSignInClient;

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
                    startActivity(new Intent(this, MainActivity.class));
                }
            }

            if (!signedin) {
                Log.d("DB", "Unsuccessful log in for " + loginemail.getText().toString());
                Toast.makeText(this, "Username and email do not match", Toast.LENGTH_SHORT).show();
            }
        });

        signin = findViewById(R.id.sign_in_button);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());

        }
    }
}