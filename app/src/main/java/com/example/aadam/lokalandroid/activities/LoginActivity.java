package com.example.aadam.lokalandroid.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aadam.lokalandroid.R;
import com.example.aadam.lokalandroid.dialogs.ResetPasswordDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

/**
 * Log-ins a user into the main activity
 * @author Aadam Ali
 */
@SuppressWarnings("FieldCanBeLocal")
public class LoginActivity extends AppCompatActivity implements View.OnClickListener  {
    // Declare constants
    private final Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$");

    // Declare attributes
    private Button login;
    private TextView buttonReg;
    private TextView forgetPass;
    private EditText textEmail;
    private EditText textPass;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check if the user is already logged in
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startMapActivity();
        } // End of if statement

        //Initializes all variables declared above
        forgetPass = findViewById(R.id.buttonFpass);
        login = findViewById(R.id.buttonLogin);
        buttonReg = findViewById(R.id.buttonReg);
        textEmail = findViewById(R.id.editTextEmail);
        textPass = findViewById(R.id.editTextPassword);
        auth = FirebaseAuth.getInstance();

        // Set listeners
        login.setOnClickListener(this);
        buttonReg.setOnClickListener(this);
        forgetPass.setOnClickListener(this);
    } // End of method

    /**
     * Signs a user into the firebase system
     */
    private void userLogin(){
        // Get the email and password
        String email = textEmail.getText().toString().trim();
        String password = textPass.getText().toString().trim();

        // Check if input is entered and is valid
        if (email.isEmpty()){
            textEmail.setError("Please enter an e-mail!");
        }
        else if (password.isEmpty()){
            textPass.setError("Please enter a password!");
        }
        else if (!emailPattern.matcher(email).matches()){
            textEmail.setError("The entered e-mail is not of correct format!");
        }
        else if (!validatePassword(password)) {
            textPass.setError("The password needs to be 8-20 characters long and needs one " +
                    "uppercase letter and one lowercase letter!");
        }
        else {
            // Attempt to sign a user into the database
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    startMapActivity();
                } // End of method
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "Could not be signed in: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } // End of method
            }); // End of methods
        } // End of if statement
    } // End of method

    /**
     * Shows a dialog to reset password
     */
    private void forgot() {
        ResetPasswordDialog frag = new ResetPasswordDialog();
        frag.show(getFragmentManager(),"something");
    } // End of method

    /**
     * Starts the main activity of the application
     */
    private void startMapActivity() {
        startActivity(new Intent(LoginActivity.this, MapActivity.class));
    } // End of method

    /**
     * Launches the register activities
     */
    private void register() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    } // End of method

    @Override
    public void onClick(View click) {
        // Check which button was clicked
        if (click.getId() == login.getId()){
            userLogin();
        }
        else if (click.getId() == forgetPass.getId()) {
            forgot();
        }
        else {
            register();
        } // End of if statement
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