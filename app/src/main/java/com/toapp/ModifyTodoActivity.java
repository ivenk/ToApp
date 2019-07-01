package com.toapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.toapp.data.AppDatabase;
import com.toapp.data.Contact;
import com.toapp.data.Todo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ModifyTodoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, ContactScroller.OnFragmentInteractionListener, CustomContactScrollElement.IContactScrollListener {
    private final String TAG = "ModifyTodoActivity";

    private final int CONTACT_PICKER = 1;
    public final static int RESULT_OK_DELETE = 5;
    public final static int RESULT_OK_UPDATE = 6;
    public final static int RESULT_FAIL = 7;


    private boolean dateUpdated;
    private boolean timeUpdated;

    private Todo todo;

    private int year;
    private int month;
    private int day;

    private int hour;
    private int minute;

    private ContactScroller contactScroller;

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

        this.contactScroller = (ContactScroller) getSupportFragmentManager().findFragmentById(R.id.modify_contact_list_outer);
        if(this.contactScroller == null){
            Log.e(TAG, "onCreate: ContactScroller fragment could not be located.");
        }

        if (todo == null) {
            Log.e(TAG, "onCreate: No todo to modify was given. Returning");
            setResult(RESULT_FAIL);
            finish();
        } else {
            ((TextView) findViewById(R.id.modify_title_input_label)).setText(todo.getName());
            ((TextView)findViewById(R.id.modify_description_input_label)).setText(todo.getDescription());
            ((Switch)findViewById(R.id.modify_favourite_switch)).setChecked(todo.isFavourite());
            ((Switch)findViewById(R.id.modify_done_switch)).setChecked(todo.isDone());

            Date d = new Date(todo.getDueDate());
            SimpleDateFormat sdfD = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
            SimpleDateFormat sdfT = new SimpleDateFormat("HH:mm", Locale.GERMANY);
            ((TextView)findViewById(R.id.modify_input_date)).setText(sdfD.format(d));
            ((TextView)findViewById(R.id.modify_input_time)).setText(sdfT.format(d));


            Calendar c = Calendar.getInstance();
            c.setTime(d);
            this.minute = c.get(Calendar.MINUTE);
            this.hour = c.get(Calendar.HOUR);
            this.day = c.get(Calendar.DAY_OF_MONTH);
            this.month = c.get(Calendar.MONTH);
            this.year = c.get(Calendar.YEAR);

            Log.i(TAG, "onCreate: contacts found :" + todo.getContacts());
            if(todo.getContacts() != null) {
                // fill contactScroller
                for (String str: todo.getContacts().split(",")) {
                    Log.i(TAG, "onCreate: in loop" + str);
                    if((str == null)||(str == "")){ return ;}
                    try {
                        int id = Integer.parseInt(str);
                        contactScroller.attachNewContact(ContactReceiver.queryContactResolver(this, id));
                    } catch(NumberFormatException nfe) {
                        Log.e(TAG, "onCreate: NumberFormatException occurred while trying to parse to an integer; value was : " + str, nfe);
                    }
                }
            }
        }

        PermissionRequester.CheckPermissions(this);
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

        this.year = i;
        this.month = i1;
        this.day = i2;

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
        String dateString = sdf.format(buildDate());
        if (dateString == null) {
            dateString = "";
        }
        ((TextView)findViewById(R.id.modify_input_date)).setText(dateString);
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
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Log.i(TAG, "onTimeSet: got called !!!");
        timeUpdated = true;

        this.hour = i;
        this.minute = i1;

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.GERMANY);
        String dateString = sdf.format(buildDate());
        if (dateString == null) {
            dateString = "";
        }
        ((TextView)findViewById(R.id.modify_input_time)).setText(dateString);
    }

    public void onCreateButton(View view) {
        String title = ((TextView)findViewById(R.id.modify_title_input_label)).getText().toString();
        String description = ((TextView)findViewById(R.id.modify_description_input_label)).getText().toString();
        Boolean favourite = ((Switch)findViewById(R.id.modify_favourite_switch)).isChecked();
        Boolean done = ((Switch)findViewById(R.id.modify_done_switch)).isChecked();

        String contacts = contactScroller.getContactsString();

        long dateTime;

        if(timeUpdated || dateUpdated) {
            //TODO mind that only one might have been set !!! BUGG HERE !!
            dateTime = buildDate().getTime();
        } else {
            dateTime = todo.getDueDate();
        }

        Todo newTodo = new Todo(todo.getId(),title, description, done, favourite, dateTime, contacts);
        Intent result = new Intent();
        result.putExtra("todo", newTodo.toJSON().toString());
        setResult(RESULT_OK_UPDATE, result);
        finish();
    }

    public void onDeleteButton(View view) {
        if (todo == null) {
            Log.e(TAG, "onDeleteButton: Something went wrong. todo should never be null");
            return;
        }
        // show confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent result = new Intent();
                result.putExtra("todo", todo.toJSON().toString());
                setResult(RESULT_OK_DELETE, result);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i(TAG, "onClick: Deletion event was cancelled.");
            }
        });
        builder.setMessage("Are you sure ?");
        AlertDialog dialog = builder.create();
        dialog.show();
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
        contactScroller.showAdvancedContactDialog(id);
    }
}
