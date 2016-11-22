package com.example.hungdo.team44phase3;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import model.MultiSelectionSpinner;



/**
 * Created by Nhu Ng on 11/21/2016.
 */



public class EditProfile extends Activity {

    private Spinner spinnerMajor;
    private Spinner spinnerYear;
    private ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Setup ListView
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

        spinnerMajor = (Spinner) findViewById(R.id.spinnerMajor);
        List<String> list = new ArrayList<>();

        list.add("All");
        list.add("CS");
        list.add("EE");
        list.add("CM");
        list.add("ME");
        list.add("CE");
        list.add("MATH");
        list.add("DS");
        adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMajor.setAdapter(adapter);



        spinnerYear = (Spinner) findViewById(R.id.spinnerYear);
        list = new ArrayList<>();
        list.add("All");
        list.add("FRESHMAN");
        list.add("SOPHOMORE");
        list.add("JUNIOR");
        list.add("SENIOR");
        adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapter);
    }
}
