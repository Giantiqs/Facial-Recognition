package com.example.mlseriesdemonstrator.activities.student;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.LoadingActivity2;
import com.example.mlseriesdemonstrator.activities.host.ActivateEmployeeAccountActivity;
import com.example.mlseriesdemonstrator.utilities.Activation;

public class ActivateStudentAccountActivity extends AppCompatActivity {

  Context context;
  EditText studentNumberInput;
  TextView notAStudent;
  Button idFinderBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activate_student);

    Activation.addPeople();
    context = ActivateStudentAccountActivity.this;
    studentNumberInput = findViewById(R.id.STUDENT_NUMBER);
    notAStudent = findViewById(R.id.NOT_STUDENT);
    idFinderBtn = findViewById(R.id.CHECK_STUDENT);

    notAStudent.setOnClickListener(v -> {
      startActivity(new Intent(context, ActivateEmployeeAccountActivity.class));
      finish();
    });

    idFinderBtn.setOnClickListener(v -> {
      String studentIdStr = studentNumberInput.getText().toString();

      Intent intent = new Intent(context, LoadingActivity2.class);

      if (studentIdStr.isEmpty()) {
        studentNumberInput.setError("Please enter your student ID");
        return;
      }

      intent.putExtra("student_id", studentIdStr);
      intent.putExtra("mode", "student");

      startActivity(intent);
      finish();
    });
  }
}
