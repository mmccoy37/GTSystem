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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseAccess;
import model.Course;
import model.MultiSelectionSpinner;
import model.Project;
import model.YearLevel;

public class UserScreen extends Activity {

    private MultiSelectionSpinner spinnerCat;
    private EditText title;
    private Spinner spinnerDes;
    private Spinner spinnerMajor;
    private Spinner spinnerYear;
    private ListView listView;

    ArrayAdapter<String> adapter;

    DatabaseAccess data;
    private ImageButton btnMe;
    private Button search;
    private Button reset;
    private String YEAR;
    private List<String> CATEGORY;
    private String DESIGNATION;
    private String MAJOR;
    private String TITLE;
    public static int TYPE_IS_PROJECT = 0;
    public static int TYPE_IS_COURSE = 1;
    public static int TYPE_IS_BOTH = 2;
    private int TYPE = TYPE_IS_BOTH;
    public static Course COURSE;
    public static Project PROJECT;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);
        this.context = this;
        //database intitialize
        //DatabaseAccess.Initialize();
        data = DatabaseAccess.getDatabaseAccess();
        data.setConnection();
        data.setContext(this);
        // Hide keyboard when open Activity
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //setup listview and spinners
        setupFields();
    }

    private void setupFields() {
        title = (EditText) findViewById(R.id.title);

        // Setup ListView
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new ArrayAdapter<Object>(context, android.R.layout.simple_list_item_1,
                data.getAllCourseAndProject()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listView.getItemAtPosition(position).getClass() == Course.class) {
                    COURSE = (Course) listView.getItemAtPosition(position);
                    startActivity(new Intent(context, CourseInfoScreen.class));
                } else {
                    PROJECT = (Project) listView.getItemAtPosition(position);
                    startActivity(new Intent(context, ViewAndApplyProjectScreen.class));
                }
            }
        });

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

        //ArrayList<String> listYear = data.getYears();

        spinnerYear = (Spinner) findViewById(R.id.spinnerYear);
        ArrayAdapter<YearLevel> adapterYear = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,YearLevel.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapterYear);
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

        btnMe = (ImageButton) findViewById(R.id.btnMe);
        btnMe.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ProfileActivity.class));
            }
        });

        search = (Button) findViewById(R.id.button_search);
        search.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                CATEGORY = spinnerCat.getSelectedStrings();
                TITLE = title.getText().toString();
                listView.setAdapter(
                        new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                                data.getMainPageResults(TITLE, CATEGORY, DESIGNATION, MAJOR, YEAR, TYPE)));
            }
        });

        reset = (Button) findViewById(R.id.btn_Reset);
        reset.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerCat.setSelectALL();
                spinnerYear.setSelection(0);
                spinnerDes.setSelection(0);
                spinnerMajor.setSelection(0);
                title.setText("");
                RadioButton rb = (RadioButton) findViewById(R.id.radioButton);
                rb.setChecked(true);

                listView.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                        data.getAllCourseAndProject()));
            }
        });

    }

    /**
     * Action when the item is clicked
     * @param v View on screen
     */
    public void onClick(View v) {
        // If button me is clicked
    }


}
