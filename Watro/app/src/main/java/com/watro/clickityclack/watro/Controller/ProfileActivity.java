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
import com.watro.clickityclack.watro.Model.Worker;
import com.watro.clickityclack.watro.R;

import java.util.HashMap;

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

        DatabaseReference usersDataBaseReference = databaseReference.child("Users");

        usersDataBaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                // TODO: Stop iterating over all User HashMaps and go straight to info from user id
                for (DataSnapshot user : children) {
                    HashMap<String, String> userIdToUserInfoHashMap = (HashMap<String, String>) user.getValue();

                    if (userIdToUserInfoHashMap.get("id").equals(currentUser.getUid())) {
                        // TODO: Add checks for if any of the following User properties is null
                        String firstName = userIdToUserInfoHashMap.get("firstName");
                        String lastName = userIdToUserInfoHashMap.get("lastName");
                        String email = userIdToUserInfoHashMap.get("email");
                        String homeAddress = userIdToUserInfoHashMap.get("homeAddress");
                        String userType = userIdToUserInfoHashMap.get("userType");
                        String id = userIdToUserInfoHashMap.get("id");

                        if (userType.equals("User")) {
                            superUser = new BasicUser(firstName, lastName, email, id, homeAddress, userType);

                            editTextEmail.setText(((BasicUser) superUser).getEmail());
                            editTextFirstName.setText(((BasicUser) superUser).getFirstName());
                            editTextLastName.setText(((BasicUser) superUser).getLastName());
                            editTextHomeAddress.setText(((BasicUser) superUser).getHomeAddress());
                        } else if (userType.equals("Worker")) {
                            superUser = new Worker(firstName, lastName, email, id, homeAddress, userType);

                            editTextEmail.setText(superUser.getEmail());
                            editTextFirstName.setText(((Worker) superUser).getFirstName());
                            editTextLastName.setText(((Worker) superUser).getLastName());
                            editTextHomeAddress.setText(((Worker) superUser).getHomeAddress());
                        } else if (userType.equals("Manager")) {
                            superUser = new Manager(firstName, lastName, email, id, homeAddress, userType);

                            editTextEmail.setText(superUser.getEmail());
                            editTextFirstName.setText(((Manager) superUser).getFirstName());
                            editTextLastName.setText(((Manager) superUser).getLastName());
                            editTextHomeAddress.setText(((Manager) superUser).getHomeAddress());
                        } else if (userType.equals("Administrator")) {
                            superUser = new Administrator(email, id);

                            // TODO: Add more info for Administrator (firstName, lastName, etc.)
                            editTextEmail.setText(superUser.getEmail());
                        }
                    }
                }
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

        if (updatedUserType.equals("User")) {
            currUserWithUpdatedInfo = new BasicUser(updatedFirstName, updatedLastName, updatedEmail, firebaseUser.getUid(), updatedHomeAddress, updatedUserType);
        } else if (updatedUserType.equals("Worker")) {
            currUserWithUpdatedInfo = new Worker(updatedFirstName, updatedLastName, updatedEmail, firebaseUser.getUid(), updatedHomeAddress, updatedUserType);
        } else if (updatedUserType.equals("Manager")) {
            currUserWithUpdatedInfo = new Manager(updatedFirstName, updatedLastName, updatedEmail, firebaseUser.getUid(), updatedHomeAddress, updatedUserType);
        } else if (updatedUserType.equals("Administrator")) {
            currUserWithUpdatedInfo = new Administrator(updatedEmail, firebaseUser.getUid());
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
            saveUserInformation(currentUser);

            Toast.makeText(this, "Changes Saved", Toast.LENGTH_LONG).show();
        }
    }
}
