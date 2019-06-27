package com.toapp.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents a contact.
 */
public class Contact implements IJsonable, IJSONBuildable {
    private final String TAG ="Contact";

    private int id;
    private String name;
    private String number;
    private String email;

    public Contact() {

    }

    public Contact(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("id");
            this.name = jsonObject.getString("name");
            this.number = jsonObject.getString("number");
            this.email = jsonObject.getString("email");
        }catch(JSONException jse ){
            Log.e(TAG, "Contact: JSONException occurred while trying to build contact from JSONObject " + jsonObject.toString(), jse);
            return;
        }
    }


    public Contact(int id, String name, String number, String email) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", this.id);
            jsonObject.put("name", this.name);
            jsonObject.put("number", this.number);
            jsonObject.put("email", this.email);
        } catch (JSONException jse) {
            Log.e(TAG, "toJSON: JSONException occurred while trying to parse contact " + this.toString() + " to JSON.");
            return null;
        }
        return jsonObject;
    }
}
