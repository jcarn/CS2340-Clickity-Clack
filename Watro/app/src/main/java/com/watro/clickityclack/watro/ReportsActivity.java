package com.watro.clickityclack.watro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ReportsActivity extends AppCompatActivity implements View.OnClickListener  {

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
    }

    @Override
    public void onClick(View v) {
        if (v == settingsButton) {
            startActivity(new Intent(this, ProfileActivity.class));
        }
        if (v == submitReportButton) {
            // line below will be implemented after John creates SubmitReportsActivity
            //startActivity(new Intent(this, SubmitReportsActivity.class));
        }
    }
}
