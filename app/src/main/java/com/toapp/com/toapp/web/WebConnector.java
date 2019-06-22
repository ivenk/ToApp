package com.toapp.com.toapp.web;

import android.util.Log;

import androidx.annotation.Nullable;

import com.toapp.Todo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WebConnector{
    private final String TAG = "WebConnector";

    private final String BASEURI = "http://10.0.2.2:8080/api/";
    private final String TODOENPOINT = "todos";
    private final String USERENTPOINT = "users";

    public boolean createTodos(List<Todo> todos) {
        return false;
    }

    @Nullable
    public List<JSONObject> readAllTodos(){
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(BASEURI + TODOENPOINT);
            Log.i(TAG, "readAllTodos: connection url is " + url.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return UnmarshallHelper.unmarshall(in);
        } catch (MalformedURLException mue) {
            Log.e(TAG, "readAllTodos: Malformed url !", mue);
        } catch (IOException ioe){
            Log.e(TAG, "readAllTodos: IOException ocured while trying to unmarshall input stream.", ioe);
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }
        return null;
    }

    public Todo readTodo(int id) {
        Log.e(TAG, "deleteTodo: Not yet implemented !");
        return null;
    }

    public boolean updateTodo(int id, Todo newVersion) {
        Log.e(TAG, "deleteTodo: Not yet implemented !");
        return false;
    }

    public boolean deleteTodo(int id) {
        Log.e(TAG, "deleteTodo: Not yet implemented !");
        return false;
    }
    
    public boolean deleteAllTodos() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(BASEURI + TODOENPOINT);//"http://10.0.2.2:9876");//BASEURI + TODOENPOINT);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String result = reader.readLine(); // Result is supposed to be one line. If its more something is wrong and we are not interested.
            return Boolean.parseBoolean(result);
        } catch (MalformedURLException mue) {
            Log.e(TAG, "deleteAllTodos: ", mue);
        } catch (ProtocolException pte) {
            Log.e(TAG, "deleteAllTodos: ", pte);
        } catch (IOException ioe) {
            Log.e(TAG, "deleteAllTodos: ", ioe);
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException ioe) {
                Log.e(TAG, "deleteAllTodos: IOException occured while trying to close bufferedreader", ioe);
            }
        }
        return false;
    }
}
