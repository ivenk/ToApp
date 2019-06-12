package com.toapp;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class NewTodoActivity extends AppCompatActivity {
    private final String TAG = "NewTodoActivity";

    // TODO: Change the view. The activity_new_todo.xml currently includes hardcoded width for the <include> element

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");

        setContentView(R.layout.activity_new_todo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("New Todo");
    }

    public void onCreateButton(View view) {
        Log.i(TAG, "onCreateButton: Called! No action implemented yet");

        //TODO get more infos from page
        TextView titleView = findViewById(R.id.title_input_label);
        TextView descriptionView = findViewById(R.id.description_input_label);
        Switch favouriteView = findViewById(R.id.favourite_switch);

        Todo t = new Todo(titleView.getText().toString(), descriptionView.getText().toString(), false, favouriteView.isChecked());
        AppDatabase.getInstance(getApplicationContext()).todoDao().insert(t);
        finish();
    }
}
