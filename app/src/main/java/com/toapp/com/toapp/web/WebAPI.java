package com.toapp.com.toapp.web;

import com.toapp.Todo;

import java.util.List;

public interface WebAPI {

    boolean createTodos(List<Todo> todos);

    List<Todo> readAllTodos();

    Todo readTodo(int id);

    boolean updateTodo(int id, Todo newVersion);

    boolean deleteTodo(int id);

    //boolean autenticateUser();
}
