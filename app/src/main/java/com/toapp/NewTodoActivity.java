package com.toapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;

public class NewTodoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private final String TAG = "NewTodoActivity";

    private int date1;
    private int date2;
    private int date3;

    private int time1;
    private int time2;

    private int id;
    private String name;
    private String description;
    private Boolean done;
    private Boolean favourite;
    private Long dueDate;

    private Boolean create;

    //TODO still some buggs here! What if we only want to change the time not the date in update ? will not work currently.
    private boolean dateTimeUpdated;

    // TODO: Change the view. The activity_new_todo.xml currently includes hardcoded width for the <include> element

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");

        dateTimeUpdated = false;

        Intent intent = getIntent();
        this.id = intent.getIntExtra("id", -1);
        Log.i(TAG, "onCreate: id is : " + this.id);
        if (this.id == -1) {
            // if no id was given we assume no values passed and we are trying to create a new object instead of inspecting an existing one.
            create = true;
        } else {
            this.name = intent.getStringExtra("name");
            this.description = intent.getStringExtra("description");
            this.done = intent.getBooleanExtra("done", false);
            this.favourite = intent.getBooleanExtra("favourite", false);
            this.dueDate = intent.getLongExtra("dueDate", -1);

            ((TextView) findViewById(R.id.title_input_label)).setText(name);
            ((TextView)findViewById(R.id.description_input_label)).setText(description);
            ((Switch)findViewById(R.id.favourite_switch)).setChecked(favourite);


            Date d = new Date(dueDate);
            ((TextView)findViewById(R.id.input_date)).setText("" + d.toString());
            ((TextView)findViewById(R.id.input_time)).setText("" + d.toString());
        }

        setContentView(R.layout.activity_new_todo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("New Todo");


        if(!create) {
            //TODO String should be in a xml
            ((Button)findViewById(R.id.create_button)).setText("Update");
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

    public void onCreateButton(View view) {
        //TODO get all infos from page
        TextView titleView = findViewById(R.id.title_input_label);
        TextView descriptionView = findViewById(R.id.description_input_label);
        Switch favouriteView = findViewById(R.id.favourite_switch);

        if (titleView.getText().toString() == "") {
            Toast.makeText(getApplicationContext(), "Please provide a title for your todo !", Toast.LENGTH_LONG).show();
            return;
        }

        if(create) { // creation mode
            // if crucial information was not provided no t..do will be created
            if (!dateTimeUpdated) {
                Toast.makeText(getApplicationContext(), "Please provide a date for your todo !", Toast.LENGTH_LONG).show();
                return;
            }
            Todo t = new Todo(titleView.getText().toString(), descriptionView.getText().toString(), false, favouriteView.isChecked(), new Date(date1, date2, date3, time1, time2).getTime());
            AppDatabase.getInstance(getApplicationContext()).todoDao().insert(t);
        } else { // update mode
            long dateTime;
            // if no new time and date is given we asume the old ones are correct.
            if (dateTimeUpdated) {
                //TODO mind that only one might have been set !!!
                dateTime = dueDate;
            } else {
                dateTime = new Date(date1, date2, date3, time1, time2).getTime();
            }
            Todo t = new Todo(titleView.getText().toString(), descriptionView.getText().toString(), false, favouriteView.isChecked(), dateTime);
            AppDatabase.getInstance(getApplicationContext()).todoDao().update(t);
        }
        finish();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Log.i(TAG, "onDateSet: with i: " + i + "i1" + i1 + "i2"+ i2);
        dateTimeUpdated = true;

        this.date1 = i;
        this.date2 = i1;
        this.date3 = i2;

        ((TextView)findViewById(R.id.input_date)).setText("" + i2 + "." + i1 + "." + i);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Log.i(TAG, "onTimeSet: got called !!!");
        dateTimeUpdated = true;

        this.time1 = i;
        this.time2 = i1;
        //TODO improve time format
        ((TextView)findViewById(R.id.input_time)).setText("" + i + ":" + i1);
    }

}
