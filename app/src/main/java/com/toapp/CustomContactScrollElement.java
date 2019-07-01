package com.toapp;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomContactScrollElement extends LinearLayout {

    public CustomContactScrollElement(final Context context, final int id, String name) {
        super(context);
        View view = inflate(context, R.layout.custom_contact_scroll_element, this);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((IContactScrollListener) context).onScrollableCall(id);
            }
        });

        TextView text = view.findViewById(R.id.contact_name_detail);
        text.setText(name);

        Button button = view.findViewById(R.id.remove_contact);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((IContactScrollListener) context).onScrollableDeletionCall(id);
            }
        });
    }

    public interface IContactScrollListener {
      void onScrollableDeletionCall (int id);

      void onScrollableCall(int id);
    }

}
