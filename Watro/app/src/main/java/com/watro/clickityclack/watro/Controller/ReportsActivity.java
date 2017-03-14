package com.watro.clickityclack.watro.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.watro.clickityclack.watro.Model.Report;
import com.watro.clickityclack.watro.R;

import java.util.HashMap;

public class ReportsActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback    {

    private ImageButton settingsButton;
    private Button submitReportButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        submitReportButton = (Button) findViewById(R.id.submitReportButton);

        settingsButton.setOnClickListener(this);
        submitReportButton.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        HashMap<String, Report> reportHashCodeToReportHashMap = new HashMap<String,Report>();

        //TODO: Henry make this actually have real values

        DatabaseReference reportDataBaseReference = databaseReference.child("Reports");
        final boolean[] isHashMapReady = {false};

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
                    HashMap<String, String> currReportPropertiesToValuesHashMap = (HashMap<String, String>) child.getValue();
                    if (currReportPropertiesToValuesHashMap.get("waterType") != null) {
                        currReport.setWaterType(currReportPropertiesToValuesHashMap.get("waterType"));
                    }
                    if (currReportPropertiesToValuesHashMap.get("waterCondition") != null) {
                        currReport.setWaterCondition(currReportPropertiesToValuesHashMap.get("waterCondition"));
                    }

                    reportHashCodeToReportHashMap.put(String.valueOf(currReport.getReportID()), currReport);
                }

                LatLng rep;
                int avgLat = 0, avgLong = 0, num = 0;
                for (String key : reportHashCodeToReportHashMap.keySet()) {
                    num++;
                    rep = addressToLatLng(reportHashCodeToReportHashMap.get(key).getStreetAddress());
                    avgLat += rep.latitude;
                    avgLat += rep.longitude;
                    googleMap.addMarker(new MarkerOptions().position(rep)
                            .title(reportHashCodeToReportHashMap.get(key).getStreetAddress()));
                }

                LatLng average = new LatLng(avgLat, avgLong);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(average));
                //TODO: replace the average with the user's current location

                if (reportHashCodeToReportHashMap.size() > 0) {
                    isHashMapReady[0] = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private LatLng addressToLatLng(String address) {
        //TODO: Implement, either Uche or me way later tonight}
        return new LatLng(33.8, 84.4);
    }


    @Override
    public void onClick(View v) {
        if (v == settingsButton) {
            startActivity(new Intent(this, ProfileActivity.class));
        }
        if (v == submitReportButton) {
            startActivity(new Intent(this, SubmitActivity.class));
        }
    }
}
