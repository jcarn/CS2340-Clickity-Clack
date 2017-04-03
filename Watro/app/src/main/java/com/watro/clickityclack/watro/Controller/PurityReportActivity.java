package com.watro.clickityclack.watro.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.watro.clickityclack.watro.Model.BasicUser;
import com.watro.clickityclack.watro.Model.PurityAdapter;
import com.watro.clickityclack.watro.Model.PurityModel;
import com.watro.clickityclack.watro.Model.PurityReport;
import com.watro.clickityclack.watro.R;

import java.util.ArrayList;

import static com.watro.clickityclack.watro.R.layout.activity_history_reports_view;

public class PurityReportActivity extends AppCompatActivity implements View.OnClickListener {
    private Button submitPurityReportButton;
    private Button viewHistoryButton;

    private ImageButton returnButton;
    private ListView purityReportListView;
    private PurityAdapter adapter;
    ArrayList<PurityModel> models;
    ArrayList<String> workerIDs;
    private DatabaseReference databaseReference;
    String reportDate;
    String reportID;
    String workerID;
    String location;
    String overallCondition;
    String virusPPM;
    String contaminantPPM;
    PurityModel pureModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purity_report);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference purityReference = databaseReference.child("PurityReports");
        models = new ArrayList<>();
        workerIDs = new ArrayList<>();
        purityReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child: children)  {
                    PurityReport pureReport = (PurityReport) child.getValue(PurityReport.class);
                    reportDate = pureReport.getReportDate();
                    reportID = pureReport.getReportID();
                    location = pureReport.getStreetAddress();
                    overallCondition = pureReport.getWaterCondition();
                    virusPPM = pureReport.getVirusPPM();
                    contaminantPPM = pureReport.getContaminantPPM();
                    workerID = pureReport.getReporterID();
                    pureModel = new PurityModel(reportDate, reportID, "", location, overallCondition, virusPPM,contaminantPPM);
                    workerIDs.add(workerID);
                    models.add(pureModel);
                }
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
                for (String x: workerIDs) {
                    BasicUser user = (BasicUser) dataSnapshot.child(x).getValue(BasicUser.class);
                    models.get(index).setWorkerName(user.getFirstName() + " " + user.getLastName());
                    index++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        submitPurityReportButton = (Button) findViewById(R.id.submitPurityReportButton);
        returnButton = (ImageButton) findViewById(R.id.returnButton);
        purityReportListView = (ListView) findViewById(R.id.purityReportListView);
        viewHistoryButton = (Button) findViewById(R.id.viewHistoryButton);

        viewHistoryButton.setOnClickListener(this);
        submitPurityReportButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        adapter = new PurityAdapter(models, getApplicationContext());
        purityReportListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == viewHistoryButton) {
            startActivity(new Intent(this, HistoryReportsView.class));
        }
        if (v == submitPurityReportButton) {
            startActivity(new Intent(this, SubmitPurityReportActivity.class));
        }
        if (v == returnButton) {
            startActivity(new Intent(this, ReportsActivity.class));
        }
    }
}
