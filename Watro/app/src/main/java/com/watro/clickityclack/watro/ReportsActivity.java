package com.watro.clickityclack.watro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ReportsActivity extends AppCompatActivity implements View.OnClickListener  {

    private ImageButton settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        settingsButton = (ImageButton) findViewById(R.id.settingsButton);

        settingsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == settingsButton) {
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }
}
