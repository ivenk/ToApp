package com.toapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

/** Data object to represent a "to do". Just for data storage */

@Entity
@TypeConverters(DateConverter.class)
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
    private Date dueDate;

    public Todo(int id, String name, String description, boolean done, boolean favourite, Date dueDate) {
        this.id = id;
        this.name =name;
        this.description = description;
        this.done = done;
        this.favourite = favourite;
        this.dueDate = dueDate;
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
