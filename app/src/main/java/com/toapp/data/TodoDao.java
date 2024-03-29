package com.toapp.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.toapp.data.Todo;

import java.util.List;

@Dao
public interface TodoDao {
    @Query("SELECT * FROM Todo")
    List<Todo> getAll();

    @Insert
    void insert(Todo todo);

    @Delete
    void delete(Todo todo);

    @Update
    void update(Todo todo);

    @Query("SELECT * FROM Todo WHERE id=:inId")
    Todo getById(int inId);

    @Query("DELETE FROM Todo")
    void deleteAllTodos();
}
