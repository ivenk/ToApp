package com.toapp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

public class TodoListActivity extends AppCompatActivity {
    private final String TAG = "TodoListActivity";

    private List<Todo> todos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Your todos");
    }


    @Override
    protected void onStart() {
        super.onStart();
        //TODO: check with web
        //    tdos = new ArrayList<>();
        //TODO : Think about mutli threading

        AppDatabase db = AppDatabase.getInstance(this);
        todos = db.todoDao().getAll();

        // TODO: Display todos in list view
        // not super sure why viewgroup instead of linear layout
        ViewGroup scrollLayout = (ViewGroup) findViewById(R.id.scroll_layout);
        // remove all children
        scrollLayout.removeViewsInLayout(0, scrollLayout.getChildCount());

        for (Todo t : todos) {
            CustomScrollElement cse = new CustomScrollElement(this, t.getName(), new Date(t.getDueDate()).toString(), t.isFavourite());
            cse.setTag(t.getId());
            scrollLayout.addView(cse);
        }
    }

    // gets called once the create_new_todo button is clicked
    public void onCreateNewTodo(View view) {
        // launch activity for creating todos
        Intent intent = new Intent(this, NewTodoActivity.class);
        startActivity(intent);
    }
    
    public void onTodoSelected(View view) {
        Log.i(TAG, "onTodoSelected: WIP");

        try {
            int id = (int) view.getTag();
            AppDatabase db = AppDatabase.getInstance(this);
            Todo todo = db.todoDao().getById(id);

            // There might be a better way to do this
            Intent intent = new Intent(this, NewTodoActivity.class);
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
        }


    }

}
