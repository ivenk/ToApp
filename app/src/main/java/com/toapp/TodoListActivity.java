package com.toapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.toapp.com.toapp.web.WebOperator;

import org.json.JSONException;

import java.io.IOException;
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

        //TODO remove this !!

        new Thread() {
            @Override
            public void run() {
                super.run();
                WebOperator webOperator = new WebOperator();
                try {
                    webOperator.readAllTodos();
                } catch (IOException e) {
                    Log.e(TAG, "onCreate: Could not reach webserver !", e);
                }catch (JSONException jse) {
                    Log.e(TAG, "run: Could not parse received JSON !", jse);
                }
            }
        }.start();
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
            LinearLayout cse = new CustomScrollElement(this, t.getName(), new Date(t.getDueDate()).toString(), t.isFavourite(), t.getId());
            scrollLayout.addView(cse);
        }
    }

    // gets called once the create_new_todo button is clicked
    public void onCreateNewTodo(View view) {
        // launch activity for creating todos
        Intent intent = new Intent(this, DetailTodoActivity.class);
        startActivity(intent);
    }
    
    public void onTodoSelected(View view) {
        Log.i(TAG, "onTodoSelected: WIP");
        Log.i(TAG, "onTodoSelected: view:" + view.toString());
        Log.i(TAG, "onTodoSelected: view:" + view.getId());


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

}
