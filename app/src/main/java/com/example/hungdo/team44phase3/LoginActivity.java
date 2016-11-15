package com.example.hungdo.team44phase3;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import database.DatabaseAccess;
import model.User;

public class LoginActivity extends AppCompatActivity {

    DatabaseAccess data;
    private TextView reg;
    private EditText uname;
    private EditText password;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DatabaseAccess.Initialize();
        data = DatabaseAccess.getDatabaseAccess();
        data.setContext(this);

        reg = (TextView) findViewById(R.id.btnRegister);
        reg.setPaintFlags(reg.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        uname = (EditText) findViewById(R.id.uName);
        password = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.login);


        // data.deleteAllData();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.login) {
            String unamestr = uname.getText().toString();
            String passwordstr = password.getText().toString();

            User u = data.getUserByUsername(unamestr);
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
                startActivity(new Intent(this, UserScreen.class));
            }
        }
        if (v.getId() == R.id.btnRegister) {
            startActivity(new Intent(this, Signup.class));
        }
    }
}
