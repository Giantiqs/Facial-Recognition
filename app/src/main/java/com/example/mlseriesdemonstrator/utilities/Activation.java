package com.example.mlseriesdemonstrator.utilities;

import android.content.Context;

import com.example.mlseriesdemonstrator.model.Employee;
import com.example.mlseriesdemonstrator.model.Student;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Activation {

    public interface StudentCallback {
        void onStudentRetrieved(Student student);
    }

    public interface EmployeeCallback {
        void onEmployeeRetrieved(Employee employee);
    }

    public static CollectionReference getRefByName(String collectionName) {
        return FirebaseFirestore.getInstance()
                .collection(collectionName);
    }

    public static void addPeople() {
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

        Employee employee = new Employee(
                "Tiqui",
                "Michael Gian",
                "Magsino",
                "1",
                "michaelgiantiqui3@gmail.com"
        );

        Employee employee2 = new Employee(
                "Alvarez",
                "Kianna Dominique",
                "De Guzman",
                "2",
                "kiannaalvarez@gmail.com"
        );

        addStudent(student);
        addStudent(student2);
        addStudent(student3);
        addEmployee(employee);
    }

    public static void addStudent(Student student) {
        String studentId = student.getStudentID();
        CollectionReference studentsCollection = getRefByName("students");

        // Check if the student with the given ID already exists
        studentsCollection.document(studentId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (!document.exists())
                    studentsCollection.document(studentId).set(student);
            } else {

            }
        });
    }

    public static void addEmployee(Employee employee) {
        String employeeId = employee.getEmployeeID();
        CollectionReference employeesCollection = getRefByName("employees");

        // Check if the employee with the given ID already exists
        employeesCollection.document(employeeId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (!document.exists())
                    employeesCollection.document(employeeId).set(employee);
            } else {

            }
        });
    }


    public static void getStudentById(String studentId, Context context, StudentCallback studentCallback) {
        DocumentReference studentDocRef = getRefByName("students").document(studentId);
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

    public static void getEmployeeById(String employeeId, Context context, EmployeeCallback employeeCallback) {
        DocumentReference studentDocRef = getRefByName("employees").document(employeeId);
        studentDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot result = task.getResult();
                if (result.exists()) {
                    Employee employee = result.toObject(Employee.class);
                    employeeCallback.onEmployeeRetrieved(employee);
                } else {
                    Utility.showToast(context, "Employee ID invalid");
                    employeeCallback.onEmployeeRetrieved(null);
                }
            } else {
                Utility.showToast(context, Objects.requireNonNull(task.getException()).getLocalizedMessage());
                employeeCallback.onEmployeeRetrieved(null);
            }
        });
    }

    public static void activateStudent(String id) {
        CollectionReference studentsCollection = getRefByName("students");

        studentsCollection.document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Student student = document.toObject(Student.class);

                    assert student != null;
                    student.setActivated(true);

                    studentsCollection.document(id).set(student);
                }
            } else {

            }
        });
    }

    public static void activateEmployee(Employee employee) {
        String employeeId = employee.getEmployeeID();
        CollectionReference employeesCollection = getRefByName("employees");

        employeesCollection.document(employeeId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (!document.exists()) {
                    employee.setActivated(true);
                    employeesCollection.document(employeeId).set(employee);
                }
            } else {

            }
        });
    }

}
