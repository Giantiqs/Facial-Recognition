package com.example.mlseriesdemonstrator.model;

import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class User {

    private String lastName;
    private String firstName;
    private String middleName;
    private String role;
    private String studentID;
    private String course;
    private String UID;
    private String passwordHashCode;
    private double[][] faceVector;

    public User() {

    }

    public User(
            String lastName,
            String firstName,
            String middleName,
            String role,
            String studentID,
            String course,
            String UID,
            String password
    ) throws NoSuchAlgorithmException {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.role = role;
        this.studentID = studentID;
        this.course = course;
        this.UID = UID;
        String passHashCode = Utility.hashString(password);
        this.passwordHashCode = passHashCode;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public double[][] getFaceVector() {
        return faceVector;
    }

    public void setFaceVector(double[][] faceVector) {
        this.faceVector = faceVector;
    }

    public String getPasswordHashCode() {
        return passwordHashCode;
    }

    public void setPasswordHashCode(String oldPassword, String newPassword) throws NoSuchAlgorithmException {

//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        assert firebaseUser != null;
//        AuthCredential authCredential = EmailAuthProvider.getCredential(
//                Objects.requireNonNull(firebaseUser.getEmail()),
//                oldPassword
//        );
//
//        firebaseUser.reauthenticate(authCredential)
//                .addOnCompleteListener(task -> firebaseUser.updatePassword(newPassword))
//                .addOnFailureListener(e -> {
//
//                });

        this.passwordHashCode = Utility.hashString(newPassword);
    }

}
