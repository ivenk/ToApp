package com.toapp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomScrollElement extends LinearLayout {
    private final String TAG = "CustomScrollElement";

    // not really usefull
    protected CustomScrollElement(Context context) {
        super(context);
        inflate(context, R.layout.custom_scroll_element, this);
    }

    public CustomScrollElement(Context context, String name, String dueDate, int priority) {
        super(context);
        inflate(context, R.layout.custom_scroll_element, this);
        ((TextView)findViewById(R.id.customScrollTodoName)).setText(name);
        ((TextView)findViewById(R.id.customScrollTodoName)).setText(dueDate);


    }

    // we do not want that to be called
    protected CustomScrollElement(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.custom_scroll_element, this);
    }


}
