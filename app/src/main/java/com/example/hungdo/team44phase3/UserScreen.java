package com.example.hungdo.team44phase3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseAccess;
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

    DatabaseAccess data;
    private String YEAR;
    private String CATEGORY;
    private String DESIGNATION;
    private String MAJOR;
    private String TITLE;
    public static int TYPE_IS_PROJECT = 0;
    public static int TYPE_IS_COURSE = 1;
    public static int TYPE_IS_BOTH = 2;
    private int TYPE = TYPE_IS_BOTH;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);
        this.context = this;
        //database intitialize
        DatabaseAccess.Initialize();
        data = DatabaseAccess.getDatabaseAccess();
        data.setConnection();
        data.setContext(this);
        // Hide keyboard when open Activity
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //setup listview and spinners
        setupFields();
    }

    private void setupFields() {
        // Setup ListView
        listView = (ListView) findViewById(R.id.listview);
        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrs);
        //listView.setAdapter(adapter);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group_1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton b1 = (RadioButton) findViewById(R.id.radioButton); //both
                RadioButton b2 = (RadioButton) findViewById(R.id.radioButton2); //Course
                RadioButton b3 = (RadioButton) findViewById(R.id.radioButton3); //Project
                if (b3.isChecked()) {
                    TYPE = TYPE_IS_PROJECT;
                } else if (b2.isChecked()) {
                    TYPE = TYPE_IS_COURSE;
                } else {
                    TYPE = TYPE_IS_BOTH;
                }
            }
        });
        // category spinner
        //ArrayList<String> listCat = data.getCategories();
        spinnerCat = (MultiSelectionSpinner) findViewById(R.id.spinnerCat);
        //ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCat);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinnerCat.setAdapter(adapter);
        spinnerCat.setItems(data.getCategories());
        spinnerCat.setSelectALL();
        spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CATEGORY = spinnerCat.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing
            }
        });


        ArrayList<String> listDes = data.getDesignations();
        spinnerDes = (Spinner) findViewById(R.id.spinnerDes);
        adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,listDes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDes.setAdapter(adapter);
        spinnerDes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DESIGNATION = spinnerDes.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing
            }
        });

        ArrayList<String> listMajor = data.getMajors();
        spinnerMajor = (Spinner) findViewById(R.id.spinnerMajor);
        adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,listMajor);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMajor.setAdapter(adapter);
        spinnerMajor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MAJOR = spinnerMajor.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing
            }
        });

        ArrayList<String> listYear = data.getYears();
        spinnerYear = (Spinner) findViewById(R.id.spinnerYear);
        adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,listYear);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapter);
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                YEAR = spinnerYear.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing
            }
        });

        Button buttonSearch = (Button) findViewById(R.id.button_search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setAdapter(
                        new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,
                                data.getMainPageResults(TITLE, CATEGORY, DESIGNATION, MAJOR, YEAR, TYPE)));
            }
        });




        ImageButton profButton = (ImageButton) findViewById(R.id.btnMe);
        profButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ProfileActivity.class));
            }
        });
    }

    /**
     * Action when the item is clicked
     * @param v View on screen
     */
    public void onClick(View v) {
        // If button me is clicked
        //if (v.getId() == R.id.btnMe) {
        //    startActivity(new Intent(this, ProfileActivity.class));
        //}
    }


}
