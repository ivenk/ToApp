package com.toapp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class CustomScrollElement extends LinearLayout {
    private final String TAG = "CustomScrollElement";

    private String name;
    private String dueDate;
    private String done;
    private String important;

    // not really usefull
    protected CustomScrollElement(Context context) {
        super(context);
        inflate(context, R.layout.custom_scroll_element, this);
    }

    public CustomScrollElement(Context context, String name) {
        super(context);
        inflate(context, R.layout.custom_scroll_element, this);
    }

    // we do not want that to be called
    protected CustomScrollElement(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.custom_scroll_element, this);
    }


}
