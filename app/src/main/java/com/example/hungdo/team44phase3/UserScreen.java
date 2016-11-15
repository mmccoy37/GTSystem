package com.example.hungdo.team44phase3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import model.MultiSelectionSpinner;

public class UserScreen extends Activity {

    private MultiSelectionSpinner spinnerCat;
    private Spinner spinnerDes;
    private Spinner spinnerMajor;
    private Spinner spinnerYear;
    private ListView listView;
    private String[] arrs = new String[]{
            "do something",
            "get something",
            "create something",
            "make something",
            "build something",
            "laugh something",
            "punch something",
            "sing something",
            "see something",
            "point something"
    };
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);

        // Hide keyboard when open Activity
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Setup ListView
        listView = (ListView) findViewById(R.id.listview);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrs);
        listView.setAdapter(adapter);

        // Setup spinners
        spinnerCat = (MultiSelectionSpinner) findViewById(R.id.spinnerCat);
        List<String> list = new ArrayList<>();
        list.add("All");
        list.add("Cat 1");
        list.add("Cat 2");
        list.add("Cat 3");
        list.add("Cat 4");
//        adapter = new ArrayAdapter<>(
//                this, android.R.layout.simple_spinner_item,list);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCat.setItems(list);
        spinnerCat.setSelectALL();

        spinnerDes = (Spinner) findViewById(R.id.spinnerDes);
        list = new ArrayList<>();
        list.add("All");
        list.add("Job Title 1");
        list.add("Job Title 2");
        list.add("Job Title 3");
        list.add("Job Title 4");
        adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDes.setAdapter(adapter);

        spinnerMajor = (Spinner) findViewById(R.id.spinnerMajor);
        list = new ArrayList<>();
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

    /**
     * Action when the item is clicked
     * @param v View on screen
     */
    public void onClick(View v) {
        // If button me is clicked
        if (v.getId() == R.id.btnMe) {
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }
}
