package com.toapp.com.toapp.web;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UnmarshallHelper {
    private static final String TAG = "UnmarshallHelper";

    public static JSONArray unmarshall(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder strBuilder = new StringBuilder();
        try {
            String line;
            while((line = br.readLine()) != null) {
                strBuilder.append(line);
            }
            return new JSONArray(strBuilder.toString());
        }
        catch (IOException ioe) {
            Log.e(TAG, "unmarshall: ", ioe);
            return null;
        } catch (JSONException jse) {
            Log.e(TAG, "unmarshall: ", jse);
            return null;
        }
    }
}
