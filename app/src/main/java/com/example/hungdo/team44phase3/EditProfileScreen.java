package com.example.hungdo.team44phase3;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import model.MultiSelectionSpinner;

/**
 * Created by Nhu Ng on 11/21/2016.
 */

public class EditProfileScreen extends AppCompatActivity {
    Spinner spinnerMajor;
    Spinner spinnerYear;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_myprofile);


        // Setup spinners
        spinnerMajor = (Spinner) findViewById(R.id.spinnerMajor);
        List<String> listMajor = new ArrayList<>();
        listMajor.add("CS");
        listMajor.add("CM");
        listMajor.add("EE");
        listMajor.add("ME");
        adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,listMajor);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMajor.setAdapter(adapter);

        // Setup spinners
        spinnerYear = (Spinner) findViewById(R.id.spinnerYear);
        List<String> listYear = new ArrayList<>();
        listYear.add("Cat 1");
        listYear.add("Cat 2");
        listYear.add("Cat 3");
        listYear.add("Cat 4");
        adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,listYear);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapter);
    }


    public void onClick(View v) {
        //
    }

}
