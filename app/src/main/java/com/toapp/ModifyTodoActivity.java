package com.toapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.toapp.com.toapp.web.WebOperator;
import com.toapp.data.AppDatabase;
import com.toapp.data.Todo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


public class ModifyTodoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private final String TAG = "ModifyTodoActivity";

    private boolean dateUpdated;
    private boolean timeUpdated;

    private Todo todo;

    private int date1;
    private int date2;
    private int date3;

    private int time1;
    private int time2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_todo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Selected");

        dateUpdated = false;
        timeUpdated = false;

        Intent intent = getIntent();
        try {
            String jsTodo = intent.getStringExtra("todo");
            if (jsTodo != null)
                todo = new Todo(new JSONObject(jsTodo));
        } catch(JSONException jse) {
            Log.e(TAG, "onCreate: JSONException occurred while trying to recreate the passed todo object", jse);
        }

        if (todo == null) {
            Log.e(TAG, "onCreate: No todo to modify was given. Returning");
            finish();
        } else {
            ((TextView) findViewById(R.id.title_input_label)).setText(todo.getName());
            ((TextView)findViewById(R.id.description_input_label)).setText(todo.getDescription());
            ((Switch)findViewById(R.id.favourite_switch)).setChecked(todo.isFavourite());


            Date d = new Date(todo.getDueDate());
            ((TextView)findViewById(R.id.input_date)).setText("" + d.toString());
            ((TextView)findViewById(R.id.input_time)).setText("" + d.toString());
        }
    }

    public void onDateClicked(View view) {
        Log.i(TAG, "onDateClicked: ");
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onTimeClicked(View view) {
        Log.i(TAG, "onTimeClicked: ");
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Log.i(TAG, "onDateSet: with i: " + i + "i1" + i1 + "i2"+ i2);
        dateUpdated = true;

        this.date1 = i;
        this.date2 = i1;
        this.date3 = i2;

        ((TextView)findViewById(R.id.input_date)).setText("" + i2 + "." + i1 + "." + i);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Log.i(TAG, "onTimeSet: got called !!!");
        timeUpdated = true;

        this.time1 = i;
        this.time2 = i1;
        //TODO improve time format
        ((TextView)findViewById(R.id.input_time)).setText("" + i + ":" + i1);
    }

    public void onCreateButton(View view) {
        String title = ((TextView)findViewById(R.id.title_input_label)).getText().toString();
        String description = ((TextView)findViewById(R.id.description_input_label)).getText().toString();
        Boolean favourite = ((Switch)findViewById(R.id.favourite_switch)).isChecked();

        long dateTime;
        if(timeUpdated || dateUpdated) {
            //TODO mind that only one might have been set !!! BUGG HERE !!
            dateTime = new Date(date1, date2, date3, time1, time2).getTime();
        } else {
            dateTime = todo.getDueDate();
        }

        Todo newTodo = new Todo(todo.getId(),title, description, false, favourite, dateTime);

        new LocalTodoUpdater().execute(newTodo);
        Intent result = new Intent();
        result.putExtra("todo", newTodo.toJSON().toString());
        setResult(RESULT_OK, result);
        finish();
    }

    public class LocalTodoUpdater extends AsyncTask<Todo, Void, Void> {
        @Override
        protected Void doInBackground(Todo... todos) {
            AppDatabase.getInstance(getApplicationContext()).todoDao().update(todos[0]);
            return null;
        }
    }
}