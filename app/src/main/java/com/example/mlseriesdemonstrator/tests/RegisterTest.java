package com.example.mlseriesdemonstrator.tests;

import android.content.Context;

import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class RegisterTest {

    public static CollectionReference getStudentRef() {

        return FirebaseFirestore.getInstance()
                .collection("students");
    }

    public static void addStudents() {

        Student student = new Student(
                "Tiqui",
                "Michael Gian",
                "Magsino",
                "20134903",
                "BSIT"
        );

        Student student2 = new Student(
                "Alvarez",
                "Kianna Dominique",
                "De Guzman",
                "21137836",
                "BSIT"
        );

        Student student3 = new Student(
                "Aquino",
                "Mary Rose",
                "Busa",
                "21137534",
                "BSIT"
        );

        addStudent(student);
        addStudent(student2);
        addStudent(student3);
    }

    public static void addStudent(Student student) {
        getStudentRef().document(student.getStudentID()).set(student);
    }

    public static Student getStudentById(String studentId, Context context) {

        DocumentReference studentDocRef = getStudentRef().document(studentId);
        DocumentSnapshot document = studentDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot result = task.getResult();
                if (result.exists()) {
                    Student student = result.toObject(Student.class);
                } else {
                    Utility.showToast(context, "Student ID invalid");
                }
            } else {
                Utility.showToast(context, Objects.requireNonNull(task.getException()).getLocalizedMessage());
            }
        }).getResult();

        return document.toObject(Student.class);
    }

}
