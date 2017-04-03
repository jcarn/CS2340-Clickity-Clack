package com.watro.clickityclack.watro.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.watro.clickityclack.watro.Model.Administrator;
import com.watro.clickityclack.watro.Model.BasicUser;
import com.watro.clickityclack.watro.Model.Manager;
import com.watro.clickityclack.watro.Model.SuperUser;
import com.watro.clickityclack.watro.Model.UserSingleton;
import com.watro.clickityclack.watro.Model.Worker;
import com.watro.clickityclack.watro.R;

import java.util.HashMap;

import static android.widget.Toast.makeText;
import static java.lang.String.valueOf;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private ImageButton returnButton;

    EditText editTextEmail;
    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextHomeAddress;
    Spinner spinnerUserType;
    Button buttonSaveChanges;

    private ArrayAdapter<CharSequence> userTypeAdapter;

    FirebaseUser currentUser;

    private Button buttonLogOut;

    private SuperUser superUser;

    private DatabaseReference databaseReference;
    UserSingleton singleton = UserSingleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        currentUser  = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            // User has not logged in
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        buttonSaveChanges = (Button) findViewById(R.id.buttonSaveChanges);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextHomeAddress = (EditText) findViewById(R.id.editTextHomeAddress);
        spinnerUserType = (Spinner) findViewById(R.id.spinnerUserType);
        userTypeAdapter = ArrayAdapter.createFromResource(this, R.array.userTypes, R.layout.support_simple_spinner_dropdown_item);
        userTypeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerUserType.setAdapter(userTypeAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        //This is getting the specific reference to the current User using their Uid
        DatabaseReference currentUserReference = databaseReference.child("Users").child(currentUser.getUid());
        currentUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get all of the information for the current user
                BasicUser user = dataSnapshot.getValue(BasicUser.class);
                editTextEmail.setText(user.getEmail());
                singleton.setUserType(user.getUserType());
                editTextFirstName.setText(user.getFirstName());
                editTextLastName.setText(user.getLastName());
                editTextHomeAddress.setText(user.getHomeAddress());
                spinnerUserType.setSelection(userTypeAdapter.getPosition(singleton.getUserType()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        returnButton = (ImageButton) findViewById(R.id.returnButton);

        buttonLogOut = (Button) findViewById(R.id.logOutButton);

        buttonLogOut.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        buttonSaveChanges.setOnClickListener(this);
    }

    private void saveUserInformation(FirebaseUser firebaseUser) {
        SuperUser currUserWithUpdatedInfo = new BasicUser("","","","","","");

        String updatedEmail = String.valueOf(editTextEmail.getText()).trim();
        String updatedFirstName = valueOf(editTextFirstName.getText()).trim();
        String updatedLastName = valueOf(editTextLastName.getText()).trim();
        String updatedHomeAddress = valueOf(editTextHomeAddress.getText()).trim();

        String updatedUserType = String.valueOf(spinnerUserType.getSelectedItem());
        singleton.setUserType(updatedUserType);

        if (updatedUserType.equals("User")) {
            currUserWithUpdatedInfo = new BasicUser(updatedFirstName, updatedLastName, updatedEmail, firebaseUser.getUid(), updatedHomeAddress, updatedUserType);
        } else if (updatedUserType.equals("Worker")) {
            currUserWithUpdatedInfo = new Worker(updatedFirstName, updatedLastName, updatedEmail, firebaseUser.getUid(), updatedHomeAddress, updatedUserType);
        } else if (updatedUserType.equals("Manager")) {
            currUserWithUpdatedInfo = new Manager(updatedFirstName, updatedLastName, updatedEmail, firebaseUser.getUid(), updatedHomeAddress, updatedUserType);
        } else if (updatedUserType.equals("Administrator")) {
            currUserWithUpdatedInfo = new Administrator(updatedFirstName, updatedLastName, updatedEmail, firebaseUser.getUid(), updatedHomeAddress, updatedUserType);
        }

        // Use the unique ID of the logged-in user to save user's information into Firebase database
        databaseReference.child("Users").child(firebaseUser.getUid()).setValue(currUserWithUpdatedInfo);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogOut) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        if (v == returnButton) {
            finish();
            startActivity(new Intent(this, ReportsActivity.class));
        }

        if (v == buttonSaveChanges) {

            if (String.valueOf(editTextFirstName.getText()).equals("") || String.valueOf(editTextLastName.getText()).equals("") || String.valueOf(editTextEmail.getText()).equals("") || String.valueOf(editTextHomeAddress.getText()).equals("")) {
                makeText(this, "Field cannot be left blank.", Toast.LENGTH_LONG).show();
            } else {
                saveUserInformation(currentUser);
                makeText(this, "Changes Saved", Toast.LENGTH_LONG).show();
            }
        }

    }
}
