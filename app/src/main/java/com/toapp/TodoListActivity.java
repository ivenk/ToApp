package com.toapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.toapp.com.toapp.web.WebOperator;
import com.toapp.data.AppDatabase;
import com.toapp.data.Todo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TodoListActivity extends AppCompatActivity {
    private final String TAG = "TodoListActivity";

    private List<Todo> todos;
    ViewGroup scrollLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Your todos");

        Intent intent = getIntent();
        boolean online = intent.getBooleanExtra("online", false);
        if(!online) {
            Toast.makeText(this, "There seems to be no connection to the internet. Working offline ...", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //TODO: check with web
        //    tdos = new ArrayList<>();
        //TODO : Think about mutli threading

        // not super sure why viewgroup instead of linear layout
        scrollLayout = findViewById(R.id.scroll_layout);
        // remove all children
        scrollLayout.removeViewsInLayout(0, scrollLayout.getChildCount());

        new LocalTodoGetter().execute(this);
    }

    // gets called once the create_new_todo button is clicked
    public void onCreateNewTodo(View view) {
        // launch activity for creating todos
        Intent intent = new Intent(this, DetailTodoActivity.class);
        startActivity(intent);
    }
    
    public void onTodoSelected(View view) {
        try {
            Log.i(TAG, "onTodoSelected: view context : "+ view.getContext());
            int id = Integer.parseInt(((TextView)view.findViewById(R.id.customScrollTodoId)).getText().toString());

            AppDatabase db = AppDatabase.getInstance(this);
            Todo todo = db.todoDao().getById(id);

            // There might be a better way to do this
            Intent intent = new Intent(this, DetailTodoActivity.class);
            intent.putExtra("id", todo.getId());
            intent.putExtra("name", todo.getName());
            intent.putExtra("description", todo.getDescription());
            intent.putExtra("done", todo.isDone());
            intent.putExtra("favourite", todo.isFavourite());
            intent.putExtra("dueDate", new Long(todo.getDueDate()));

            startActivity(intent);
        } catch (ClassCastException cce) {
            Log.e(TAG, "onTodoSelected: expected id of type int in tag.", cce);
            return;
        } catch(NullPointerException npe) {
            Log.e(TAG, "onTodoSelected: Could not retrieve intent values.", npe);
            return;
        }
    }

    private void displayTodosFromCSE(List<CustomScrollElement> todos) {

    }

    private void displayTodosFromTodo(List<Todo> todos){
        List<CustomScrollElement> customScrollElements = convertToCSE(todos);
        for (CustomScrollElement c : customScrollElements) {
            scrollLayout.addView(c);
        }
    }

    private List<CustomScrollElement> convertToCSE(List<Todo> todos) {
        List<CustomScrollElement> list = new ArrayList<>();
        for (Todo t : todos) {
            list.add(new CustomScrollElement(this, t.getName(), new Date(t.getDueDate()).toString(), t.isFavourite(), t.getId()));
        }
        return list;
    }

    /**
     * Cleares all todos on the server and pushes the local todos.
     */
    public class RemoteTodoPusher extends AsyncTask<Todo, Void, Boolean> {
        private final String TAG = "TodoPusher";

        @Override
        protected Boolean doInBackground(Todo... todos) {
            WebOperator webOperator = new WebOperator();
            webOperator.deleteAllTodos();
            return webOperator.createTodos(Arrays.asList(todos));
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Log.i(TAG, "onPostExecute: Returned :" + result);
        }
    }

    public class LocalTodoGetter extends AsyncTask<Context, Void, List<Todo>> {

        @Override
        protected List<Todo> doInBackground(Context... contexts) {

            return AppDatabase.getInstance(contexts[0]).todoDao().getAll();
        }

        @Override
        protected void onPostExecute(List<Todo> inTodos) {
            super.onPostExecute(inTodos);
            todos = inTodos;
            displayTodosFromTodo(inTodos);

            new RemoteTodoPusher().execute(todos.toArray(new Todo[todos.size()]));
        }
    }

}
