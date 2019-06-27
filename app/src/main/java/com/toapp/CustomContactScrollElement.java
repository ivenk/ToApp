package com.toapp;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomContactScrollElement extends LinearLayout {

    public CustomContactScrollElement(Context context, int id, String name) {
        super(context);
        View view = inflate(context, R.layout.custom_contact_scroll_element, this);

        TextView text = view.findViewById(R.id.contact_name_detail);
        ((TextView) view.findViewById(R.id.custom_contact_scroll_id)).setText(Integer.toString(id));
        text.setText(name);
    }


}
