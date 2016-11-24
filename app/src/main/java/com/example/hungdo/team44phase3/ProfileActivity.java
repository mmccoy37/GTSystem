package com.example.hungdo.team44phase3;

import android.content.Intent;
import android.graphics.Paint;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    Button editProfile;
    Button viewMyApp;
    TextView uName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView viewApp = (TextView) findViewById(R.id.btnEdit);
        viewApp.setPaintFlags(viewApp.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        TextView viewPopularApp = (TextView) findViewById(R.id.btnMyApplication);
        viewPopularApp.setPaintFlags(viewPopularApp.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btnEdit) {
            //TODO: Navigate to new Activitive
            startActivity(new Intent(this, EditProfileScreen.class));
        }
        if (v.getId() == R.id.btnMyApplication) {
            //TODO: Navigate to new Activitive
            startActivity(new Intent(this, MyApplicationScreen.class));
        }
    }

}
