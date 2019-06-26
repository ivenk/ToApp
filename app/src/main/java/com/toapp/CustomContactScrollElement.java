package com.toapp;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class CustomContactScrollElement extends LinearLayout {

    public CustomContactScrollElement(Context context) {
        super(context);
        View view = inflate(context, R.layout.custom_scroll_element, this);

    }


}
