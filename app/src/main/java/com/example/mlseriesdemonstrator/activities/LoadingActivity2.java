package com.example.mlseriesdemonstrator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.utilities.Activation;
import com.example.mlseriesdemonstrator.utilities.Utility;

public class LoadingActivity2 extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading2);

        context = LoadingActivity2.this;

        String studentIdStr = getIntent().getStringExtra("student_id");

        Activation.getStudentById(studentIdStr, context, student -> {
            if (student == null) {
                Utility.showToast(context, "Student not found");
                startActivity(new Intent(context, ActivateAccountActivity.class));
            } else if (student.isActivated()) {
                Utility.showToast(context, "This account is already activated");
                startActivity(new Intent(context, ActivateAccountActivity.class));
            } else {
                Intent intent = new Intent(context, SignUpActivity.class);

                intent.putExtra("last_name", student.getLastName());
                intent.putExtra("first_name", student.getFirstName());
                intent.putExtra("middle_name", student.getMiddleName());
                intent.putExtra("email", student.getInstitutionalEmail());
                intent.putExtra("student_id", student.getStudentID());
                intent.putExtra("course", student.getCourse());

                startActivity(intent);
            }

            finish();
        });
    }
}
