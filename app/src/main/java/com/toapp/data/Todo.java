package com.toapp.data;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.json.JSONException;
import org.json.JSONObject;

/** Data object to represent a "to do". Just for data storage */
@Entity
public class Todo implements IJsonable, IJSONBuildable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo (name="description")
    private String description;
    @ColumnInfo(name="done")
    private boolean done;
    @ColumnInfo(name="favourite")
    private boolean favourite;
    @ColumnInfo(name="date")
    private long dueDate;

    public Todo(int id, String name, String description, boolean done, boolean favourite, long dueDate) {
        this.id = id;
        this.name =name;
        this.description = description;
        this.done = done;
        this.favourite = favourite;
        this.dueDate = dueDate;
    }

    @Ignore
    public Todo(String name, String description, boolean done, boolean favourite, long dueDate) {
        this.name = name;
        this.description = description;
        this.done = done;
        this.favourite = favourite;
        this.dueDate = dueDate;
    }

    // this might be a bad practice ...
    @Ignore
    public Todo(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("id");
            this.name = jsonObject.getString("name");
            this.description = jsonObject.getString("description");
            this.dueDate = jsonObject.getLong("expiry"); // Due to naming convention on server side
            this.done = jsonObject.getBoolean("done");
            this.favourite = jsonObject.getBoolean("favourite");
        } catch (JSONException jse) {
            Log.e("Todo", "Todo: Todo could not be build due to JSONException.", jse);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return done;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("name", name);
            jsonObject.put("description" , description);
            jsonObject.put("done", done);
            jsonObject.put("favourite", favourite);
            jsonObject.put("expiry", dueDate); // Due to naming convention on server side
        } catch (JSONException jse) {
            Log.e("Todo", "toJSON: JSONException occured while trying to create JSON representation of todo", jse);
        }
        return jsonObject;
    }
}
