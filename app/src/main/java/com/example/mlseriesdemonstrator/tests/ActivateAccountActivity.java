package com.example.mlseriesdemonstrator.tests;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.mlseriesdemonstrator.R;

public class ActivateAccountActivity extends AppCompatActivity {

    Context context;
    EditText studentNumberInput;
    Button idFinderBtn;
    TextView fullnameTxt;
    TextView studentNumberTxt;
    TextView courseTxt;
    TextView IE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_account);
        Activation.addPeople();
        context = ActivateAccountActivity.this;
        studentNumberInput = findViewById(R.id.STUDENT_NUMBER);
        idFinderBtn = findViewById(R.id.CHECK_STUDENT);
        fullnameTxt = findViewById(R.id.FULL_NAME);
        studentNumberTxt = findViewById(R.id.STUDENT_NUMBER_TXT);
        courseTxt = findViewById(R.id.COURSE_TXT);
        IE = findViewById(R.id.INSTITUTIONAL_EMAIL);

        idFinderBtn.setOnClickListener(v -> {
            String studentIdStr = studentNumberInput.getText().toString();

            Activation.getStudentById(studentIdStr, context, student -> {
                if (student != null) {
                    String fullName = student.getFirstName() + " " + student.getLastName();
                    fullnameTxt.setText(fullName);
                    studentNumberTxt.setText(student.getStudentID());
                    courseTxt.setText(student.getCourse());
                    IE.setText(student.getInstitutionalEmail());
                } else {

                }
            });
        });
    }
}
