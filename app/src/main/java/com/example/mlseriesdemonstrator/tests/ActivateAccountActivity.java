package com.example.mlseriesdemonstrator.tests;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mlseriesdemonstrator.R;

import java.security.interfaces.EdECKey;

public class ActivateAccountActivity extends AppCompatActivity {

    Context context;
    EditText studentNumberInput;
    Button idFinderBtn;
    TextView fullnameTxt;
    TextView studentNumberTxt;
    TextView courseTxt;
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_account);
        RegisterTest.addStudents();
        context = ActivateAccountActivity.this;
        studentNumberInput = findViewById(R.id.STUDENT_NUMBER);
        idFinderBtn = findViewById(R.id.CHECK_STUDENT);
        fullnameTxt = findViewById(R.id.FULL_NAME);
        studentNumberTxt = findViewById(R.id.STUDENT_NUMBER_TXT);
        courseTxt = findViewById(R.id.COURSE_TXT);

        idFinderBtn.setOnClickListener(v -> {
            String studentIdStr = studentNumberInput.getText().toString();

            student = RegisterTest.getStudentById(studentIdStr, context);

            String fullname = student.getFirstName() + " " + student.getLastName();

            fullnameTxt.setText(fullname);
            studentNumberTxt.setText(student.getStudentID());
            courseTxt.setText(student.getCourse());
        });

    }
}