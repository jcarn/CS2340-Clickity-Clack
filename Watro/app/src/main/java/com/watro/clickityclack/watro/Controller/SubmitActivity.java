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
import com.watro.clickityclack.watro.Model.Report;
import com.watro.clickityclack.watro.R;

import static java.lang.String.valueOf;

public class SubmitActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private ImageButton returnButton;

    EditText editTextAddress;
    Spinner spinnerWaterType;
    Spinner spinnerWaterCondition;
    Button buttonSubmitReport;

    FirebaseUser currentUser;

    private Button submitButton;

    private Report newReport;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        firebaseAuth = FirebaseAuth.getInstance();

        currentUser  = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            // User has not logged in
            finish();
            startActivity(new Intent(this, SubmitActivity.class));
        }


        spinnerWaterType = (Spinner) findViewById(R.id.spinnerWaterType);
        spinnerWaterType.setAdapter(new ArrayAdapter<Report.WaterType>(this, android.R.layout.simple_spinner_item, Report.WaterType.values()));
        spinnerWaterCondition = (Spinner) findViewById(R.id.spinnerWaterCondition);
        spinnerWaterCondition.setAdapter(new ArrayAdapter<Report.WaterCondition>(this, android.R.layout.simple_spinner_item, Report.WaterCondition.values()));

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                // TODO: Stop iterating over all User HashMaps and go straight to info from user id
                for (DataSnapshot child : children) {
                    //all you Henry, do crazy firebase wizard magic
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        returnButton = (ImageButton) findViewById(R.id.returnButton);
        submitButton = (Button) findViewById(R.id.buttonSubmitReport);


        returnButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
    }

    private void saveReport() { //idk what type a report is from firebase's persective
        //save report, @HenrySaba you got this
    }

    @Override
    public void onClick(View v) {

        if (v == returnButton) {
            finish();
            startActivity(new Intent(this, ReportsActivity.class));
        }

        if (v == submitButton) {
            saveReport(); //once again, not sure what to send it here

            Toast.makeText(this, "Report Submitted", Toast.LENGTH_LONG).show();
        }
    }
}
