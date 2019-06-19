package com.toapp.com.toapp.web;

import com.toapp.Todo;

import java.util.List;

public class WebConnector implements WebAPI {

    @Override
    public boolean createTodos(List<Todo> todos) {
        return false;
    }

    @Override
    public List<Todo> readAllTodos() {
        return null;
    }

    @Override
    public Todo readTodo(int id) {
        return null;
    }

    @Override
    public boolean updateTodo(int id, Todo newVersion) {
        return false;
    }

    @Override
    public boolean deleteTodo(int id) {
        return false;
    }
}
