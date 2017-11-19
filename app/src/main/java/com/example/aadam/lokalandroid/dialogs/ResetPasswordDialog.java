package com.example.aadam.lokalandroid.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aadam.lokalandroid.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

/**
 * Allows a user to reset their password
 * @author Ryan Schmidt
 */
public class ResetPasswordDialog extends DialogFragment {
    // Declare constants
    private final Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$");

    // Declare attributes
    private EditText emailEntry;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // Create dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.dialog_password_reset)
            .setPositiveButton("Reset Password", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                } // End of method
            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                ResetPasswordDialog.this.getDialog().dismiss();
            }
        }); // End of method

        // Create the AlertDialog object and return it
        return builder.create();
    } // End of method

    @Override
    public void onStart() {
        super.onStart();

        // Get alert dialog
        AlertDialog d = (AlertDialog) getDialog();

        // Check if alert dialog exists
        if (d != null) {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get the email entry view
                    emailEntry = getDialog().findViewById(R.id.emailEntry);

                    // Get text from email
                    String email = emailEntry.getText().toString().trim();

                    // Check if input was entered and is valid
                    if (email.isEmpty()) {
                        emailEntry.setError("Please enter an E-Mail!");
                    }
                    else if (!emailPattern.matcher(email).matches()) {
                        emailEntry.setError("The entered e-mail is not of correct format!");
                    }
                    else {
                        findEmail(email, FirebaseAuth.getInstance());
                    } // End of if statement
                } // End of method
            }); // End of method
        } // End of if statements
    } // End of method

    /**
     * Attempts to find and reset the e-mail of a Firebase user
     * @param email is the email of a Firebase user
     * @param auth is the authentication of the Firebase user
     */
    public void findEmail(String email, FirebaseAuth auth){
        // Declare variables
        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Open new notify dialog and close current one
                DialogFragment notify = new EmailSentDialog();
                notify.show(getFragmentManager(),"Password Reset");
                ResetPasswordDialog.this.getDialog().dismiss();
            } // End of method
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Show error message
                Toast.makeText(getActivity(), "Could not reset password: " + e.getMessage(), Toast.LENGTH_LONG).show();
            } // End of method
        }); // End of methods
    } // End of method
} // End of class
