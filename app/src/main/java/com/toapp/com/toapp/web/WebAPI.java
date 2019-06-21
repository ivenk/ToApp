package com.toapp.com.toapp.web;

import com.toapp.Todo;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface WebAPI {

    // POST: api/todos
    boolean createTodos(List<Todo> todos);

    //GET api/todos
    List<Todo> readAllTodos() throws JSONException, IOException;

    //GET: api/todos
    Todo readTodo(int id);

    boolean updateTodo(int id, Todo newVersion);

    boolean deleteTodo(int id);

    //boolean autenticateUser();
}
