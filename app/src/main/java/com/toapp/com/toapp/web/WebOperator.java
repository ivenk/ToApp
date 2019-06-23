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

            // build todos
            List<Todo> result = new ArrayList<>();
            for (JSONObject jsobject: rawTodos) {
                int id = jsobject.getInt("id");
                String name = jsobject.getString("name");
                String description = jsobject.getString("description");
                Long dueDate = jsobject.getLong("expiry");
                Boolean done = jsobject.getBoolean("done");
                Boolean favourite = jsobject.getBoolean("favourite");
                result.add(new Todo(id, name, description, done, favourite, dueDate));
            }
            return result;
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

    @Override
    public boolean authenticateUser(User user) {
        WebConnector webConnector = new WebConnector();
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pwd", user.getPwd());
            jsonObject.put("email", user.getEmail());
            jsonArray.put(jsonObject);
            return webConnector.authenticateUser(jsonArray);
        } catch (JSONException jse) {
            Log.e(TAG, "authenticateUser: JSONException occured while trying to marshall user." + jse);
        }
        return false;
    }

    @Override
    public boolean checkAvailability() {
        return false;
    }
}
