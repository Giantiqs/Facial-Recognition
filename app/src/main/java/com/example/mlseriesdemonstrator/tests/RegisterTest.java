package com.example.mlseriesdemonstrator.tests;

import android.content.Context;

import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class RegisterTest {

    public interface StudentCallback {
        void onStudentRetrieved(Student student);
    }

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
                "BSIT",
                "tiquimichaelgian_bsit@plmun.edu.ph"
        );

        Student student2 = new Student(
                "Alvarez",
                "Kianna Dominique",
                "De Guzman",
                "21137836",
                "BSIT",
                "alvarezkiannadominique_bsit@plmun.edu.ph"
        );

        Student student3 = new Student(
                "Aquino",
                "Mary Rose",
                "Busa",
                "21137534",
                "BSIT",
                "aquinomaryrose_bsit@plmun.edu.ph"
        );

        addStudent(student);
        addStudent(student2);
        addStudent(student3);
    }

    public static void addStudent(Student student) {
        getStudentRef().document(student.getStudentID()).set(student);
    }

    public static void getStudentById(String studentId, Context context, StudentCallback studentCallback) {
        DocumentReference studentDocRef = getStudentRef().document(studentId);
        studentDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot result = task.getResult();
                if (result.exists()) {
                    Student student = result.toObject(Student.class);
                    studentCallback.onStudentRetrieved(student);
                } else {
                    Utility.showToast(context, "Student ID invalid");
                    studentCallback.onStudentRetrieved(null);
                }
            } else {
                Utility.showToast(context, Objects.requireNonNull(task.getException()).getLocalizedMessage());
                studentCallback.onStudentRetrieved(null);
            }
        });
    }
}
