package com.example.mlseriesdemonstrator.utilities;

import android.content.Context;
import android.util.Log;

import com.example.mlseriesdemonstrator.model.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
* Declare this class once the CSV file is ready but make sure to declare it before sign up/in screens
 */

public class CsvBridge {

  private final String TAG = "CsvBridge";
  private final String fileName = "data.csv"; // Name of your CSV file in assets

  public CsvBridge(Context context) {

  }

  public void transferStudents(Context context) {

    ArrayList<Student> students = new ArrayList<>();

    try {
      InputStream inputStream = context.getAssets().open(fileName);

      BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
      String line;
      String splitBy = ",";

      while ((line = br.readLine()) != null) {
        String[] data = line.split(splitBy);

        if (data.length >= 7) {
          Student student = new Student();
          student.setStudentID(data[0].trim());
          student.setLastName(data[1].trim());
          student.setFirstName(data[2].trim());
          student.setMiddleName(data[3].trim());
          student.setInstitutionalEmail(data[4].trim());
          student.setCourse(data[5].trim());
          student.setDepartment(data[6].trim());

          students.add(student);

          String studentInfo = String.format(
                  "%s %s %s %s %s %s %s",
                  student.getStudentID(),
                  student.getLastName(),
                  student.getFirstName(),
                  student.getMiddleName(),
                  student.getInstitutionalEmail(),
                  student.getCourse(),
                  student.getDepartment()
          );

          Log.d(TAG, studentInfo);

          AccountManager.addStudent(student);
        }
      }
    } catch (IOException e) {
      Log.e(TAG, "Error reading CSV from assets: " + e.getLocalizedMessage());
    } finally {

    }

    Utility.showToast(context, "Students records updated.");
  }
}
