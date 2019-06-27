package com.toapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "ContactScroller";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactScroller.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactScroller newInstance(String param1, String param2) {
        ContactScroller fragment = new ContactScroller();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        contacts = new ArrayList<>();
        //TODO add passed contacts
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
