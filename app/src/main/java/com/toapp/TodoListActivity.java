package com.toapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.toapp.com.toapp.web.WebOperator;
import com.toapp.data.AppDatabase;
import com.toapp.data.Todo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TodoListActivity extends AppCompatActivity {
    private final String TAG = "TodoListActivity";
    public final static int MODIFY = 1;
    public final static int CREATE = 2;

    private boolean initSync = true;

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
        // not super sure why viewgroup instead of linear layout
        scrollLayout = findViewById(R.id.scroll_layout);
        // remove all children
        scrollLayout.removeViewsInLayout(0, scrollLayout.getChildCount());

        new LocalShowAllTodos().execute();
    }

    // gets called once the create_new_todo button is clicked
    public void onCreateNewTodo(View view) {
        // launch activity for creating todos
        Intent intent = new Intent(this, NewTodoActivity.class);
        startActivityForResult(intent, CREATE);
    }
    
    public void onTodoSelected(View view) {
        try {
            Log.i(TAG, "onTodoSelected: view context : "+ view.getContext());
            int id = Integer.parseInt(((TextView)view.findViewById(R.id.customScrollTodoId)).getText().toString());

            new LocalSelectTodo().execute(id);

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


    private void onLocalTodoGetterCompletion () {

        // does the initial sync if need by running the remote pusher
        if(initSync) {
            // we do not sync if there are no local todos
            if(todos.size() != 0) {
                new RemoteInitTodoPusher().execute(todos.toArray(new Todo[todos.size()]));
            } else {
                // if there was no initial sync yet and there are no local todos we have to do a pull
                new RemoteTodoPuller().execute();
            }
            initSync = false;
        }
    }

    private Context giveContext() {
        return this;
    }

    private void startDetailView(Todo todo) {
        // There might be a better way to do this
        Intent intent = new Intent(this, ModifyTodoActivity.class);
        intent.putExtra("todo", todo.toJSON().toString());
        startActivityForResult(intent, MODIFY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: called !!");
        if(requestCode == MODIFY) { // returning from detail view
            if(resultCode == ModifyTodoActivity.RESULT_OK_UPDATE) {
                if(data != null) {
                    try {
                        String str = data.getStringExtra("todo");
                        if(str == null) {
                            Log.e(TAG, "onActivityResult: String with name: todo could not be retrieved from received intent");
                            return;
                        }
                        Todo t = new Todo(new JSONObject(str));
                        new RemoteTodoUpdater().execute(t);
                        Log.i(TAG, "onActivityResult: remote update started");
                    } catch (JSONException jse) {
                        Log.e(TAG, "onActivityResult: received todo could not be converted from json", jse);
                    }
                }
            } else if (resultCode == ModifyTodoActivity.RESULT_OK_DELETE) {
                if(data != null) {
                    try {
                        String str = data.getStringExtra("todo");
                    if(str == null) {
                        Log.e(TAG, "onActivityResult: String with name: todo could not be retrieved from received intent");
                        return;
                    }
                        Todo t = new Todo(new JSONObject(str));
                        new RemoteSingleTodoDeleter().execute(t);
                        Log.i(TAG, "onActivityResult: remote update started");
                    } catch (JSONException jse) {
                        Log.e(TAG, "onActivityResult: received todo could not be converted from json", jse);
                    }
                }
            } else if (resultCode == ModifyTodoActivity.RESULT_FAIL) {
                Log.e(TAG, "onActivityResult: Result returned to from ModifyTodo activity is broken.");
            }
        } else if (requestCode == CREATE) { // returning from new todo activity
            // do stuff
            if(data != null) {
                try {
                    String str = data.getStringExtra("todo");
                    if(str == null) {
                        Log.e(TAG, "onActivityResult: String with name: todo could not be retrieved from received intent");
                        return;
                    }
                    Todo t = new Todo(new JSONObject(str));
                    new RemoteSingleTodoPusher().execute(t);
                    Log.i(TAG, "onActivityResult: remote update started");
                } catch (JSONException jse) {
                    Log.e(TAG, "onActivityResult: received todo could not be converted from json", jse);
                }
            }
        }
    }

    /**
     * Cleares all todos on the server and pushes the local todos.
     */
    public class RemoteInitTodoPusher extends AsyncTask<Todo, Void, Boolean> {
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

    public class RemoteTodoPuller extends AsyncTask<Void, Void, List<Todo>> {

        @Override
        protected List<Todo> doInBackground(Void... voids) {
            return new WebOperator().readAllTodos();
        }

        @Override
        protected void onPostExecute(List<Todo> inTodos) {
            super.onPostExecute(inTodos);
            todos = inTodos;
            displayTodosFromTodo(inTodos);
        }
    }

    public class LocalShowAllTodos extends AsyncTask<Void, Void, List<Todo>> {

        @Override
        protected List<Todo> doInBackground(Void... voids) {
            return AppDatabase.getInstance(giveContext()).todoDao().getAll();
        }

        @Override
        protected void onPostExecute(List<Todo> inTodos) {
            super.onPostExecute(inTodos);
            todos = inTodos;
            displayTodosFromTodo(inTodos);
            onLocalTodoGetterCompletion();
        }
    }

    public class LocalSelectTodo extends AsyncTask<Integer, Void, Todo> {

        @Override
        protected Todo doInBackground(Integer... ids) {
            return AppDatabase.getInstance(giveContext()).todoDao().getById(ids[0]);
        }

        @Override
        protected void onPostExecute(Todo todo) {
            super.onPostExecute(todo);
            startDetailView(todo);
        }
    }

    public class RemoteTodoUpdater extends AsyncTask<Todo, Void, Boolean> {
        private Todo todo;

        @Override
        protected Boolean doInBackground(Todo... newTodos) {
            this.todo = newTodos[0];
            // the id is from the t..do belonging to the surrounding class, the second one is newly created.
            return new WebOperator().updateTodo(todo.getId(), todo);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Log.e(TAG, "onPostExecute: RemoteTodoUpdater returned " + aBoolean + " while trying to update todo : " + todo.toJSON().toString());
        }
    }

    public class RemoteSingleTodoDeleter extends AsyncTask<Todo, Void, Void> {
        @Override
        protected Void doInBackground(Todo... todos) {
            Boolean success = new WebOperator().deleteTodo(todos[0].getId());
            if (!success) {
                Log.e(TAG, "doInBackground: todo " + todos[0].toJSON().toString() +" could not be deleted on server");
            }
            return null;
        }
    }

    public class RemoteSingleTodoPusher extends AsyncTask<Todo, Void, Void> {
        @Override
        protected Void doInBackground(Todo... todos) {
             Boolean success = new WebOperator().createTodos(Arrays.asList(todos));
             if(!success) {
                 Log.e(TAG, "doInBackground: todo " + todos[0].toJSON().toString() + "could not be created on server.");
             }
             return null;
        }
    }
}
