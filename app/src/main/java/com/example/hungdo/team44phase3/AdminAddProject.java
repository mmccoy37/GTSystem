package com.example.hungdo.team44phase3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseAccess;
import model.MultiSelectionSpinner;
import model.YearLevel;
import model.YearLevelExtra;

public class AdminAddProject extends AppCompatActivity {

    private EditText fieldAdvEmail;
    private EditText fieldAdvisor;
    private EditText fieldDescription;
    private EditText fieldStudentCount;
    private  EditText fieldName;
    private Spinner spinnerDes;
    private Spinner spinnerMajor;
    private Spinner spinnerYear;
    private Spinner spinnerDept;
    private MultiSelectionSpinner spinnerCat;
    private Button submit;
    ArrayAdapter<String> adapter;

    DatabaseAccess data;
    private List<String> CATEGORY;
    private String DESIGNATION;
    private String YEAR;
    private String MAJOR;
    private String DEPARTMENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //databse setup
        data = DatabaseAccess.getDatabaseAccess();
        data.setConnection();
        data.setContext(this);
        //set view
        setContentView(R.layout.activity_admin_add_project);
        //listeners
        setupSpinners();
        setupOther();

    }

    private void setupSpinners() {

        ArrayList<String> listMajor = data.getMajors();
        listMajor.add(0, "N/A");
        spinnerMajor = (Spinner) findViewById(R.id.projMajorReqSpinner);
        adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,listMajor);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMajor.setAdapter(adapter);
        spinnerMajor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MAJOR = spinnerMajor.getSelectedItem().toString();
                if (MAJOR.equals("N/A")) {
                    MAJOR = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing
            }
        });

        //ArrayList<String> listYear = data.getYears();

        spinnerYear = (Spinner) findViewById(R.id.projYearReqSpinner);
        ArrayAdapter<YearLevelExtra> adapterYear = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,YearLevelExtra.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapterYear);
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                YEAR = "" + ((YearLevelExtra)spinnerYear.getSelectedItem());
                if (YEAR.equals("N/A")) {
                    YEAR = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing
            }
        });

        spinnerCat = (MultiSelectionSpinner) findViewById(R.id.categorySpinnerProj);
        spinnerCat.setItems(data.getCategories());
        spinnerCat.setSelectALL();

        ArrayList<String> listDes = data.getDesignations();
        spinnerDes = (Spinner) findViewById(R.id.designationSpinnerProj);
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

        ArrayList<String> listDept = data.getDepartments();
        listDept.add(0, "N/A");
        spinnerDept = (Spinner) findViewById(R.id.projDepReqSpinner);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listDept);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDept.setAdapter(adapter);
        spinnerDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DEPARTMENT = spinnerDept.getSelectedItem().toString();
                if (DEPARTMENT.equals("N/A")) {
                    DEPARTMENT = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setupOther() {
        fieldAdvisor = (EditText) findViewById(R.id.editProjectAdv);
        fieldAdvEmail = (EditText) findViewById(R.id.editProjectAdvEm);
        fieldDescription = (EditText) findViewById(R.id.editProjectDesc);
        fieldStudentCount = (EditText) findViewById(R.id.editProjNumStudent);
        fieldName = (EditText) findViewById(R.id.editProjectName);

        submit = (Button) findViewById(R.id.projButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CATEGORY = spinnerCat.getSelectedStrings();
                String PNAME = fieldName.getText().toString();
                String PADV = fieldAdvisor.getText().toString();
                String PEMAIL = fieldAdvEmail.getText().toString();
                String PDESC = fieldDescription.getText().toString();
                String PCOUNT = fieldStudentCount.getText().toString();
                if (CATEGORY.size() < 1 || PADV.equals("") || PEMAIL.equals("") || PDESC.equals("") || PCOUNT.equals("") || PNAME.equals("")) {
                    Toast.makeText(AdminAddProject.this, "ALL INPUT FIELDS MUST BE FILLED OUT.", Toast.LENGTH_LONG).show();
                } else {
                    String RESULT = data.addNewProject(PNAME, PCOUNT, PDESC, PADV,
                            PEMAIL, DESIGNATION, YEAR, MAJOR, DEPARTMENT, CATEGORY);
                    Toast.makeText(AdminAddProject.this, RESULT, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
