package com.watro.clickityclack.watro.Controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.watro.clickityclack.watro.Model.BasicUser;
import com.watro.clickityclack.watro.R;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextHomeAddress;
    private EditText editTextPassword;

    private DatabaseReference databaseReference;

    private Spinner spinnerUserType;
    private ArrayAdapter<CharSequence> userTypeAdapter;

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
            // User has already logged in. Start reports activity right away.
            finish();
            startActivity(new Intent(getApplicationContext(), ReportsActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextHomeAddress = (EditText) findViewById(R.id.editTextHomeAddress);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        spinnerUserType = (Spinner) findViewById(R.id.spinnerUserType);
        userTypeAdapter = ArrayAdapter.createFromResource(this, R.array.userTypes, R.layout.support_simple_spinner_dropdown_item);
        userTypeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerUserType.setAdapter(userTypeAdapter);

        textViewSignIn = (TextView) findViewById(R.id.textViewSignIn);

        buttonRegister.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);
    }

    private void saveUserInformation(FirebaseUser firebaseUser) {
        String email = String.valueOf(editTextEmail.getText()).trim();
        String firstName = valueOf(editTextFirstName.getText()).trim();
        String lastName = valueOf(editTextLastName.getText()).trim();
        String homeAddress = valueOf(editTextHomeAddress.getText()).trim();

        String userType = String.valueOf(spinnerUserType.getSelectedItem());

        BasicUser basicUser = new BasicUser(firstName, lastName, email, String.valueOf(firebaseUser.getUid()), homeAddress, userType);

        // Use the unique ID of the logged-in user to save user's information into Firebase database
        databaseReference.child(firebaseUser.getUid()).setValue(basicUser);
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String userType = valueOf(spinnerUserType.getSelectedItem());

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your e-mail", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, "Please enter your first name", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Please enter your last name", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(userType)) {
            Toast.makeText(this, "Please select the user type", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_LONG).show();
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
                            // User is successfully registered and logged in.
                            // Now, we need to save the user's information
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            saveUserInformation(firebaseUser);

                            // Display a toast notifying the user that registration was successful
                            Toast.makeText(MainActivity.this, "Registration Successful!", Toast.LENGTH_LONG).show();

                            // Registration successful. Start Reports Activity.
                            finish();
                            startActivity(new Intent(getApplicationContext(), ReportsActivity.class));
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
