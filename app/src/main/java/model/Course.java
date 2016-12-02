package model;

/**
 * Created by hungdo on 11/28/16.
 */

public class Course {
    private String course_Num;
    private String name;
    private String instructor;
    private int num_Student;
    private String designation;

    public Course(String course_Num, String name, int num_Student, String instructor, String designation) {
        this.course_Num = course_Num;
        this.name = name;
        this.num_Student = num_Student;
        this.instructor = instructor;
        this.designation = designation;
    }

    // Getters
    public String getCourse_Num() {
        return course_Num;
    }

    public String getName() {
        return name;
    }

    public String getInstructor() {
        return instructor;
    }

    public int getNum_Student() {
        return num_Student;
    }

    public String getDesignation() {
        return designation;
    }


    // Setters
    public void setCourse_Num(String course_Num) {
        this.course_Num = course_Num;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setNum_Student(int num_Student) {
        this.num_Student = num_Student;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public String toString() {
        return name + " (Course)";
    }

}
