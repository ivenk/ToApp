package com.toapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/** Data object to represent a "to do". Just for data storage */

@Entity
public class Todo {
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
    // TODO: Research if Date is deprecated and if it includes a time ?
    //Date dueDate

    public Todo(int id, String name, String description, boolean done, boolean favourite) {
        this.id = id;
        this.name =name;
        this.description = description;
        this.done = done;
        this.favourite = favourite;
    }

    @Ignore
    public Todo(String name, String description, boolean done, boolean favourite) {
        this.name = name;
        this.description = description;
        this.done = done;
        this.favourite = favourite;
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
}
