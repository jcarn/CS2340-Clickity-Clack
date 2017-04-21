package com.watro.clickityclack.watro.Controller;

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
import com.watro.clickityclack.watro.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignUp;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        // Check if user has logged in already
        if (firebaseAuth.getCurrentUser() != null) {
            // User has already logged in. Start reports activity right away.
            finish();
            startActivity(new Intent(getApplicationContext(), ReportsActivity.class));
        }

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);

        progressDialog = new ProgressDialog(this);

        buttonSignIn.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
    }

    private void userLogin() {
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
        progressDialog.setMessage("Logging In...");

        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Remove progress dialog from screen
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            // Login successful. Start reports activity.

                            finish();
                            startActivity(new Intent(getApplicationContext(), ReportsActivity.class));
                        } else {
                            // Login was unsuccessful

                            // Display a toast notifying the user that registration was not successful
                            if (task.getException() != null) {
                                Toast.makeText(LoginActivity.this, "Registration Failed.\n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Registration Failed.\n", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSignIn) {
            userLogin();
        }

        if (v == textViewSignUp) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
