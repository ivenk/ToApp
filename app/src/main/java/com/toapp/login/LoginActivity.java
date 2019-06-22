package com.toapp.login;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.toapp.R;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // we want to watch imput to both fields
        final Button loginButton = findViewById(R.id.login);
        EditText userField = findViewById(R.id.username);
        final EditText passField = findViewById(R.id.password);
        TextWatcher myTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG, "onTextChanged: Called !");
                // if both fields have a value set
                if((charSequence.length() > 0) && (passField.getText().length() > 0)) {
                    Log.i(TAG, "onTextChanged: both fields have values");
                    loginButton.setEnabled(true);
                } else {
                    loginButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };

        userField.addTextChangedListener(myTextWatcher);
        passField.addTextChangedListener(myTextWatcher);

    }
}


//TODO : Now !
// Design:
// Check if both fields have values in them. If yes ->  enable login button
// If login button is pressed check wether both fields acutally match the critiria and return error messages
//      - criteria :
//          - mail matches: android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
//          - check if password is numeric and 6 chars long
