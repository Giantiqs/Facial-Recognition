package com.example.mlseriesdemonstrator.tests;

public class Student {

    private String lastName;
    private String firstName;
    private String middleName;
    private String studentID;
    private String course;
    private String institutionalEmail;

    public Student() {

    }

    public Student(
            String lastName,
            String firstName,
            String middleName,
            String studentID,
            String course,
            String institutionalEmail
    ) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.studentID = studentID;
        this.course = course;
        this.institutionalEmail = institutionalEmail;
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
}
