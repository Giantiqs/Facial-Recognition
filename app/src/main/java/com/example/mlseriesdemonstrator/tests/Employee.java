package com.example.mlseriesdemonstrator.tests;

public class Employee {

    private String lastName;
    private String firstName;
    private String middleName;
    private String employeeID;
    private String institutionalEmail;
    private boolean activated;

    public Employee() {

    }

    public Employee(
            String lastName,
            String firstName,
            String middleName,
            String employeeID,
            String institutionalEmail
    ) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.employeeID = employeeID;
        this.institutionalEmail = institutionalEmail;
        this.activated = false;
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

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String studentID) {
        this.employeeID = studentID;
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
}
