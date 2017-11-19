package com.example.aadam.lokalandroid.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Ryan_Schmidt on 2017-11-18.
 */

public class SettingsPanelDialog extends DialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Settings")
                .setItems(new String[]{"Change Password", "Log Out"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    if(which == 1){
                        //logout
                    }
                    else if(which == 0){
                        //change password
                    }
                    }
                });
        return builder.create();
    }
}
