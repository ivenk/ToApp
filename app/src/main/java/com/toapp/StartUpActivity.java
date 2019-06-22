package com.toapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import com.toapp.login.LoginActivity;

public class StartUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        // Check internet connection.
        if(true) {
            // load login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            // load listactivity
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
        }
    }
}
