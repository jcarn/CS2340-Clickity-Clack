package com.watro.clickityclack.watro.Controller;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.watro.clickityclack.watro.Model.Report;
import com.watro.clickityclack.watro.R;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SubmitActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth firebaseAuth;

    private ImageButton returnButton;

    EditText editTextAddress;
    Spinner spinnerWaterType;
    Spinner spinnerWaterCondition;
    private ArrayAdapter<CharSequence> waterTypeAdapter;
    private ArrayAdapter<CharSequence> waterConditionAdapter;
    private Calendar calendar;

    FirebaseUser currentUser;

    private Button submitButton;

    private DatabaseReference databaseReference;
    protected GoogleApiClient mClient;
    protected Location lastLocation;
    protected double curLatitude;
    protected double curLongitude;
    protected final int LOCATION_REQUEST = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Immediately ask for permission if necessary
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST);
        } else {
            buildGoogleApiClient();
        }
        setContentView(R.layout.activity_submit);

        firebaseAuth = FirebaseAuth.getInstance();

        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            // User has not logged in
            finish();
            startActivity(new Intent(this, SubmitActivity.class));
        }
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
            
        spinnerWaterType = (Spinner) findViewById(R.id.spinnerWaterType);
        waterTypeAdapter = ArrayAdapter.createFromResource(this, R.array.waterTypes, android.R.layout.simple_spinner_dropdown_item);
        spinnerWaterType.setAdapter(waterTypeAdapter);
        waterConditionAdapter = ArrayAdapter.createFromResource(this, R.array.waterCondition, android.R.layout.simple_spinner_dropdown_item);
        spinnerWaterCondition = (Spinner) findViewById(R.id.spinnerWaterCondition);
        spinnerWaterCondition.setAdapter(waterConditionAdapter);

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
        String reportDate = calendar.get(Calendar.MONTH) + 1 + "-"
                + calendar.get(Calendar.DAY_OF_MONTH)
                + "-" + calendar.get(Calendar.YEAR);

        report.setReportDate(reportDate);
        report.setReportID(String.valueOf(report.hashCode()));
        report.setReporterID(currentUser.getUid());
        report.setStreetAddress(String.valueOf(editTextAddress.getText()).trim());
        report.setWaterType(waterType);
        report.setWaterCondition(waterCondition);
        report.setLatitude(String.valueOf(curLatitude));
        report.setLongitude(String.valueOf(curLongitude));

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
            public void onCancelled(DatabaseError databaseError) {
            }
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
            if (!editTextAddress.getText().toString().equals("")) {
                saveReport();
                startActivity(new Intent(this, ReportsActivity.class));
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mClient);
        } catch (SecurityException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (lastLocation != null) {
            curLatitude = lastLocation.getLatitude();
            curLongitude = lastLocation.getLongitude();

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addressList = geocoder.getFromLocation(curLatitude, curLongitude,1);
                Address address = addressList.get(0);
                editTextAddress.setText(address.getAddressLine(0) + " "
                        + address.getLocality() + ", " + address.getAdminArea());
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No Location Detected", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended", Toast.LENGTH_LONG).show();
        mClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection has failed with error code of: "
                + connectionResult.getErrorCode(), Toast.LENGTH_LONG).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                //The user will be asked what they will allow and now this
                //function will make sure they granted everything access
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //mClient.connect();
                    buildGoogleApiClient();
                }
            }
        }
    }
}
