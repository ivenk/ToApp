package com.toapp.com.toapp.web;

import android.util.Log;

import com.toapp.Todo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class WebOperator implements WebAPI {
    public final String TAG = "WebOperator";

    @Override
    public boolean createTodos(List<Todo> todos) {
        return false;
    }

    @Override
    public List<Todo> readAllTodos() throws IOException, JSONException {
        Log.i(TAG, "readAllTodos: Weboperator called");
        WebConnector webConnector = new WebConnector();
        List<JSONObject> rawTodos = webConnector.readAllTodos();
        try {
            int id = rawTodos.get(0).getInt("id");
            Log.i(TAG, "readAllTodos: Found id was : " + id);
        } catch(JSONException jse) {
            throw jse;
        }

        Log.i(TAG, "readAllTodos: " + rawTodos.toString());

        return null;
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
