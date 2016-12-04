package com.example.hungdo.team44phase3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import database.DatabaseAccess;
import listViewAdapter.ReportAdapter;
import model.Apply;
import model.Report;

public class AdminAppReport extends AppCompatActivity {
    private DatabaseAccess data;
    private ListView listReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_app_report);
        data = DatabaseAccess.getDatabaseAccess();;
        data.setContext(this);
        List<Report> reports = data.getAllListReport();
        int accepted = 0;
        List<Apply> applies = data.getALLApplies();
        for (Apply a: applies) {
            if (a.getStatus().equals("accepted")) {
                accepted += 1;
            }
        }

        listReports = (ListView) findViewById(R.id.listReports);
        listReports.setAdapter(new ReportAdapter(this, reports));
        TextView numApplication = (TextView) findViewById(R.id.totalApplication);
        numApplication.setText("" + applies.size());
        TextView numAccepted = (TextView) findViewById(R.id.accepted);
        numAccepted.setText("" + accepted);

    }

}
