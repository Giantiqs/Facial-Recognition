package com.example.mlseriesdemonstrator.utilities;

import android.content.Context;
import android.util.Log;

import com.example.mlseriesdemonstrator.model.Employee;
import com.example.mlseriesdemonstrator.model.Student;
import com.example.mlseriesdemonstrator.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;

public class AccountManager {

  private static final String TAG = "AccountManager";
  public interface StudentCallback {
    void onStudentRetrieved(Student student);
  }

  public interface EmployeeCallback {
    void onEmployeeRetrieved(Employee employee);
  }

  public static CollectionReference getRefByName(String collectionName) {
    return FirebaseFirestore.getInstance().collection(collectionName);
  }

  public static void addPeople() {

    ArrayList<Student> students = new ArrayList<>();

    students.add(
            new Student(
                    "Tiqui",
                    "Michael Gian",
                    "Magsino",
                    "20134903",
                    "CITCS",
                    "BSIT",
                    "tiquimichaelgian_bsit@plmun.edu.ph"
            )
    );

    students.add(
            new Student(
                    "Alvarez",
                    "Kianna Dominique",
                    "De Guzman",
                    "21137836",
                    "CITCS",
                    "BSIT",
                    "alvarezkiannadominique_bsit@plmun.edu.ph"
            )
    );

    students.add(
            new Student(
                    "Aquino",
                    "Mary Rose",
                    "Busa",
                    "21137534",
                    "CITCS",
                    "BSIT",
                    "aquinomaryrose_bsit@plmun.edu.ph"
            )
    );

    students.add(
            new Student(
                    "Aquino",
                    "Mary Rose",
                    "Busa",
                    "21137534",
                    "CITCS",
                    "BSIT",
                    "aquinomaryrose_bsit@plmun.edu.ph"
            )
    );

    ArrayList<Employee> employees = new ArrayList<>();

    employees.add(
            new Employee(
                    "Tiqui",
                    "Michael Gian",
                    "Magsino",
                    "h1",
                    "michaelgiantiqui3@gmail.com"
            )
    );

    employees.add(
            new Employee(
                    "Alvarez",
                    "Kianna Dominique",
                    "De Guzman",
                    "h2",
                    "kyanahalvarez@gmail.com"
            )
    );

    Employee admin = new Employee(
            "Tiqui",
            "Michael Gian",
            "Magsino",
            "a1",
            "giantiqui1@gmail.com"
    );

    admin.setRole("admin");

    employees.add(admin);

    for (Student s : students)
      addStudent(s);

    for (Employee e : employees)
      addEmployee(e);
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
        Log.d(TAG, Objects.requireNonNull(Objects.requireNonNull(task.getException()).getLocalizedMessage()));
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
        Log.d(TAG, Objects.requireNonNull(Objects.requireNonNull(task.getException()).getLocalizedMessage()));
      }
    });
  }

  public static void getStudentById(String studentId, Context context, String mode, StudentCallback studentCallback) {

    DocumentReference studentDocRef = getRefByName("students").document(studentId);
    studentDocRef.get().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        DocumentSnapshot result = task.getResult();
        if (result.exists()) {
          Student student = result.toObject(Student.class);
          studentCallback.onStudentRetrieved(student);
        } else {
          if (!"login".equals(mode))
            Utility.showToast(context, "Student ID invalid");
          studentCallback.onStudentRetrieved(null);
        }
      } else {
        Utility.showToast(context, Objects.requireNonNull(task.getException()).getLocalizedMessage());
        studentCallback.onStudentRetrieved(null);
      }
    });
  }

  public static void getEmployeeById(String employeeId, Context context, String mode, EmployeeCallback employeeCallback) {

    DocumentReference studentDocRef = getRefByName("employees").document(employeeId);
    studentDocRef.get().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        DocumentSnapshot result = task.getResult();
        if (result.exists()) {
          Employee employee = result.toObject(Employee.class);
          employeeCallback.onEmployeeRetrieved(employee);
        } else {
          if (!"login".equals(mode))
            Utility.showToast(context, "Employee ID invalid");
          employeeCallback.onEmployeeRetrieved(null);
        }
      } else {
        Utility.showToast(context, Objects.requireNonNull(task.getException()).getLocalizedMessage());
        employeeCallback.onEmployeeRetrieved(null);
      }
    });
  }

  public static void activateStudent(String studentId) {

    CollectionReference studentsCollection = getRefByName("students");

    studentsCollection.document(studentId).get().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        DocumentSnapshot document = task.getResult();
        if (document.exists()) {
          Student student = document.toObject(Student.class);

          assert student != null;
          student.setActivated(true);

          studentsCollection.document(studentId).set(student);
        }
      } else {
        Log.d(TAG, Objects.requireNonNull(Objects.requireNonNull(task.getException()).getLocalizedMessage()));
      }
    });
  }

  public static void activateEmployee(String employeeId) {

    CollectionReference employeesCollection = getRefByName("employees");

    employeesCollection.document(employeeId).get().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        DocumentSnapshot document = task.getResult();
        if (document.exists()) {
          Employee employee = document.toObject(Employee.class);

          assert employee != null;
          employee.setActivated(true);

          employeesCollection.document(employeeId).set(employee);
        }
      } else {
        Log.d(TAG, Objects.requireNonNull(Objects.requireNonNull(task.getException()).getLocalizedMessage()));
      }
    });
  }

}
