package model;

/**
 * Created by hungdo on 12/2/16.
 */

public class Apply {
    private String email;
    private String projectName;
    private String date;
    private String status;

    public Apply(String email, String projectName, String date, String status) {
        this.email = email;
        this.projectName = projectName;
        this.date = date;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
