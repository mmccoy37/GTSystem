package com.example.hungdo.team44phase3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import database.DatabaseAccess;
import model.Project;

public class ViewAndApplyProjectScreen extends Activity {

    TextView projectName;
    TextView projectContent;
    DatabaseAccess data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_and_apply_project_screen);
        data = DatabaseAccess.getDatabaseAccess();
        data.setContext(this);

        Project p = UserScreen.PROJECT;
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

        applyClick();
    }

    private void applyClick() {
        Button apply = (Button) findViewById(R.id.btnApply);
        apply.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
