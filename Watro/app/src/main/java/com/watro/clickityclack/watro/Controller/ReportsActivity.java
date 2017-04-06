package com.watro.clickityclack.watro.Controller;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
//import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.watro.clickityclack.watro.Model.BasicUser;
import com.watro.clickityclack.watro.Model.Report;
import com.watro.clickityclack.watro.Model.SourceAdapter;
import com.watro.clickityclack.watro.Model.SourceModel;
import com.watro.clickityclack.watro.Model.UserSingleton;
import com.watro.clickityclack.watro.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportsActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private ImageButton settingsButton;
    private Button submitReportButton;
    private Button purityReportButton;
    private DatabaseReference databaseReference;
    private ArrayList<SourceModel> models;
    private ArrayList<String> reporterIDs;
    private String reportDate;
    private String reportID;
    private String reporterID;
    private String location;
    private String waterType;
    private String waterCondition;
    private final UserSingleton singleton = UserSingleton.getInstance();
    private SourceModel source;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        DatabaseReference reportReference = databaseReference.child("Reports");
        models = new ArrayList<>();
        reporterIDs = new ArrayList<>();
        reportReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child: children)  {
                    Report report = child.getValue(Report.class);
                    reportDate = report.getReportDate();
                    reportID = report.getReportID();
                    location = report.getStreetAddress();
                    waterType = report.getWaterType();
                    waterCondition = report.getWaterCondition();
                    reporterID = report.getReporterID();
                    source = new SourceModel(reportDate, reportID, "", location, waterType, waterCondition);
                    reporterIDs.add(reporterID);
                    models.add(source);
                }

                //BasicUser person = (BasicUser) databaseReference.child("Users").child(reportID).getValue();
                //String name =

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DatabaseReference userReference = databaseReference.child("Users");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int index = 0;
                for (String x: reporterIDs) {
                    BasicUser user = dataSnapshot.child(x).getValue(BasicUser.class);
                    models.get(index).setReporterName(user.getFirstName() + " " + user.getLastName());
                    index++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        submitReportButton = (Button) findViewById(R.id.submitReportButton);
        purityReportButton = (Button) findViewById(R.id.purityReportButton);
        ListView sourceReportListView = (ListView) findViewById(R.id.sourceReportListView);

        SourceAdapter adapter = new SourceAdapter(models, getApplicationContext());
        sourceReportListView.setAdapter(adapter);
        settingsButton.setOnClickListener(this);
        submitReportButton.setOnClickListener(this);
        purityReportButton.setOnClickListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            purityReportButton.setVisibility(View.GONE);
        } else if (singleton.exists()) {
            if (singleton.getUserType().equals("User") || singleton.getUserType().equals("Administrator")) {
                purityReportButton.setVisibility(View.GONE);
            }
        } else {
            databaseReference = FirebaseDatabase.getInstance().getReference();
            final DatabaseReference usersDataBaseReference = databaseReference.child("Users").child(currentUser.getUid());
            usersDataBaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    BasicUser person = dataSnapshot.getValue(BasicUser.class);

                    if (person != null) {
                        singleton.setUserType(person.getUserType());
                        if (singleton.getUserType().equals("User") || singleton.getUserType().equals("Administrator")) {
                            purityReportButton.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        //HashMap<String, Report> reportHashCodeToReportHashMap = new HashMap<>();

        //TODO: Henry make this actually have real values

        DatabaseReference reportDataBaseReference = databaseReference.child("Reports");
        //        final boolean[] isHashMapReady = {false};

        reportDataBaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                HashMap<String, Report> reportHashCodeToReportHashMap = new HashMap<>();

                // iterating over every report
                for (DataSnapshot child : children) {
                    Report currReport = child.getValue(Report.class);

                    if (currReport != null) {
                        HashMap<String, String> currReportPropertiesToValuesHashMap = (HashMap<String, String>) child.getValue();
                        if (currReportPropertiesToValuesHashMap.get("waterType") != null) {
                            currReport.setWaterType(currReportPropertiesToValuesHashMap.get("waterType"));
                        }
                        if (currReportPropertiesToValuesHashMap.get("waterCondition") != null) {
                            currReport.setWaterCondition(currReportPropertiesToValuesHashMap.get("waterCondition"));
                        }

                        reportHashCodeToReportHashMap.put(String.valueOf(currReport.getReportID()), currReport);
                    }
                }

                //LatLng rep;
                Report rep;
                float avgLat = 0, avgLong = 0;
                int num = 0;
                LatLng average;
                for (String key : reportHashCodeToReportHashMap.keySet()) {
                    num++;
                    rep = reportHashCodeToReportHashMap.get(key);
                    avgLat += Double.parseDouble(rep.getLatitude());
                    avgLong += Double.parseDouble(rep.getLongitude());
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(rep.getLatitude()), Double.parseDouble(rep.getLongitude())))
                            .title(reportHashCodeToReportHashMap.get(key).getStreetAddress())
                            .snippet(reportHashCodeToReportHashMap.get(key).getWaterType() + ": " + reportHashCodeToReportHashMap.get(key).getWaterCondition()));
                }

                if (num > 0) {
                    average = new LatLng(avgLat / num, avgLong / num);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(average));
                }

                //TODO: replace the average with the user's current location

                //                if (reportHashCodeToReportHashMap.size() > 0) {
                //                   isHashMapReady[0] = true;
                //                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onClick(View v) {
        if (v == settingsButton) {
            startActivity(new Intent(this, ProfileActivity.class));
        }
        if (v == submitReportButton) {
            startActivity(new Intent(this, SubmitActivity.class));
        }
        //If they are even able to see the purity report button, they are a worker or manager
        if (v == purityReportButton) {
            if (singleton.getUserType().equals("Worker")) {
                startActivity(new Intent(this, SubmitPurityReportActivity.class));
            } else {
                startActivity(new Intent(this, PurityReportActivity.class));
            }
        }
    }
}
