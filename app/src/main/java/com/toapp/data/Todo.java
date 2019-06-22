package com.toapp.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

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
}
