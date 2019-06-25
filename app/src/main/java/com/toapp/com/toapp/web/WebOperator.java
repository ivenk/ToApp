package com.toapp.com.toapp.web;

import android.util.Log;

import com.toapp.data.Todo;
import com.toapp.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class WebOperator implements WebAPI {
    public final String TAG = "WebOperator";

    @Override
    public boolean createTodos(List<Todo> todos) {
        for (Todo t : todos) {
            WebConnector webConnector = new WebConnector();
            webConnector.createTodo(t.toJSON());
        }
        return true;
    }

    @Override
    public List<Todo> readAllTodos() {
        Log.i(TAG, "readAllTodos: Weboperator called");
        WebConnector webConnector = new WebConnector();
        List<JSONObject> rawTodos = webConnector.readAllTodos();
        if(rawTodos == null) //something went wrong earlier, nothing to do here. Error was thrown already.
            return null;

        // build todos
        List<Todo> result = new ArrayList<>();
        for (JSONObject jsobject: rawTodos) {
            result.add(new Todo(jsobject));
        }
        return result;
    }

    @Override
    public boolean deleteAllTodos() {
        Log.i(TAG, "deleteAllTodos: Weboperator.deleteAllTodos() called!");
        WebConnector webConnector = new WebConnector();
        return webConnector.deleteAllTodos();
    }

    @Override
    public boolean authenticateUser(User user) {
        WebConnector webConnector = new WebConnector();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pwd", user.getPwd());
            jsonObject.put("email", user.getEmail());
            return webConnector.authenticateUser(jsonObject);
        } catch (JSONException jse) {
            Log.e(TAG, "authenticateUser: JSONException occured while trying to marshall user." + jse);
        }
        return false;
    }

    @Override
    public boolean checkAvailability() {
        WebConnector webConnector = new WebConnector();
        boolean b = webConnector.checkAvailability();
        Log.i(TAG, "checkAvailability: returned " + b);
        return b;
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
