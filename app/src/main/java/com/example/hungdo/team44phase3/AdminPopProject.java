package com.example.hungdo.team44phase3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import database.DatabaseAccess;
import listViewAdapter.PopularProjectAdapter;

public class AdminPopProject extends AppCompatActivity {
    private ListView listpopular;
    private DatabaseAccess data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pop_project);
        data = DatabaseAccess.getDatabaseAccess();
        data.setContext(this);

        listpopular = (ListView) findViewById(R.id.listPopular);
        listpopular.setAdapter(new PopularProjectAdapter(this, data.getListAppliesWithNumberOfApplicant()));
    }

}
