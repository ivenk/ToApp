package com.toapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.toapp.data.Contact;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactScroller.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactScroller#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactScroller extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CONTACTS_PARAM = "contacts";
    public static final String TAG = "ContactScroller";

    private OnFragmentInteractionListener mListener;

    private Button addButton;
    private ScrollView scrollView;
    private LinearLayout linearLayout;

    private List<Contact> contacts;

    public ContactScroller() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ContactScroller.
     */
    public static ContactScroller newInstance(@Nullable List<Contact> contacts) {
        ContactScroller fragment = new ContactScroller();
        Bundle args = new Bundle();
        if(contacts != null) {
            String[] strings = new String[contacts.size()];
            for (int i = 0; i< contacts.size(); i++) {
                strings[i] = contacts.get(i).toJSON().toString();
            }
            args.putStringArray(CONTACTS_PARAM, strings);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String[] contactsJson = getArguments().getStringArray(CONTACTS_PARAM);
            if (contactsJson != null) {
                List<Contact> building = new ArrayList<>();
                try {
                    for(String str : contactsJson) {
                        building.add(new Contact(new JSONObject(str)));
                    }
                }catch (JSONException jse) {
                    Log.e(TAG, "onCreate: JSONError occurred while trying to build contacts passed to ContactScroller ", jse);
                }
                contacts = building;
            }
        }
        contacts = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_picker, container, false);
        addButton = view.findViewById(R.id.add_contact);
        addButton.setOnClickListener(this);

        scrollView =view.findViewById(R.id.contact_scroll_view);
        linearLayout = scrollView.findViewById(R.id.contact_scrollable_linear);
        showContacts();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if(view == addButton) {
            mListener.startContactPicker();
        }
    }

    private void showContacts() {
        Log.i(TAG, "showContacts: called !!!");
        linearLayout.removeAllViews();
        for(Contact c : contacts) {
            linearLayout.addView(new CustomContactScrollElement((Context) mListener, c.getId(), c.getName()));
        }
    }

    public void onContactDelete(int id) {
        Log.i(TAG, "onContactDelete: Trying to delete contact with id" + id);
        for (Contact c: contacts) {
            if(c.getId() == id) {
                contacts.remove(c);
            }
        }
        showContacts();
    }

    public String getContactsString() {
        String result = "";

        for (int i = 0; i < contacts.size(); i++){
            Contact c = contacts.get(i);
            result += c.getId();
            if(i != (contacts.size()-1)) { // always add , except for the last value
                result += ",";
            }
        }
        return result;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void startContactPicker();
    }

    public void attachNewContact(Contact contact) {
        this.contacts.add(contact);
        showContacts();
    }
}
