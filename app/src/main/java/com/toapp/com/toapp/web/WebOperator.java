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
    public List<Todo> readAllTodos() {
        Log.i(TAG, "readAllTodos: Weboperator called");
        WebConnector webConnector = new WebConnector();
        try {
            List<JSONObject> rawTodos = webConnector.readAllTodos();
            if(rawTodos == null) //something went wrong earlier, nothing to do here. Error was thrown already.
                return null;
            int id = rawTodos.get(0).getInt("id");
            Log.i(TAG, "readAllTodos: Found id was : " + id);
        } catch(JSONException jse) {
            Log.e(TAG, "readAllTodos: JSONException while trying to fetch results from the webserver.", jse);
        }
        return null;
    }

    @Override
    public boolean deleteAllTodos() {
        Log.i(TAG, "deleteAllTodos: Weboperator.deleteAllTodos() called!");
        WebConnector webConnector = new WebConnector();
        return webConnector.deleteAllTodos();
    }
}
