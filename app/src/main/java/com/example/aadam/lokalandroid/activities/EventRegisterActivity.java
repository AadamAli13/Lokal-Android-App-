package com.example.aadam.lokalandroid.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.aadam.lokalandroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Registers event to the database
 * @author Ryan Schmidt & Samer Alabi
 */
public class EventRegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    // Declare attributes
    private EditText eventName;
    private EditText description;
    private Spinner spinner;
    private Button dateButton;
    private Button timeButton;
    private Button addEvent;
    private DatePickerDialog dateDialog;
    private TimePickerDialog timeDialog;
    private String pickedDate;
    private String pickedTime;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_register);

        // Declare spinner
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        // Create adapter and add it to the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.types,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Instantiate views
        eventName = findViewById(R.id.eventName);
        description = findViewById(R.id.description);

        // Instantiate buttons and their listener
        dateButton = findViewById(R.id.pickDate);
        dateButton.setOnClickListener(this);
        timeButton = findViewById(R.id.pickTime);
        timeButton.setOnClickListener(this);
        addEvent = findViewById(R.id.addEvent);
        addEvent.setOnClickListener(this);
    } // End of method

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    } // End of method

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    } // End of method

    @Override
    public void onClick(View view) {
        if (view.getId() == dateButton.getId()) {
            // Get the current date
            String[] date = (new SimpleDateFormat("yyyy-MM-dd", Locale.US)).format(Calendar.getInstance().getTime()).split("-");

            // Create the date picker dialog
            dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    pickedDate = year + "-" + month + "-" + day;
                } // End of method
            }, Integer.getInteger(date[0]), Integer.getInteger(date[1]), Integer.getInteger(date[2]));
        }
        else if (view.getId() == timeButton.getId()) {
            // Get the current time
            String[] time = (new SimpleDateFormat("HH:mm:ss", Locale.US)).format(Calendar.getInstance().getTime()).split("-");

            // Create the date picker dialog
            timeDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    pickedTime = hour + ":" + minute + ":00";
                } // End of method
            }, Integer.getInteger(time[0]), Integer.getInteger(time[1]), false);
        }
        else {
            // Declare and instantiate values
            String eventNameText = eventName.getText().toString().trim();
            String descriptionText = description.getText().toString().trim();
            String type = spinner.getSelectedItem().toString();

            // Check if input was entered and is valid
            if (eventNameText.isEmpty() || descriptionText.isEmpty()) {
                if (eventNameText.isEmpty())
                    eventName.setError("Please enter the name of your event!");
                if (descriptionText.isEmpty())
                    description.setError("Please enter the description of your event!");
            }
            else if (spinner.getSelectedItem() == null)
                Toast.makeText(getApplicationContext(), "Please select an event type!", Toast.LENGTH_LONG).show();
            else if (pickedDate == null || pickedTime == null) {
                Toast.makeText(getApplicationContext(), "Please select a date AND a time!", Toast.LENGTH_LONG).show();
            }
            else {
                try {
                    // Get epoch time
                    long epoch = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)).parse(pickedDate + " " + pickedTime).getTime();

                    // Check if time is valid
                    if (epoch <= (new Date()).getTime()) {
                        Toast.makeText(getApplicationContext(), "Please enter a date and time that are past the current!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        // Get database reference
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                        // Create map with data
                        HashMap<String, String> map = new HashMap<>();
                        map.put("eventName", eventNameText);
                        map.put("lat~long", getIntent().getStringExtra("lat~long"));
                        map.put("type", type);
                        map.put("description", descriptionText);
                        map.put("date", Long.toString(epoch));
                        map.put("owner", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

                        // Add data to database
                        ref.child("detailed_event").push().setValue(map);
                    } // End of if statement
                } catch (ParseException e) {
                    e.printStackTrace();
                } // End of try statement
            } // End of if statement
        }  // End of if statement
    } // End of method
} // End of class