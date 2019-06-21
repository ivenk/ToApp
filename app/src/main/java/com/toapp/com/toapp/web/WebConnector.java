package com.toapp.com.toapp.web;

import android.util.Log;

import androidx.annotation.Nullable;

import com.toapp.Todo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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

    @Nullable
    public List<JSONObject> readAllTodos(){
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(BASEURI + TODOENPOINT);
            Log.i(TAG, "readAllTodos: connection url is " + url.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            JSONArray jsonArray = UnmarshallHelper.unmarshall(in);

            List<JSONObject> results = new ArrayList<>();

            for (int i =0; i< jsonArray.length(); i++) {
                results.add(jsonArray.getJSONObject(i));
            }
            return results;
            //TODO read and unmarshall stream
        } catch (JSONException jse){
            Log.e(TAG, "readAllTodos: JSONException occured while trying to parse fetched results to jsonobjects.", jse);
        } catch (MalformedURLException mue) {
            Log.e(TAG, "readAllTodos: Malformed url !", mue);
        } catch (IOException ioe){
            Log.e(TAG, "readAllTodos: IOException ocured while trying to unmarshall input stream.", ioe);
        }finally {
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
        try {
            URL url = new URL(BASEURI + TODOENPOINT);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("DELETE");
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            JSONArray jsonArray = UnmarshallHelper.unmarshall(in);

        } catch (MalformedURLException mue) {
            Log.e(TAG, "deleteAllTodos: ", mue);
        } catch (ProtocolException pte) {
            Log.e(TAG, "deleteAllTodos: ", pte);
        } catch (IOException ioe){
            Log.e(TAG, "deleteAllTodos: ", ioe);
        }
        return false;
    }
}
