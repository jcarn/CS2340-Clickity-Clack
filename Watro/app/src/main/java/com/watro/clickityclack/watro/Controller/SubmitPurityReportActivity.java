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
import android.support.v7.widget.LinearLayoutCompat;
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
import com.watro.clickityclack.watro.Model.PurityReport;
import com.watro.clickityclack.watro.Model.Report;
import com.watro.clickityclack.watro.R;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SubmitPurityReportActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    private DatabaseReference databaseReference;

    protected GoogleApiClient mClient;
    protected Location lastLocation;
    protected double curLatitude;
    protected double curLongitude;
    protected final int LOCATION_REQUEST = 100;

    private Calendar calendar;

    private ImageButton returnButton;
    private Button submitButton;
    private EditText editTextAddress;
    private EditText editTextVirus;
    private EditText editTextContaminant;
    private Spinner spinnerOverallCondition;
    private ArrayAdapter<CharSequence> overallConditionAdapter;


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
        setContentView(R.layout.activity_submit_purity_report);

        firebaseAuth = FirebaseAuth.getInstance();

        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            // User has not logged in
            finish();
            startActivity(new Intent(this, SubmitPurityReportActivity.class));
        }

        setContentView(R.layout.activity_submit_purity_report);
        returnButton = (ImageButton) findViewById(R.id.returnButton);
        returnButton.setOnClickListener(this);
        submitButton = (Button) findViewById(R.id.buttonSubmitPurityReport);
        submitButton.setOnClickListener(this);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextVirus = (EditText) findViewById(R.id.editTextVirus);
        editTextContaminant = (EditText) findViewById(R.id.editContaminant);
        spinnerOverallCondition = (Spinner) findViewById(R.id.spinnerOverallCondition);
        overallConditionAdapter = ArrayAdapter.createFromResource(this, R.array.overallCondition, R.layout.support_simple_spinner_dropdown_item);
        overallConditionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerOverallCondition.setAdapter(overallConditionAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void saveReport() {
        final boolean[] submitButtonPressed = {true};
        final PurityReport report = new PurityReport();
        String waterCondition = String.valueOf(spinnerOverallCondition.getSelectedItem());
        calendar = Calendar.getInstance();
        String reportDate = calendar.get(Calendar.MONTH) + 1 + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.YEAR);

        report.setReportDate(reportDate);
        report.setReportID(String.valueOf(report.hashCode()));
        report.setReporterID(currentUser.getUid());
        report.setStreetAddress(String.valueOf(editTextAddress.getText()).trim());
        report.setWaterCondition(waterCondition);
        report.setLatitude(String.valueOf(curLatitude));
        report.setLongitude(String.valueOf(curLongitude));
        report.setVirusPPM(String.valueOf(editTextVirus.getText()).trim());
        report.setContaminantPPM(String.valueOf(editTextContaminant.getText()).trim());

        DatabaseReference purityReportsDataBaseReference = databaseReference.child("PurityReports");
        purityReportsDataBaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                HashMap<String, PurityReport> reportHashCodeToReportHashMap = new HashMap<>();

                //iterating over every report
                for (DataSnapshot child : children) {
                    PurityReport currReport = child.getValue(PurityReport.class);
                    reportHashCodeToReportHashMap.put(String.valueOf(currReport.getReportID()), currReport);
                }
                reportHashCodeToReportHashMap.put(String.valueOf(report.hashCode()), report);
                if (submitButtonPressed[0]) {
                    databaseReference.child("PurityReports").setValue(reportHashCodeToReportHashMap);
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
            startActivity(new Intent(this, PurityReportActivity.class));
        }

        if (v == submitButton) {
            if (!editTextAddress.getText().toString().equals("")) {
                saveReport();
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
