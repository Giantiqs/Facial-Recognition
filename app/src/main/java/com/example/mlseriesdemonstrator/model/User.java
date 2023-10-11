package com.example.mlseriesdemonstrator.model;

import android.content.Context;

import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class User {

    private static final String TAG = "User";
    private String lastName;
    private String firstName;
    private String middleName;
    private String role;
    private String institutionalID;
    private String department;
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
            String institutionalID,
            String department,
            String course,
            String UID,
            String password
    ) throws NoSuchAlgorithmException {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.role = role;
        this.institutionalID = institutionalID;
        this.department = department;
        this.course = course;
        this.UID = UID;
        this.passwordHashCode = Utility.hashString(password);
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

    public String getInstitutionalID() {
        return institutionalID;
    }

    public void setInstitutionalID(String institutionalID) {
        this.institutionalID = institutionalID;
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

    public void setPasswordHashCode(
            String oldPassword,
            String newPassword,
            Context context
    ) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        assert firebaseUser != null;
        AuthCredential authCredential = EmailAuthProvider.getCredential(
                Objects.requireNonNull(firebaseUser.getEmail()),
                oldPassword
        );

        firebaseUser.reauthenticate(authCredential)
                .addOnCompleteListener(task -> {
                    firebaseUser.updatePassword(newPassword);
                    try {
                        this.passwordHashCode = Utility.hashString(newPassword);

                        // Update the passwordHashCode in Firestore
                        CollectionReference userRef = Utility.getUserRef();
                        userRef.document(this.getUID()).update("passwordHashCode", this.passwordHashCode)
                                .addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        Utility.showToast(context, "Password updated!");
                                    } else {
                                        Utility.showToast(context, "Failed to update password.");
                                    }
                                });
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                })
                .addOnFailureListener(e -> Utility.showToast(context, e.getLocalizedMessage()));
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

}
