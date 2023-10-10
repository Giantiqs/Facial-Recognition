package com.example.mlseriesdemonstrator.model;

public class Student {

    private String lastName;
    private String firstName;
    private String middleName;
    private String studentID;
    private String department;
    private String course;
    private String institutionalEmail;
    private boolean activated;
    private String role;

    public Student() {

    }

    public Student(
            String lastName,
            String firstName,
            String middleName,
            String studentID,
            String department,
            String course,
            String institutionalEmail
    ) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.studentID = studentID;
        this.department = department;
        this.course = course;
        this.institutionalEmail = institutionalEmail;
        this.activated = false;
        this.role = "host";
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getInstitutionalEmail() {
        return institutionalEmail;
    }

    public void setInstitutionalEmail(String institutionalEmail) {
        this.institutionalEmail = institutionalEmail;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
