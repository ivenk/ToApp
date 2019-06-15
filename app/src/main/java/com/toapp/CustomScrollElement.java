package com.toapp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomScrollElement extends LinearLayout {
    private final String TAG = "CustomScrollElement";

    public CustomScrollElement(Context context, String name, String dueDate, boolean isFavourite) {
        super(context);
        inflate(context, R.layout.custom_scroll_element, this);
        ((TextView)findViewById(R.id.customScrollTodoName)).setText(name);
        ((TextView)findViewById(R.id.customScrollTodoDate)).setText(dueDate);

        int color = getResources().getColor(R.color.scrollElementFavouriteNo);
        if (isFavourite)
            color = getResources().getColor(R.color.scrollElementFavouriteYes);
        findViewById(R.id.customScrollTodoPriority).setBackgroundColor(color);
    }

    // not really useful
    private CustomScrollElement(Context context) {
        super(context);
        inflate(context, R.layout.custom_scroll_element, this);
    }

    // we do not want that to be called
    private CustomScrollElement(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.custom_scroll_element, this);
    }
}
