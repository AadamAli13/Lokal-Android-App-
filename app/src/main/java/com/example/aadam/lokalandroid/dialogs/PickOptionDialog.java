package com.example.aadam.lokalandroid.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Picks an option in a dialog fragment
 * @author Ryan Schmidt
 */
public class PickOptionDialog extends DialogFragment{
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose one:")
                .setItems(new String[]{"Create Event", "Set Location"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 1){

                        }
                        else if(which == 0){
                            //change password
                        }
                    }
                });

        // Return builder
        return builder.create();
    } // End of method
} // End of class
