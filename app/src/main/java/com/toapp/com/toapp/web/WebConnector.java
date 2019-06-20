package com.toapp.com.toapp.web;

import android.util.Log;

import androidx.annotation.Nullable;

import com.toapp.Todo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebConnector implements WebAPI {
    private final String TAG = "WebConnector";

    private final String BASEURI = "http://localhost:8080/api/";
    private final String TODOENPOINT = "todos";
    private final String USERENTPOINT = "users";



    @Override
    public boolean createTodos(List<Todo> todos) {
        return false;
    }

    @Override
    public List<Todo> readAllTodos() throws MalformedURLException, IOException{
        List<Todo> results = new ArrayList<>();
        URL url = new URL(BASEURI + TODOENPOINT);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            //TODO read and unmarshall stream
        } catch (MalformedURLException mue) {
            Log.e(TAG, "readAllTodos: ", mue);
        } catch (IOException ioe){
            Log.e(TAG, "readAllTodos: ", ioe);
        }finally {
            urlConnection.disconnect();
        }
        return results;
    }

    @Override
    public Todo readTodo(int id) {
        return null;
    }

    @Override
    public boolean updateTodo(int id, Todo newVersion) {
        return false;
    }

    @Override
    public boolean deleteTodo(int id) {
        return false;
    }
}
