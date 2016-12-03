package com.example.hungdo.team44phase3;

import android.app.Activity;
import android.os.Bundle;
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
import model.Project;
import model.User;

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
        String requirements = req.get(0);
        for (int i = 1; i < req.size(); i++) {
            requirements += ", " + req.get(i);
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
            try {
                data.applyProject(u.getEmail(), p.getName(), currentDay);
                Toast toast = Toast.makeText(this, "Apply Successful", Toast.LENGTH_SHORT);
                toast.show();
            } catch (DupplicateProjectName e) {
                Toast toast = Toast.makeText(this, "You applied this project before!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
