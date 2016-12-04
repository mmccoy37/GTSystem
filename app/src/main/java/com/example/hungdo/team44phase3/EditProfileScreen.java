package com.example.hungdo.team44phase3;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseAccess;
import model.MultiSelectionSpinner;
import model.User;
import model.YearLevel;

/**
 * Created by Nhu Ng on 11/21/2016.
 */

public class EditProfileScreen extends AppCompatActivity {
    private Spinner spinnerMajor;
    private Spinner spinnerYear;
    private DatabaseAccess data;
    private TextView deptText;
    private Button btnUpdate;
    private User u;
    private YearLevel yearLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_myprofile);
        data = DatabaseAccess.getDatabaseAccess();
        data.setContext(this);
        u = data.getUserByUserName(LoginActivity.USERNAME);
        if (u.getMajor() == null) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("!!!!!");
            alertDialog.setMessage("You are missing your Major and Year information!" +
                    "\nPlease update now!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        if (u.getYear() != 0) {
            if (u.getYear() == 1) {
                yearLevel = YearLevel.FR;
            } else if (u.getYear() == 2) {
                yearLevel = YearLevel.SO;
            } else if (u.getYear() == 3) {
                yearLevel = YearLevel.JR;
            } else {
                yearLevel = YearLevel.SR;
            }
        }

        deptText = (TextView) findViewById(R.id.deptTxt);

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
                String maj = spinnerMajor.getSelectedItem().toString();
                deptText.setText(data.getDeptNameFromMajor(maj));
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
        adapterY.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapterY);

        if (u.getMajor() != null) {
            spinnerMajor.setSelection(adapter.getPosition(u.getMajor()));
            spinnerYear.setSelection(adapterY.getPosition(yearLevel));
        }

    }


    public void onClick(View v) {
        if (v.getId() == R.id.btnUpdate) {
            data.updateUserByEmail(u.getEmail(), (String) spinnerMajor.getSelectedItem()
                    , ((YearLevel) spinnerYear.getSelectedItem()).getCode());
            Toast toast = Toast.makeText(this, "Change Successful", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
