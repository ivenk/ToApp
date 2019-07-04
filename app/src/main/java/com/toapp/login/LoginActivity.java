package com.toapp.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.toapp.R;
import com.toapp.TodoListActivity;
import com.toapp.com.toapp.web.WebOperator;
import com.toapp.data.User;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginActivity";

    private EditText userField;
    private EditText passField;
    private Button loginButton;
    private TextView errorMessage;
    private LinearLayout loadingIndicator;

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
        loadingIndicator = findViewById(R.id.loading_indicator);

        // we want to watch input to both fields
        TextWatcher myTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                errorMessage.setVisibility(View.INVISIBLE);

                // if both fields have a value set
                if((charSequence.length() > 0) && (passField.getText().length() > 0)) {
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
        String mail = userField.getText().toString();
        String pass = passField.getText().toString();

        if (!isValidMailAdress(mail)) {
            showLoginErrorMessage(getString(R.string.invalid_mail));
            return;
        }
        Log.i(TAG, "onLoginButtonClicked: Mail format valid !");

        if (pass.length() != 6) {
            showLoginErrorMessage(getString(R.string.invalid_password));
            return;
        }
        Log.i(TAG, "onLoginButtonClicked: Password format valid !");

        Log.i(TAG, "onLoginButtonClicked: Trying to authenticate user");
        UserAuthenticator userAuthenticator = new UserAuthenticator();
        userAuthenticator.execute(new User(mail, pass));

        displayLoading();
    }

    private void authenticationResult(Boolean result) {
        Log.i(TAG, "authenticationResult: called with : " + result);
        if(!result) {
            hideLoading();
            showLoginErrorMessage(getString(R.string.login_failed));
        } else {
            Intent intent = new Intent(this, TodoListActivity.class);
            intent.putExtra("online", true);
            startActivity(intent);
        }
    }

    private boolean isValidMailAdress(String mailAdress) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(mailAdress).matches();
    }

    private void showLoginErrorMessage(String errorMessage) {
        this.errorMessage.setVisibility(View.VISIBLE);
        this.errorMessage.setText(errorMessage);
    }

    private void displayLoading() {
        loadingIndicator.setVisibility(View.VISIBLE);
        loadingIndicator.bringToFront();
        loginButton.setVisibility(View.GONE);
        hideKeyboard(this);
    }

    private void hideLoading() {
        loadingIndicator.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
    }

    /**
     * Stolen from the great rant on androids keyboard api design : https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
     * @param activity
     */
    private void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public class UserAuthenticator extends AsyncTask<User, Void, Boolean> {
        private final String TAG = "UserAuthenticator";

        @Override
        protected Boolean doInBackground(User... params) {
            try {
                Thread.sleep(2000);
            } catch(InterruptedException ie) {
                Log.e(TAG, "doInBackground: something woke me up", ie);
            }
            WebOperator webOperator = new WebOperator();
            return webOperator.authenticateUser(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Log.w(TAG, "onPostExecute: got : " + result);
            authenticationResult(result);
        }
    }

    //TODO: [Extra] Add max length to password and mail input + add little field at the end of password input to show current length of entered password!
}