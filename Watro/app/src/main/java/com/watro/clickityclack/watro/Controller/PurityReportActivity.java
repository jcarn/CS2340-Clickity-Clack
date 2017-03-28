package com.watro.clickityclack.watro.Controller;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.watro.clickityclack.watro.Model.Administrator;
import com.watro.clickityclack.watro.Model.BasicUser;
import com.watro.clickityclack.watro.Model.Manager;
import com.watro.clickityclack.watro.Model.Worker;
import com.watro.clickityclack.watro.R;

import java.util.HashMap;

public class PurityReportActivity extends AppCompatActivity implements View.OnClickListener {
    private Button submitPurityReportButton;
    private ImageButton returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purity_report);
        submitPurityReportButton = (Button) findViewById(R.id.submitPurityReportButton);
        returnButton = (ImageButton) findViewById(R.id.returnButton);
        submitPurityReportButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == submitPurityReportButton) {
            int changeThis = 0;
        }
        if (v == returnButton) {
            startActivity(new Intent(this, ReportsActivity.class));
        }
    }
}
