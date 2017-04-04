package com.watro.clickityclack.watro.Controller;

import android.graphics.Point;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;
import com.watro.clickityclack.watro.Model.PurityModel;
import com.watro.clickityclack.watro.Model.PurityReport;
import com.watro.clickityclack.watro.Model.Report;
import com.watro.clickityclack.watro.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class HistoryReportsView extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference databaseReference;
    private GraphView graph;
    private ArrayList<PurityReport> purityReportsArrayList;
    private HashMap<Integer, Integer> monthToVirusPPMHashMap;
    private PointsGraphSeries<DataPoint> series;
    private ArrayList<DataPoint> dataPoints;
    private Spinner virusOrContaminantSpinner;
    private ArrayAdapter<CharSequence> virusOrContaminantAdapter;
    private Spinner locationSpinner;
    private ArrayAdapter<String> locationAdapter;
    private Button updateGraphButton;
    private HashSet<String> locationHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_reports_view);

        graph = (GraphView) findViewById(R.id.graph);
        virusOrContaminantSpinner = (Spinner) findViewById(R.id.virusOrContaminantSpinner);
        virusOrContaminantAdapter = ArrayAdapter.createFromResource(this, R.array.virusOrContaminant, R.layout.support_simple_spinner_dropdown_item);
        virusOrContaminantAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        virusOrContaminantSpinner.setAdapter(virusOrContaminantAdapter);
        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        updateGraphButton = (Button) findViewById(R.id.updateGraphButton);
        updateGraphButton.setOnClickListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        locationHash = new HashSet<>();
        locationHash.add("All Locations");
        DatabaseReference locationReference = databaseReference.child("PurityReports");
        locationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    PurityReport purityReport = (PurityReport) child.getValue(PurityReport.class);
                    locationHash.add(purityReport.getStreetAddress());
                }
                if (locationAdapter == null) {
                    ArrayList<String> listOfLocations = new ArrayList<String>(locationHash);
                    locationAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listOfLocations);
                    locationAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(locationAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Chooosing not to show data until the user selects to update. Allows to dynamically update locations
        //showData(String.valueOf(virusOrContaminantSpinner.getSelectedItem()), "");

    }
    private void showData(final String virusOrContaminant, final String address) {
        final DatabaseReference purityReference = databaseReference.child("PurityReports");
        purityReportsArrayList = new ArrayList<>();
        monthToVirusPPMHashMap = new HashMap<>();
        series = new PointsGraphSeries<>();
        dataPoints = new ArrayList<>();
        graph.removeAllSeries();
        graph.getGridLabelRenderer().setVerticalAxisTitle(virusOrContaminant);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Month");

        purityReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int currReportPPM;
                for (DataSnapshot child: dataSnapshot.getChildren())  {
                    PurityReport purityReport = (PurityReport) child.getValue(PurityReport.class);
                    if (purityReport.getStreetAddress().equals(address) || address.equals("All Locations")) {
                        String reportDate = purityReport.getReportDate();
                        if (virusOrContaminant.equals("Virus PPM")) {
                            currReportPPM = Integer.valueOf(purityReport.getVirusPPM());
                        } else {
                            currReportPPM = Integer.valueOf(purityReport.getContaminantPPM());
                        }

                        String[] datePieces = reportDate.split("-");

                        int currReportMonth = Integer.valueOf(datePieces[0]);

                        // This is wrong as it doesn't collect all the virusPPMs of a month...
                        monthToVirusPPMHashMap.put(currReportMonth, currReportPPM);

                        purityReportsArrayList.add(purityReport);

                        dataPoints.add(new DataPoint(currReportMonth, currReportPPM)) ;
                    }
                }
                DataPoint[] arrayConvert = new DataPoint[dataPoints.size()];
                arrayConvert = dataPoints.toArray(arrayConvert);
                series = new PointsGraphSeries<DataPoint>(arrayConvert);
                graph.addSeries(series);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        if (v == updateGraphButton) {
            showData(String.valueOf(virusOrContaminantSpinner.getSelectedItem()), String.valueOf(locationSpinner.getSelectedItem()));
        }
    }

}
