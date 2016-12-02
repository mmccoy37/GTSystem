package com.example.hungdo.team44phase3;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseAccess;
import model.MultiSelectionSpinner;
import model.YearLevel;

/**
 * Created by Nhu Ng on 11/21/2016.
 */

public class EditProfileScreen extends AppCompatActivity {
    Spinner spinnerMajor;
    Spinner spinnerYear;
    DatabaseAccess data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_myprofile);
        data = DatabaseAccess.getDatabaseAccess();
        data.setContext(this);


        // Setup spinners
        spinnerMajor = (Spinner) findViewById(R.id.spinnerMajor);
        List<String> listM = data.getMajors();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,listM);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMajor.setAdapter(adapter);
        spinnerMajor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing
            }
        });

        // Setup spinners
        spinnerYear = (Spinner) findViewById(R.id.spinnerYear);
        ArrayAdapter<YearLevel> adapterY = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,YearLevel.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapterY);
    }


    public void onClick(View v) {
        //
    }

}
