package com.example.hungdo.team44phase3;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import database.DatabaseAccess;
import model.Course;

public class CourseInfoScreen extends Activity {

    private DatabaseAccess data;
    TextView courseIfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info_screen);
        courseIfo = (TextView) findViewById(R.id.courseContent);
        data = DatabaseAccess.getDatabaseAccess();
        data.setContext(this);

        Course c = UserScreen.COURSE;
        List<String> cat = data.getCategoriesOfCourseName(c.getName());

        String category = cat.get(0);
        for (int i = 1; i < cat.size(); i++) {
            category += ", " + cat.get(i);
        }
        String info = "Course Name: " + c.getName() +
                "\nInstructor: " + c.getInstructor() +
                "\nDesignation: " + c.getDesignation() +
                "\nCategory: " + category +
                "\nEstimate Number Of Students: " + c.getNum_Student();
        courseIfo.setText(info);
    }
}
