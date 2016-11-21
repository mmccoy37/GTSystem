package com.example.hungdo.team44phase3;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

//    Button editProfile;
//    Button viewMyApp;
//    TextView uName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        editProfile = (Button) findViewById(R.id.editProfileBtn);
//        viewMyApp = (Button) findViewById(R.id.myAppBtn);
//
//        //go to new page from button
//        editProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //navigate to new activity
//                Intent showInfo = new Intent(ProfileActivity.this, editProfileActivity.class);
//                ProfileActivity.this.startActivity(showInfo);
//            }
//        });
    }

    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            //navigate to new activity
            Intent showInfo = new Intent(ProfileActivity.this, UserScreen.class);
            ProfileActivity.this.startActivity(showInfo);
        }

        if (v.getId() == R.id.editProfileBtn) {
            //navigate to new activity
            Intent showInfo = new Intent(ProfileActivity.this, editProfileActivity.class);
            ProfileActivity.this.startActivity(showInfo);
        }


    }

}
