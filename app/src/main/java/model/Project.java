package model;

/**
 * Created by hungdo on 11/28/16.
 */

public class Project {
    private String name;
    private String description;
    private String advisor_Email;
    private String advisor_Name;
    private int num_Student;
    private String designation;



    public Project(String name, int num_Student, String description, String advisor_Name,
                   String advisor_Email, String designation) {
        this.name = name;
        this.description = description;
        this.advisor_Email = advisor_Email;
        this.advisor_Name = advisor_Name;
        this.num_Student = num_Student;
        this.designation = designation;
    }

    //Getter
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAdvisor_Email() {
        return advisor_Email;
    }

    public String getAdvisor_Name() {
        return advisor_Name;
    }

    public int getNum_Student() {
        return num_Student;
    }

    public String getDesignation() {
        return designation;
    }

    //Setter
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAdvisor_Email(String advisor_Email) {
        this.advisor_Email = advisor_Email;
    }

    public void setAdvisor_Name(String advisor_Name) {
        this.advisor_Name = advisor_Name;
    }

    public void setNum_Student(int num_Student) {
        this.num_Student = num_Student;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * Set equals to compare name.
     * @param obj the user instance
     * @return whether the object is a project and their name matches
     */
    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Project)) {
            return false;
        }
        Project p = (Project) obj;
        return this.name.equals(p.getName());
    }

    /**
     * Set hash code equal to the username's hashcode.
     * @return the username's hashcode
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name + " (Project)";
    }

    public String getType() {
        return "Project";
    }
}
