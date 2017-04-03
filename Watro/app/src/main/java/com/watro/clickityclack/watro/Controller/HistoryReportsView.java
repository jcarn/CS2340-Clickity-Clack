package com.watro.clickityclack.watro.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.watro.clickityclack.watro.Model.PurityModel;
import com.watro.clickityclack.watro.Model.PurityReport;
import com.watro.clickityclack.watro.Model.Report;
import com.watro.clickityclack.watro.R;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryReportsView extends AppCompatActivity {

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_reports_view);

        final GraphView graph = (GraphView) findViewById(R.id.graph);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference purityReference = databaseReference.child("PurityReports");

        final ArrayList<PurityReport> purityReportsArrayList = new ArrayList<>();

        final HashMap<Integer, Integer> monthToVirusPPMHashMap = new HashMap<>();

        final PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3)
        });


        final DataPoint[] dataPoints = new DataPoint[100];

        graph.getGridLabelRenderer().setVerticalAxisTitle("PPM");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Month");

        purityReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int i = 0;

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child: children)  {
                    PurityReport purityReport = (PurityReport) child.getValue(PurityReport.class);
                    String reportDate = purityReport.getReportDate();

                    int currReportVirusPPM = Integer.valueOf(purityReport.getVirusPPM());

                    String[] datePieces = reportDate.split("-");

                    int currReportMonth = Integer.valueOf(datePieces[0]);

                    // This is wrong as it doesn't collect all the virusPPMs of a month...
                    monthToVirusPPMHashMap.put(currReportMonth, currReportVirusPPM);

                    purityReportsArrayList.add(purityReport);

                    dataPoints[i] = new DataPoint(currReportMonth, currReportVirusPPM);

                    PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(dataPoints);

                    graph.addSeries(series);
                    i++;
//                    location = purityReport.getStreetAddress();
//                    overallCondition = purityReport.getWaterCondition();
//                    virusPPM = purityReport.getVirusPPM();
//                    contaminantPPM = purityReport.getContaminantPPM();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        graph.addSeries(series);
    }

}
