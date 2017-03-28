package com.watro.clickityclack.watro.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.watro.clickityclack.watro.Model.PurityReport;
import com.watro.clickityclack.watro.R;

public class SubmitPurityReportActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton returnButton;
    private Button submitButton;
    private EditText editTextAddress;
    private EditText editTextVirus;
    private EditText editTextContaminant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_purity_report);
        returnButton = (ImageButton) findViewById(R.id.returnButton);
        returnButton.setOnClickListener(this);
        submitButton = (Button) findViewById(R.id.buttonSubmitPurityReport);
        submitButton.setOnClickListener(this);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextVirus = (EditText) findViewById(R.id.editTextVirus);
        editTextContaminant = (EditText) findViewById(R.id.editContaminant);
    }

    @Override
    public void onClick(View v) {
        if (v == returnButton) {
            finish();
            startActivity(new Intent(this, PurityReportActivity.class));
        }
    }
}
