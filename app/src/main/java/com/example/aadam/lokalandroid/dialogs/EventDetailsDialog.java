package com.example.aadam.lokalandroid.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aadam.lokalandroid.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * An event dialog showing the user event details
 * @author Samer Alabi
 */
@SuppressWarnings({"FieldCanBeLocal", "ConstantConditions", "unused"})
public class EventDetailsDialog extends DialogFragment {
    // Declare attributes
    private DatabaseReference mainRef;
    private TextView eventName;
    private TextView address;
    private TextView type;
    private TextView date;
    private TextView owner;
    private TextView description;
    private Button registerButton;

    /**
     * Creates a new instance of this fragment
     * @param uid is the uid of a Firebase user
     * @return EventDetailsDialog
     */
    public static EventDetailsDialog newInstance(String uid) {
        // Create bundle of arguments and add args
        Bundle args = new Bundle();
        args.putString("uid", uid);

        // Create new fragment
        EventDetailsDialog fragment = new EventDetailsDialog();
        fragment.setArguments(args);

        // Return fragment
        return fragment;
    } // End of method

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.dialog_event_details);
        return builder.create();
    } // End of method

    @Override
    public void onStart() {
        super.onStart();

        // Get alert dialog
        AlertDialog d = (AlertDialog) getDialog();

        // Check if alert dialog exists
        if (d != null) {
            // Create database reference
            mainRef = FirebaseDatabase.getInstance().getReference();

            // Initialize text views
            eventName = getDialog().findViewById(R.id.evName);
            address = getDialog().findViewById(R.id.address);
            type = getDialog().findViewById(R.id.type);
            date = getDialog().findViewById(R.id.date);
            owner = getDialog().findViewById(R.id.owner);
            description = getDialog().findViewById(R.id.description);

            // Get data from database
            mainRef.child("detailed_event").orderByKey().equalTo(getArguments().getString("uid")).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    // Declare and initialize variables
                    int numAttendees = 0;

                    // Spilt latitude and longitude
                    String[] latLng = dataSnapshot.child("lat~long").getValue().toString().split("~");

                    try {
                        // Get the address of the latitude and longitude
                        Geocoder geocoder = new Geocoder(getContext());
                        List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]), 1);
                        String streetAddress = addresses.get(0).getAddressLine(0);

                        // Set the text
                        address.setText(getResources().getString(R.string.full_address, streetAddress));
                    } catch (IOException err) {
                        address.setText(R.string.unattainable);
                    } // End of catch statement

                    // Set texts for text views
                    eventName.setText(dataSnapshot.child("eventName").getValue().toString());
                    type.setText(dataSnapshot.child("type").getValue().toString());
                    date.setText(dataSnapshot.child("date").getValue().toString());
                    owner.setText(dataSnapshot.child("owner").getValue().toString());
                    description.setText(dataSnapshot.child("description").getValue().toString());
                } // End of method

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    // Set texts for text views
                    eventName.setText(dataSnapshot.child("eventName").toString());
                    address.setText(dataSnapshot.child("address").toString());
                    type.setText(dataSnapshot.child("type").toString());
                    date.setText(dataSnapshot.child("date").toString());
                    description.setText(dataSnapshot.child("description").toString());
                } // End of method

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                } // End of method

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                } // End of method

                @Override
                public void onCancelled(DatabaseError databaseError) {
                } // End of method
            }); // End of method
        } // End of if statement
    } // End of method
} // End of class
