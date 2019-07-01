package com.toapp;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.toapp.data.Contact;

public class ContactReceiver {

    public static final String TAG = "ContactReceiver";

    public static Contact queryContactResolver(Context context, int id) {
        Contact contact = new Contact();
        Cursor query = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts._ID + " = ?", new String[]{Integer.toString(id)}, null);
        if(query.moveToFirst()) {
            contact.setId(id);

            String name = query.getString(query.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            contact.setName(name);

            String hasPhone = query.getString(query.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            String number = "";

            if (hasPhone.equalsIgnoreCase("1")) {
                Cursor phones = context.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                        null, null);
                phones.moveToFirst();
                number = phones.getString(phones.getColumnIndex("data1"));
            }

            contact.setNumber(number);
        }

        String email = "";
        Cursor mailCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID+ " = "+id, null, null);
        mailCursor.moveToFirst();
        try {
            email = mailCursor.getString(mailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
        } catch (Exception e) {
            Log.e(TAG, "onActivityResult: We crashed trying to read mail", e);
            email = "";
        }
        contact.setEmail(email);
        return contact;
    }
}
