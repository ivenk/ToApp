package com.toapp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class TodoListActivity extends AppCompatActivity {
    private List<Todo> todos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Todos");
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
            // TODO: implement view for todo in list
            TextView tv = new TextView(this);
            tv.setLayoutParams(new ViewGroup.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
            tv.setText(t.getName());
            scrollLayout.addView(tv);
        }
    }

    // gets called once the create_new_todo button is clicked
    public void onCreateNewTodo(View view) {
        // launch activity for creating todos
        Intent intent = new Intent(this, NewTodoActivity.class);
        startActivity(intent);
    }

}
