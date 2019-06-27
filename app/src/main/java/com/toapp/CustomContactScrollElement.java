package com.toapp;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomContactScrollElement extends LinearLayout {

    public CustomContactScrollElement(Context context) {
        super(context);
        View view = inflate(context, R.layout.custom_contact_scroll_element, this);

        TextView text = view.findViewById(R.id.contact_name_detail);
        Button removeButton = view.findViewById(R.id.remove_contact);

    }


}
