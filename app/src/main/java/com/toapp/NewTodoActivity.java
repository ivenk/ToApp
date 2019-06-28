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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewTodoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, ContactScroller.OnFragmentInteractionListener, CustomContactScrollElement.IContactScrollListener {
    private final String TAG = "NewTodoActivity";
    private final int CONTACT_PICKER = 1;

    private int date1;
    private int date2;
    private int date3;

    private int time1;
    private int time2;

    private Todo todo;

    private Boolean create;

    //TODO still some buggs here! What if we only want to change the time not the date in update ? will not work currently.
    private boolean dateTimeUpdated;
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
        if (!dateTimeUpdated) {
            Toast.makeText(getApplicationContext(), "Please provide a date for your todo !", Toast.LENGTH_LONG).show();
            return;
        }
        //TODO the date convertions dont work like this.
        Log.i(TAG, "onCreateButton: Creation date set is : " + new Date(new Date(date1, date2, date3, time1, time2).toInstant().toEpochMilli()));

        Todo t = new Todo(title, description, false, favourite, new Date(date1, date2, date3, time1, time2).getTime(), contacts);
        Intent result = new Intent();
        result.putExtra("todo", t.toJSON().toString());
        setResult(RESULT_OK, result);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONTACT_PICKER) {
            if(resultCode == Activity.RESULT_OK) {
                Uri d = data.getData();
                Cursor cursor = getContentResolver().query(d, null, null, null, null); //managedQuery(d, null, null, null, null);

                if(cursor.moveToFirst()){
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    String hasPhone = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    String number = "";

                    if (hasPhone.equalsIgnoreCase("1")) {
                        Cursor phones = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                null, null);
                        phones.moveToFirst();
                        number = phones.getString(phones.getColumnIndex("data1"));
                    }

                    String email = "";
                    Cursor mailCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID, null, null);
                    mailCursor.moveToFirst();
                    try {
                        email = getString(mailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    } catch (Exception e) {
                        Log.e(TAG, "onActivityResult: We crashed trying to read mail", e);
                        email = "";
                    }

                    Log.i(TAG, "onActivityResult: id : " + id);
                    Log.i(TAG, "onActivityResult: name : " + name);
                    Log.i(TAG, "onActivityResult: number : " + number);
                    Log.i(TAG, "onActivityResult: email : " + email);
                    contactScroller.attachNewContact(new Contact(Integer.parseInt(id), name, number, email));
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
    public void onScrollableCall(int id) {
        contactScroller.onContactDelete(id);
    }




}
