package com.toapp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomScrollElement extends LinearLayout {
    private final String TAG = "CustomScrollElement";

    // not really useful
    protected CustomScrollElement(Context context) {
        super(context);
        inflate(context, R.layout.custom_scroll_element, this);
    }

    public CustomScrollElement(Context context, String name, String dueDate, EPriority priority) {
        super(context);
        inflate(context, R.layout.custom_scroll_element, this);
        ((TextView)findViewById(R.id.customScrollTodoName)).setText(name);
        ((TextView)findViewById(R.id.customScrollTodoName)).setText(dueDate);
        findViewById(R.id.customScrollTodoPriority).setBackgroundColor(matchPriorityColor(priority));
    }

    // we do not want that to be called
    protected CustomScrollElement(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.custom_scroll_element, this);
    }

    private int matchPriorityColor(EPriority priority) {
        switch (priority) {
            case LOW:
                return R.color.scrollElementPriority1;
            case MEDIUM:
                return R.color.scrollElementPriority2;
            case HIGH:
                return R.color.scrollElementPriority3;
            default:
                //this should never happen !
                Log.e(TAG, "matchPriorityColor: Priority out of bounds. Could not be matched !");
                return R.color.scrollElementPriority1; // TODO: maybe choose different color for this in order to make debugging easier.
            }
    }
}
