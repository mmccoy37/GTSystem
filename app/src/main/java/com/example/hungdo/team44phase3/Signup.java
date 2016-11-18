package com.example.hungdo.team44phase3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseAccess;

import model.User;

public class Signup extends AppCompatActivity {

    private TextView gatechEmail;
    private EditText email;
    private EditText uname;
    private EditText pass1;
    private EditText pass2;
    private Spinner spinnerYear;
    private Spinner spinnerMajor;

    private DatabaseAccess data = DatabaseAccess.getDatabaseAccess();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        data.setContext(this);

        // Hide keyboard when open Activity
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        gatechEmail = (TextView) findViewById(R.id.gatechMail);
        gatechEmail.setText("@gatech.edu");
        email = (EditText) findViewById(R.id.email);
        uname = (EditText) findViewById(R.id.uName);
        pass1 = (EditText) findViewById(R.id.pass1);
        pass2 = (EditText) findViewById(R.id.pass2);

        // Setup spinnerYear
        spinnerYear = (Spinner) findViewById(R.id.spinnerYear);
        List<String> list = new ArrayList<>();
        list.add("YEAR");
        list.add("FRESHMAN");
        list.add("SOPHOMORE");
        list.add("JUNIOR");
        list.add("SENIOR");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(dataAdapter);

        // Setup spinnerMajor
        spinnerMajor = (Spinner) findViewById(R.id.spinnerMajor);
        list = new ArrayList<>();
        list.add("MAJOR");
        list.add("CS");
        list.add("CM");
        list.add("EE");
        list.add("MATH");
        dataAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMajor.setAdapter(dataAdapter);

    }

    public void onClick(View v) {
        if (v.getId() == R.id.btnCreate) {
            String emailstr = email.getText().toString();
            String unamestr = uname.getText().toString();
            String pass1str = pass1.getText().toString();
            String pass2str = pass2.getText().toString();
            String gatechEmailstr = gatechEmail.getText().toString();
            int year = 0;
            String yearstr = spinnerYear.getSelectedItem().toString();
            String majorstr = spinnerMajor.getSelectedItem().toString();
            if (yearstr.equals("FRESHMAN")) {
                year = 1;
            } else if (yearstr.equals("SOPHOMORE")) {
                year = 2;
            } else if (yearstr.equals("JUNIOR")) {
                year = 3;
            } else {
                year = 4;
            }

            if (yearstr.equals("YEAR") || majorstr.equals("MAJOR") || emailstr.equals("")
                    || unamestr.equals("") || pass1str.equals("") || pass2str.equals("")) {
                Toast pass = Toast.makeText(this, "Some information are missing!", Toast.LENGTH_SHORT);
                pass.show();
            } else if (pass1str.length() < 8 || pass2str.length() < 8) {
                Toast pass = Toast.makeText(this, "Passwords must have 8 or more characters"
                        , Toast.LENGTH_SHORT);
                pass.show();
            } else if (!pass1str.equals(pass2str)) {
                Toast pass = Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT);
                pass.show();
            } else {
                try {
                    User newUser = new User(unamestr, pass1str, emailstr
                            + gatechEmailstr, majorstr, year);
                    data.createUser(newUser);
                    Toast toast = Toast.makeText(this, "Account was created!", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                } catch (Exception e) {
                    Toast toast = Toast.makeText(this, "Email or username is already exist", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    }
}
