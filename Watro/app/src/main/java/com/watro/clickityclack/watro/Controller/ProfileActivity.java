package com.watro.clickityclack.watro.Controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.watro.clickityclack.watro.Model.Administrator;
import com.watro.clickityclack.watro.Model.BasicUser;
import com.watro.clickityclack.watro.Model.Manager;
import com.watro.clickityclack.watro.Model.SuperUser;
import com.watro.clickityclack.watro.Model.UserSingleton;
import com.watro.clickityclack.watro.Model.Worker;
import com.watro.clickityclack.watro.R;

//import java.util.HashMap;

import static android.widget.Toast.makeText;
import static java.lang.String.valueOf;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private ImageButton returnButton;

    private EditText editTextEmail;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextHomeAddress;
    private Spinner spinnerUserType;
    private Button buttonSaveChanges;

    private ArrayAdapter<CharSequence> userTypeAdapter;

    private FirebaseUser currentUser;

    private Button buttonLogOut;

    private DatabaseReference databaseReference;
    private final UserSingleton singleton = UserSingleton.getInstance();

    // Profile pic integration part 1 starts here...

    private StorageReference storageReference;
    private Button profilePicUpdateButton;
    private static final int GALLERY_INTENT = 2;

    private ImageView profilePicImageView;

    private ProgressDialog progressDialog;
    // ...profile pic integration part 1 ends here

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
        // This is getting the specific reference to the current User using their Uid
        DatabaseReference currentUserReference = databaseReference.child("Users").child(currentUser.getUid());

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading User Information...");
        progressDialog.show();

        currentUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get all of the information for the current user
                BasicUser user = dataSnapshot.getValue(BasicUser.class);

                progressDialog.dismiss();

                if (user != null) {
                    editTextEmail.setText(user.getEmail());
                    editTextFirstName.setText(user.getFirstName());
                    editTextLastName.setText(user.getLastName());
                    editTextHomeAddress.setText(user.getHomeAddress());
                    singleton.setUserType(user.getUserType());
                    spinnerUserType.setSelection(userTypeAdapter.getPosition(user.getUserType()));
                } else {
                    Toast.makeText(ProfileActivity.this, "User invalid. Please log out and sign in again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Profile pic integration part 2 starts here...

        storageReference = FirebaseStorage.getInstance().getReference();

        profilePicImageView = (ImageView) findViewById(R.id.profilePicImageView);

        StorageReference currProfPicStorageReference = storageReference.child("Photos").child(currentUser.getUid());

        Glide.with(ProfileActivity.this).using(new FirebaseImageLoader()).load(currProfPicStorageReference).into(profilePicImageView);

        profilePicUpdateButton = (Button) findViewById(R.id.profilePicUpdateButton);

        profilePicUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);

                intent.setType("image/*");

                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        // ...profile pic part 2 integration ends here

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

        switch (updatedUserType) {
            case "User":
                currUserWithUpdatedInfo = new BasicUser(updatedFirstName, updatedLastName, updatedEmail, firebaseUser.getUid(), updatedHomeAddress, updatedUserType);
                break;
            case "Worker":
                currUserWithUpdatedInfo = new Worker(updatedFirstName, updatedLastName, updatedEmail, firebaseUser.getUid(), updatedHomeAddress, updatedUserType);
                break;
            case "Manager":
                currUserWithUpdatedInfo = new Manager(updatedFirstName, updatedLastName, updatedEmail, firebaseUser.getUid(), updatedHomeAddress, updatedUserType);
                break;
            case "Administrator":
                currUserWithUpdatedInfo = new Administrator(updatedFirstName, updatedLastName, updatedEmail, firebaseUser.getUid(), updatedHomeAddress, updatedUserType);
                break;
        }

        // Use the unique ID of the logged-in user to save user's information into Firebase database
        databaseReference.child("Users").child(firebaseUser.getUid()).setValue(currUserWithUpdatedInfo);
    }

    // Profile pic integration part 3 starts here...

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            progressDialog.setMessage("Updating Profile Picture");
            progressDialog.show();

            profilePicImageView.setImageResource(android.R.color.transparent);

            Uri uri = data.getData();

            StorageReference photoStorageReference = storageReference.child("Photos").child(currentUser.getUid());

            photoStorageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();

                    Uri imageDownloadUri = taskSnapshot.getDownloadUrl();

                    StorageReference currProfPicStorageReference = storageReference.child("Photos").child(currentUser.getUid());

                    Glide.with(ProfileActivity.this).using(new FirebaseImageLoader()).load(currProfPicStorageReference).into(profilePicImageView);

//                    Picasso.with(ProfileActivity.this).load(imageDownloadUri).fit().centerCrop().into(profilePicImageView);

//                    DatabaseReference imageDownloadUriReference = databaseReference.child("Users").child(currentUser.getUid()).child("imageDownloadUri");

//                    databaseReference.child("Users").child(currentUser.getUid()).child("imageDownloadUri").setValue("hi");

                    Toast.makeText(ProfileActivity.this, "Updated Profile Pic!", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, "Profile Pic Update Failed.\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    // ...profile pic integration part 3 ends here...

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
                startActivity(new Intent(this, ReportsActivity.class));
            }
        }

    }
}
