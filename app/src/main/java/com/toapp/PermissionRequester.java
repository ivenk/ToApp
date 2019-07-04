package com.toapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

public class PermissionRequester {

    private final static int REQUEST_MULTIPLE_PERMISSIONS = 124;

    // ------------------------------------------------------------------------------------------------
    // most of this is shamelessly stolen from https://dzone.com/articles/access-all-contacts-using-content-provider-concept
    // great article! difficult to find up to date guides.

    public static void CheckPermissions(final Context context) {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(context, permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Read Contacts");
 //       if (!addPermission(context, permissionsList, Manifest.permission.WRITE_CONTACTS))
 //           permissionsNeeded.add("Write Contacts");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(context, message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ((Activity) context).requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            ((Activity) context).requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_MULTIPLE_PERMISSIONS);
            return;
        }
    }


    private static boolean addPermission(Context context, List<String> permissionsList, String permission)
    {
        if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
        {
            permissionsList.add(permission);
            if (!((Activity)context).shouldShowRequestPermissionRationale(permission)){
                return false;

            }
        }
        return true;
    }


    private static void showMessageOKCancel(Context context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
