package com.example.hungdo.team44phase3;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLOutput;

import database.DatabaseAccess;
import model.AccountType;
import model.User;

public class LoginActivity extends AppCompatActivity {

    DatabaseAccess data;
    private TextView reg;
    private EditText uname;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DatabaseAccess.Initialize();
        data = DatabaseAccess.getDatabaseAccess();
        data.setConnection();
        data.setContext(this);

        reg = (TextView) findViewById(R.id.btnRegister);
        reg.setPaintFlags(reg.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        uname = (EditText) findViewById(R.id.uName);
        password = (EditText) findViewById(R.id.password);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    /**
     * Make action when click
     * @param v the object on screen
     */
    public void onClick(View v) {
        // When login button was clicked
        if (v.getId() == R.id.login) {
            String unamestr = uname.getText().toString();
            String passwordstr = password.getText().toString();


            // Check database connection
            if (data.getConnection() == null) {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("WARNING");
                alertDialog.setMessage("Cannot connect to database!\n" +
                        "1. Make sure that you are in campus\n" +
                        "2. Make sure that you are connecting to the internet");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else {
                // Database connect successfully then check conditions
                User u = data.getUserByUserName(unamestr);
                if (unamestr.equals("") || passwordstr.equals("")) {
                    Toast error = Toast.makeText(this, "Missing information!", Toast.LENGTH_SHORT);
                    error.show();
                } else if (u == null) {
                    Toast error = Toast.makeText(this, "Username is invalid!", Toast.LENGTH_SHORT);
                    error.show();
                } else if (!passwordstr.equals(u.getPassword())) {
                    Toast error = Toast.makeText(this, "Password is incorrect!", Toast.LENGTH_SHORT);
                    error.show();
                } else {
                    data.setUser(u);
                    if (u.getAccountType().equals(AccountType.STUDENT)) {
                        startActivity(new Intent(this, UserScreen.class));
                    } else {
                        startActivity(new Intent(this, AdminScreen.class));
                    }
                }
            }
        }

        // When Register was clicked
        if (v.getId() == R.id.btnRegister) {
            startActivity(new Intent(this, Signup.class));
        }
    }
}
