package com.watro.clickityclack.watro.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.watro.clickityclack.watro.Model.Report;
import com.watro.clickityclack.watro.R;

import java.util.Calendar;
import java.util.HashMap;

public class SubmitActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private ImageButton returnButton;

    EditText editTextAddress;
    EditText editTextLatitude;
    EditText editTextLongitude;
    Spinner spinnerWaterType;
    Spinner spinnerWaterCondition;

    private Calendar calendar;

    FirebaseUser currentUser;

    private Button submitButton;

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

        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextLatitude = (EditText) findViewById(R.id.editTextLatitude);
        editTextLongitude = (EditText) findViewById(R.id.editTextLongitude);


        spinnerWaterType = (Spinner) findViewById(R.id.spinnerWaterType);
        spinnerWaterType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Report.WaterType.values()));
        spinnerWaterCondition = (Spinner) findViewById(R.id.spinnerWaterCondition);
        spinnerWaterCondition.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Report.WaterCondition.values()));

        databaseReference = FirebaseDatabase.getInstance().getReference();

        returnButton = (ImageButton) findViewById(R.id.returnButton);
        submitButton = (Button) findViewById(R.id.buttonSubmitReport);


        returnButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
    }

    private void saveReport() {
        final boolean[] submitButtonPressed = {true};
        final Report report = new Report();
        String waterType = String.valueOf(spinnerWaterType.getSelectedItem());
        String waterCondition = String.valueOf(spinnerWaterCondition.getSelectedItem());
        calendar = Calendar.getInstance();
        String reportDate = Integer.valueOf(calendar.get(Calendar.MONTH)) + 1 + "-"
                + calendar.get(Calendar.DAY_OF_MONTH)
                + "-" + calendar.get(Calendar.YEAR);

        report.setReportDate(reportDate);
        report.setReportID(String.valueOf(report.hashCode()));
        report.setReporterID(currentUser.getUid());
        report.setStreetAddress(String.valueOf(editTextAddress.getText()).trim());
        report.setWaterType(waterType);
        report.setWaterCondition(waterCondition);
        report.setLatitude(String.valueOf(editTextLatitude.getText()).trim());
        report.setLongitude(String.valueOf(editTextLongitude.getText()).trim());

        DatabaseReference reportDataBaseReference = databaseReference.child("Reports");

        reportDataBaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                HashMap<String, Report> reportHashCodeToReportHashMap = new HashMap<>();

                // iterating over every report
                for (DataSnapshot child : children) {
                    Report currReport = child.getValue(Report.class);

                    // for some reason, Firebase can't retrieve the enum properties correctly from
                    // the database. Therefore, the next few lines retrieve them.

                    // TODO: FIX THE REASON BEHIND WHY FIREBASE KEEPS DELETING THE ENUM PROPERTIES
                    HashMap<String, String> currReportPropertiesHashMap = (HashMap<String, String>) child.getValue();
                    if (currReportPropertiesHashMap.get("waterType") != null) {
                        currReport.setWaterType(currReportPropertiesHashMap.get("waterType"));
                    }
                    if (currReportPropertiesHashMap.get("waterCondition") != null) {
                        currReport.setWaterCondition(currReportPropertiesHashMap.get("waterCondition"));
                    }

                    reportHashCodeToReportHashMap.put(String.valueOf(currReport.getReportID()), currReport);
                }

                reportHashCodeToReportHashMap.put(String.valueOf(report.hashCode()), report);

                if (submitButtonPressed[0]) {
                    databaseReference.child("Reports").setValue(reportHashCodeToReportHashMap);
                    submitButtonPressed[0] = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        Toast.makeText(this, "Report Submitted", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {

        if (v == returnButton) {
            finish();
            startActivity(new Intent(this, ReportsActivity.class));
        }

        if (v == submitButton) {
            if (!editTextAddress.getText().toString().equals("") ||
                    !editTextLongitude.getText().toString().equals("") ||
                    !editTextLatitude.getText().toString().equals("")) {
                saveReport();
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
            }
        }
    }
}
