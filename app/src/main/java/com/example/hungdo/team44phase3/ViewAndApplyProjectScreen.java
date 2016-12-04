package com.example.hungdo.team44phase3;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import database.DatabaseAccess;
import exception.DupplicateProjectName;
import listViewAdapter.AplicationAdapter;
import model.Apply;
import model.Project;
import model.User;
import model.YearLevel;

public class ViewAndApplyProjectScreen extends Activity {

    private TextView projectName;
    private TextView projectContent;
    private DatabaseAccess data;
    private User u;
    private Project p;
    private String currentDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_and_apply_project_screen);
        data = DatabaseAccess.getDatabaseAccess();
        data.setContext(this);

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        currentDay = year + "-" + month + "-" + day;

        u = data.getUserByUserName(LoginActivity.USERNAME);

        p = UserScreen.PROJECT;
        projectName = (TextView) findViewById(R.id.projectName);
        projectName.setText(p.getName());

        projectContent = (TextView) findViewById(R.id.projectContent);

        List<String> cat = data.getCategoriesOfProjectName(p.getName());
        String category = cat.get(0);
        for (int i = 1; i < cat.size(); i++) {
            category += ", " + cat.get(i);
        }

        List<String> req = data.getListRequireByProjectName(p.getName());
        String requirements = req.get(0) + " students only";
        for (int i = 1; i < req.size(); i++) {
            requirements += ", " + req.get(i) + " students only";
        }
        if (requirements.equals("null students only")) {
            requirements = "No requirement";
        }

        String content = "Advisor: " + p.getAdvisor_Name() +
                "\nAdvisor Email: " + p.getAdvisor_Email() +
                "\nDescription: " + p.getDescription() +
                "\nDesignation: " + p.getDesignation() +
                "\nCategory: " + category +
                "\nRequirements: " + requirements +
                "\nEstimated Number Of Students: " + p.getNum_Student();
        projectContent.setText(content);
    }



    public void onClick(View v) {
        if (v.getId() == R.id.btnApply) {
            User u = data.getUserByUserName(LoginActivity.USERNAME);
            int yearNum = u.getYear();
            String yearName = "";
            if (yearNum == 1) {
                yearName = YearLevel.FR.toString();
            } else if (yearNum == 2){
                yearName = YearLevel.SO.toString();
            } else if (yearNum == 3) {
                yearName = YearLevel.JR.toString();
            } else {
                yearName = YearLevel.SR.toString();
            }
            String major = u.getMajor();
            if (major == null) {
                AlertDialog alertDialog = new AlertDialog.Builder(ViewAndApplyProjectScreen.this).create(); //Read Update
                alertDialog.setTitle("MISSING INFORMATION");
                alertDialog.setMessage("You need to update your Major and Year first!");
                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }});
                alertDialog.show();
            } else {
                List<Project> listProject = data.getListProjectByFilter(data.getCategories(), p.getDesignation(), major, yearName);
                if (listProject.contains(p)) {
                    try {
                        data.applyProject(u.getEmail(), p.getName(), currentDay);
                        Toast toast = Toast.makeText(this, "Apply Successful", Toast.LENGTH_SHORT);
                        toast.show();
                    } catch (DupplicateProjectName e) {
                        Toast toast = Toast.makeText(this, "You applied this project before!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(this, "You are not qualify to apply!", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        }
    }
}
