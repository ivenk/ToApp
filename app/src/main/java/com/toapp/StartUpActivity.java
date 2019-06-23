package com.toapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.toapp.com.toapp.web.WebOperator;
import com.toapp.data.User;
import com.toapp.login.LoginActivity;

public class StartUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        // Check internet connection.
        ConnectionVerifier connectionVerifier = new ConnectionVerifier();
        connectionVerifier.execute();
    }

    private void connectionCheckResult(Boolean success) {
        if(success) {
            // load login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            // load listactivity
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
        }
    }

    public class ConnectionVerifier extends AsyncTask<Void, Void, Boolean> {
        private final String TAG = "ConnectionVerifier";

        @Override
        protected Boolean doInBackground(Void... voids) {
            WebOperator webOperator = new WebOperator();
            return webOperator.checkAvailability();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            connectionCheckResult(result);
        }
    }

}
