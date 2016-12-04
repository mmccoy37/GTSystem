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

    /**
     * Set equals to compare course number.
     * @param obj the user instance
     * @return whether the object is a course and their number matches
     */
    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Course)) {
            return false;
        }
        Course c = (Course) obj;
        return this.course_Num.equals(c.getCourse_Num());
    }

    /**
     * Set hash code equal to the username's hashcode.
     * @return the username's hashcode
     */
    @Override
    public int hashCode() {
        return course_Num.hashCode();
    }

    @Override
    public String toString() {
        return name + " (Course)";
    }

    public String getType() {
        return "Course";
    }

}
