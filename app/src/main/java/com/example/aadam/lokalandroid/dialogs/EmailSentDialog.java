package com.example.aadam.lokalandroid.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.aadam.lokalandroid.R;

/**
 * Shows a message that the reset password email has been sent
 * @author Ryan Schmidt
 */
public class EmailSentDialog extends DialogFragment{
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.dialog_email_sent)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EmailSentDialog.this.getDialog().dismiss();
                    } // End of method
                }); // End of method

        // Create the AlertDialog object and return it
        return builder.create();
    } // End of method
} // End of class
