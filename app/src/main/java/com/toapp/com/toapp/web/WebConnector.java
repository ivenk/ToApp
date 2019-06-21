package com.toapp.com.toapp.web;

import android.util.Log;

import androidx.annotation.Nullable;

import com.toapp.Todo;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class WebConnector{
    private final String TAG = "WebConnector";

    private final String BASEURI = "http://10.0.2.2:8080/api/";
    private final String TODOENPOINT = "todos";
    private final String USERENTPOINT = "users";

    public boolean createTodos(List<Todo> todos) {
        return false;
    }

    public List<JSONObject> readAllTodos() throws IOException{
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(BASEURI + TODOENPOINT);
            Log.i(TAG, "readAllTodos: connection url is " + url.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
          //  urlConnection.connect();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            if(in == null) {
                Log.e(TAG, "readAllTodos: fail !!!");
            }
            return UnmarshallHelper.unmarshall(in);
            //TODO read and unmarshall stream
        } catch (MalformedURLException mue) {
            Log.e(TAG, "readAllTodos: Malformed url !", mue);
            throw mue;
        } catch (IOException ioe){
            Log.e(TAG, "readAllTodos: IOException ocured while trying to unmarshall input stream.", ioe);
            throw ioe;
        }finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }
    }

    public Todo readTodo(int id) {
        return null;
    }

    public boolean updateTodo(int id, Todo newVersion) {
        return false;
    }

    public boolean deleteTodo(int id) {
        return false;
    }
}
