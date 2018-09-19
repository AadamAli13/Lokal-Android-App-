package com.example.aadam.lokalandroid.activities;

import android.app.ProgressDialog;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aadam.lokalandroid.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/**
 * Registers a user using Firebase's authentication system
 * @author Samer Albi Ryley Reid Ryan Schmidt and Aadam Ali
 */
@SuppressWarnings("FieldCanBeLocal")
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    // Declare constants
    private final Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$");

    // Declare variables
    private EditText displayNameEntry;
    private EditText emailEntry;
    private EditText passwordEntry;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Instantiate EditText objects
        displayNameEntry = findViewById(R.id.displayNameEntry);
        emailEntry = findViewById(R.id.emailEntry);
        passwordEntry = findViewById(R.id.passwordEntry);

        // Instantiate Button object
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);
    } // End of method

    @Override
    public void onClick(View view) {
        // Get data stored in text entries
        String displayName = displayNameEntry.getText().toString().trim();
        String email = emailEntry.getText().toString().trim();
        String password = passwordEntry.getText().toString().trim();

        // Check if text was entered, else validate text
        if (displayName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            // Check which entries are empty and display error if needed
            if (displayName.isEmpty())
                displayNameEntry.setError("Please enter a display name!");
            if (email.isEmpty())
                emailEntry.setError("Please enter an e-mail!");
            if (password.isEmpty())
                passwordEntry.setError("Please enter a password!");
        }
        else {
            // Check entries
            boolean checkOne = validateName(displayName);
            boolean checkTwo = validateEmail(email);
            boolean checkThree = validatePassword(password);

            // Check if the entries are all valid
            if (checkOne && checkTwo && checkThree) {
                // Show the progress dialog
                ProgressDialog dialog = ProgressDialog.show(RegisterActivity.this, "Registering User...", "");
                Message msg = Message.obtain();

                // Attempt to register the user
                if (registerUser(displayName, email, password)) {
                    // Dismiss the dialog
                    msg.obj = "Registration successful!";
                    dialog.setDismissMessage(msg);
                    dialog.dismiss();

                    // Close the activity
                    finish();
                }
                else {
                    // Dismiss the dialog
                    dialog.dismiss();
                } // End of if statement
            }
            else {
                // Display error message based on which errors exist
                if (!checkOne)
                    displayNameEntry.setError("The display name must be bigger than 6 characters!");
                if (!checkTwo)
                    emailEntry.setError("The e-mail is not a correct format!");
                if (!checkThree)
                    passwordEntry.setError("The password needs to be 8-20 characters long and needs one " +
                            "uppercase letter and one lowercase letter!");
            } // End of if statement
        } // End of if statement
    } // End of method

    /**
     * Adds user to the database
     * @param displayName is a String
     * @param email is a String
     * @param password is a String
     */
    private boolean registerUser(final String displayName, final String email, final String password) {
        // Create atomic boolean
        final AtomicBoolean atomicBoolean = new AtomicBoolean();

        // Attempt to create account
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // Create update request
                UserProfileChangeRequest.Builder profileUpdates = new UserProfileChangeRequest.Builder();
                profileUpdates.setDisplayName(displayName);

                // Send request and return true
                authResult.getUser().updateProfile(profileUpdates.build());

                // Change boolean to true
                atomicBoolean.set(true);
            } // End of method
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Could not be registered: " + e.getMessage(), Toast.LENGTH_LONG).show();
            } // End of method
        }); // End of method

        // Return boolean
        return atomicBoolean.get();
    } // End of method

    /**
     * Validates a display name entry
     * @param name is a String
     * @return boolean
     */
    private boolean validateName(String name) {
        // Check if the display name is bigger than 6 characters
        return (name.length() > 6);
    } // End of method

    /**
     * Validates an e-mail entry
     * @param email is a String
     * @return boolean
     */
    private boolean validateEmail(String email) {
        return emailPattern.matcher(email).matches();
    } // End of method

    /**
     * Validates a password entry
     * @param password is a String
     * @return boolean
     */
    private boolean validatePassword(String password) {
        return (!password.equals(password.toLowerCase()) && !password.equals(password.toUpperCase())
                && password.length() >= 8 && password.length() <= 20);
    } // End of method
} // End of class
