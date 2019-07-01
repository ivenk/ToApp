package com.toapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.toapp.data.AppDatabase;
import com.toapp.data.Contact;
import com.toapp.data.Todo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewTodoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, ContactScroller.OnFragmentInteractionListener, CustomContactScrollElement.IContactScrollListener {
    private final String TAG = "NewTodoActivity";
    private final int CONTACT_PICKER = 1;

    private int year;
    private int month;
    private int day;

    private int hour;
    private int minute;

    private Todo todo;

    // TODO still some buggs here! What if we only want to change the time not the date in update ? will not work currently.
    private boolean dateUpdated;
    private ContactScroller contactScroller;

    // TODO: Change the view. The activity_new_todo.xml currently includes hardcoded width for the <include> element

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        setContentView(R.layout.activity_new_todo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("New Todo");

        this.contactScroller = (ContactScroller) getSupportFragmentManager().findFragmentById(R.id.contact_list_outer);
        if(this.contactScroller == null){
            Log.e(TAG, "onCreate: ContactScroller fragment could not be located.");
        }

        this.year = 0;
        this.month = 0;
        this.day = 0;
        this.hour = 0;
        this.minute = 0;

        PermissionRequester.CheckPermissions(this);
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
        String title = ((TextView)findViewById(R.id.title_input_label)).getText().toString();
        String description = ((TextView)findViewById(R.id.description_input_label)).getText().toString();
        Boolean favourite = ((Switch)findViewById(R.id.favourite_switch)).isChecked();
        String contacts = contactScroller.getContactsString();

        if (title == "") {
            Toast.makeText(getApplicationContext(), "Please provide a title for your todo !", Toast.LENGTH_LONG).show();
            return;
        }

        // if crucial information was not provided no t..do will be created
        if (!dateUpdated) {
            Toast.makeText(getApplicationContext(), "Please provide a date for your todo !", Toast.LENGTH_LONG).show();
            return;
        }

        Todo t = new Todo(title, description, false, favourite, buildDate().getTime(), contacts);
        Intent result = new Intent();
        result.putExtra("todo", t.toJSON().toString());
        setResult(RESULT_OK, result);

        finish();
    }

    private Date buildDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        if(month != 0)
            calendar.set(Calendar.MONTH, month);
        if(year != 0)
            calendar.set(Calendar.YEAR, year);
        if(day != 0)
            calendar.set(Calendar.DAY_OF_MONTH, day);
        if(hour != 0)
            calendar.set(Calendar.HOUR, hour);
        if(minute != 0)
            calendar.set(Calendar.MINUTE, minute);
        Date date = calendar.getTime();
        Log.i(TAG, "onCreateButton: Date: " + date.toString());
        return date;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Log.i(TAG, "onDateSet: with i: " + i + "i1" + i1 + "i2"+ i2);
        dateUpdated = true;

        this.year = i;
        this.month = i1;
        this.day = i2;

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
        String dateString = sdf.format(buildDate());
        if (dateString == null) {
            dateString = "";
        }
        ((TextView)findViewById(R.id.input_date)).setText(dateString);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Log.i(TAG, "onTimeSet: got called !!!");

        this.hour = i;
        this.minute = i1;

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.GERMANY);
        String dateString = sdf.format(buildDate());
        if (dateString == null) {
            dateString = "";
        }
        ((TextView)findViewById(R.id.input_time)).setText(dateString);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONTACT_PICKER) {
            if(resultCode == Activity.RESULT_OK) {
                Uri d = data.getData();
                Cursor cursor = getContentResolver().query(d, null, null, null, null); //managedQuery(d, null, null, null, null);
                Contact contact = null;
                if(cursor.moveToFirst()) {
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                    int iid = -1;
                    if((id == null)||(id == "")) {
                        Log.e(TAG, "onActivityResult: id for contact could not be retrieved");
                        return;
                    }
                    try {
                        iid = Integer.parseInt(id);
                    } catch (NumberFormatException nfe) {
                        Log.e(TAG, "onActivityResult: id could not be parsed to integer", nfe);
                        return;
                    }
                    contact = ContactReceiver.queryContactResolver(this, iid);
                }
                if(contact != null) {
                    contactScroller.attachNewContact(contact);
                } else {
                    Log.e(TAG, "onActivityResult: Contact could not be created, left with null");
                }
            }
        }
    }

    @Override
    public void startContactPicker() {
        Log.i(TAG, "startContactPicker: called from fragment !");
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, CONTACT_PICKER);
    }

    @Override
    public void onScrollableDeletionCall(int id) {
        contactScroller.onContactDelete(id);
    }

    @Override
    public void onScrollableCall(int id) {
        Log.i(TAG, "onScrollableCall: Advanced contact options requested.");
        //TODO: complete with intent launching.

    }
}
