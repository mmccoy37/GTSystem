package model;

/**
 * Created by hungdo on 11/28/16.
 */

public class Course {
    private int course_Num;
    private String name;
    private String instructor;
    private int num_Student;
    private String designation;

    public Course(int course_Num, String name, String instructor, int num_Student, String designation) {
        this.course_Num = course_Num;
        this.name = name;
        this.instructor = instructor;
        this.num_Student = num_Student;
        this.designation = designation;
    }

    // Getters
    public int getCourse_Num() {
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
    public void setCourse_Num(int course_Num) {
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
        return name;
    }

}
