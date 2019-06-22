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
import android.widget.TextView;

import com.toapp.R;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginActivity";

    EditText userField;
    EditText passField;
    Button loginButton;
    TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // find our ui components
        loginButton = findViewById(R.id.login);
        userField = findViewById(R.id.username);
        passField = findViewById(R.id.password);
        errorMessage = findViewById(R.id.login_error);

        // we want to watch input to both fields
        TextWatcher myTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG, "onTextChanged: Called !");

                errorMessage.setVisibility(View.INVISIBLE);

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

    public void onLoginButtonClicked(View view) {
        Log.i(TAG, "onLoginButtonClicked: Login click registered");

        if (!isValidMailAdress(userField.getText().toString())) {
            showLoginErrorMessage(getString(R.string.invalid_mail));
            return;
        }
        Log.i(TAG, "onLoginButtonClicked: Mail format valid !");

        if (passField.getText().length() != 6) {
            showLoginErrorMessage(getString(R.string.invalid_password));
            return;
        }
        Log.i(TAG, "onLoginButtonClicked: Password format valid !");

        //TODO do login
        Log.i(TAG, "onLoginButtonClicked: Trying to authenticate user");
    }

    private boolean isValidMailAdress(String mailAdress) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(mailAdress).matches();
    }

    private void showLoginErrorMessage(String errorMessage) {
        this.errorMessage.setVisibility(View.VISIBLE);
        this.errorMessage.setText(errorMessage);
        // TODO: maybe check if the text actually fits ?
    }
}