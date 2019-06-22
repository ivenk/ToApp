package com.toapp.com.toapp.web;

import com.toapp.data.Todo;
import com.toapp.data.User;

import java.util.List;

public interface WebAPI {

    // POST: api/todos
    boolean createTodos(List<Todo> todos);

    //GET api/todos
    List<Todo> readAllTodos();

    //GET: api/todos
  //  Todo readTodo(int id);

 //   boolean updateTodo(int id, Todo newVersion);

 //   boolean deleteTodo(int id);

    //boolean autenticateUser();

    boolean deleteAllTodos();

    boolean authenticateUser(User user);

}
