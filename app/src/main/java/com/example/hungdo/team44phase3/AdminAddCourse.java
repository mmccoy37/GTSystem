package com.example.hungdo.team44phase3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseAccess;
import model.MultiSelectionSpinner;


public class AdminAddCourse extends AppCompatActivity {

    private MultiSelectionSpinner spinnerCat;
    private EditText fieldNumber;
    private EditText fieldName;
    private EditText fieldInstructor;
    private EditText fieldStudentCount;
    private Spinner spinnerDes;
    private Button submit;
    ArrayAdapter<String> adapter;

    DatabaseAccess data;
    private List<String> CATEGORY;
    private String DESIGNATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //databse setup
        data = DatabaseAccess.getDatabaseAccess();
        data.setConnection();
        data.setContext(this);
        //set view
        setContentView(R.layout.activity_admin_add_course);
        //listeners
        setupSpinners();
        setupOther();
    }

    private void setupSpinners() {

        spinnerCat = (MultiSelectionSpinner) findViewById(R.id.categorySpinner);
        spinnerCat.setItems(data.getCategories());
        spinnerCat.setSelectALL();

        ArrayList<String> listDes = data.getDesignations();
        spinnerDes = (Spinner) findViewById(R.id.designationSpinner);
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
    }

    private void setupOther() {
        fieldName = (EditText) findViewById(R.id.editCourseName);
        fieldNumber = (EditText) findViewById(R.id.editCourseNum);
        fieldInstructor = (EditText) findViewById(R.id.editInstructor) ;
        fieldStudentCount = (EditText) findViewById(R.id.editNumStudent);

        submit = (Button) findViewById(R.id.CourseSubmitNew);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CATEGORY = spinnerCat.getSelectedStrings();
                String CNAME = fieldName.getText().toString();
                String CNUM = fieldNumber.getText().toString();
                String CINSTR = fieldInstructor.getText().toString();
                String CCOUNT = fieldStudentCount.getText().toString();
                if (CATEGORY.size() < 1 || CNAME.equals("") || CNUM.equals("") || CINSTR.equals("") || CCOUNT.equals("")) {
                    Toast.makeText(AdminAddCourse.this, "ALL INPUT FIELDS MUST BE FILLED OUT.", Toast.LENGTH_LONG).show();
                } else {
                    String RESULT = data.addNewCourse(CNUM, CNAME, CCOUNT, CINSTR, DESIGNATION, CATEGORY);
                    Toast.makeText(AdminAddCourse.this, RESULT, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
