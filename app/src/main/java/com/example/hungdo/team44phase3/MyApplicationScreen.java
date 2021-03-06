package com.example.hungdo.team44phase3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseAccess;
import listViewAdapter.ApplyAdapter;
import model.Apply;
import model.MultiSelectionSpinner;
import model.User;

public class MyApplicationScreen extends Activity {

    private ListView listMyApp;
    private DatabaseAccess data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myapp);
        data = DatabaseAccess.getDatabaseAccess();
        data.setContext(this);

        User u = data.getUserByUserName(LoginActivity.USERNAME);

        listMyApp = (ListView) findViewById(R.id.listMyApp);
        List<Apply> list = data.getAppliesByEmail(u.getEmail());

        if (list.size() == 0) {
            Toast toast = Toast.makeText(this, "You did not apply any project!", Toast.LENGTH_SHORT);
            toast.show();
        }
        listMyApp.setAdapter(new ApplyAdapter(this, list));

    }

}

