package com.example.hungdo.team44phase3;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import database.DatabaseAccess;
import listViewAdapter.AplicationAdapter;
import listViewAdapter.ApplyAdapter;
import model.Apply;
import model.Course;
import model.Project;
import model.User;

public class AdminViewApp extends AppCompatActivity {
    private ListView listApplication;
    private DatabaseAccess data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_app);
        data = DatabaseAccess.getDatabaseAccess();
        data.setContext(this);

        listApplication = (ListView) findViewById(R.id.listApplication);
        listApplication.setAdapter(new AplicationAdapter(this, data.getALLApplies()));
        listApplication.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(AdminViewApp.this).create(); //Read Update
                alertDialog.setTitle("DECISION");
                alertDialog.setMessage("Please choose your decision!");
                alertDialog.setButton(Dialog.BUTTON_NEUTRAL, "ACCEPT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Apply apply = (Apply) listApplication.getItemAtPosition(position);
                        System.out.print(apply.getProjectName() + " " +
                                apply.getEmail());
                        data.updateApplyStatusByProjectNameAndEmail(apply.getProjectName(),
                                apply.getEmail(), "accepted");
                        finish();
                        startActivity(getIntent());
                    }});

                alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "REJECT", new DialogInterface.OnClickListener()    {
                    public void onClick(DialogInterface dialog, int which) {
                        Apply apply = (Apply) listApplication.getItemAtPosition(position);
                        data.updateApplyStatusByProjectNameAndEmail(apply.getProjectName(),
                                apply.getEmail(), "rejected");
                        finish();
                        startActivity(getIntent());
                    }});
                alertDialog.show();  //<-- See This!
            }
        });
    }



}
