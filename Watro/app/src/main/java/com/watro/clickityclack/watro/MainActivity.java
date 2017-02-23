package com.watro.clickityclack.watro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignIn;

    private ProgressDialog progressDialog;

    // The Firebase authentication object that will be used to register the user on the server
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        // Check if user has logged in already
        if (firebaseAuth.getCurrentUser() != null) {
            // User has already logged in. Start profile activity right away.
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        textViewSignIn = (TextView) findViewById(R.id.textViewSignIn);

        buttonRegister.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            // E-mail is empty
            Toast.makeText(this, "Please enter your e-mail", Toast.LENGTH_LONG).show();

            // Stop the function from executing further
            return;
        }


        if (TextUtils.isEmpty(password)) {
            // Password is empty
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_LONG).show();

            // Stop the function from executing further
            return;
        }

        // We now need to validate the e-mail and password with the server. Because this is an
        // internet operation that will take time, a progress dialog is used

        // Setting the message of the progress dialog
        progressDialog.setMessage("Registering User...");

        progressDialog.show();

        // Create a user on the Firebase console with the given e-mail and password.
        // A listener is also attached to track execution of method. In this case, we want to know
        // when registration communication with server is done.
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    // The Task object contains info on whether registration was successful or not
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Remove progress dialog from screen
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            // User is successfully registered and logged in

                            // Display a toast notifying the user that registration was successful
                            Toast.makeText(MainActivity.this, "Registration Successful!", Toast.LENGTH_LONG).show();

                            // Registration successful. Start profile activity.\
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        } else {
                            // Registration was unsuccessful

                            // Display a toast notifying the user that registration was not successful
                            Toast.makeText(MainActivity.this, "Registration Failed.\n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == buttonRegister) {
            registerUser();
        }

        if (v == textViewSignIn) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
