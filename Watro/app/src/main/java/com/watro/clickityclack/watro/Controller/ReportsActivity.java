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
import com.watro.clickityclack.watro.Model.Report;
import com.watro.clickityclack.watro.R;

import java.util.HashMap;

public class ReportsActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private ImageButton settingsButton;
    private Button submitReportButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        submitReportButton = (Button) findViewById(R.id.submitReportButton);

        settingsButton.setOnClickListener(this);
        submitReportButton.setOnClickListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        HashMap<String, Report> reportMap = new HashMap<String,Report>();//TODO: Henry make this actually have real values

        LatLng rep;
        int avgLat = 0, avgLong = 0, num = 0;
        for (String key : reportMap.keySet()) {
            num++;
            rep = addressToLatLng(reportMap.get(key).getStreetAddress());
            avgLat += rep.latitude;
            avgLat += rep.longitude;
            googleMap.addMarker(new MarkerOptions().position(rep)
                    .title(reportMap.get(key).getStreetAddress()));
        }

        LatLng average = new LatLng(avgLat, avgLong);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(average));//TODO: replace the average with the user's current location
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
