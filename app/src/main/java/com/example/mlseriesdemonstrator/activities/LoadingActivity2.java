package com.example.mlseriesdemonstrator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.host.ActivateEmployeeAccountActivity;
import com.example.mlseriesdemonstrator.activities.student.ActivateStudentAccountActivity;
import com.example.mlseriesdemonstrator.utilities.Activation;
import com.example.mlseriesdemonstrator.utilities.Utility;

public class LoadingActivity2 extends AppCompatActivity {

    Context context;
    String mode;
    final String notApplicable = "N/A";
    final String studentMode = "student";
    final String hostMode = "host";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading2);

        context = LoadingActivity2.this;

        mode = getIntent().getStringExtra("mode");

        assert mode != null;
        if (mode.equals(studentMode)) {
            activateStudentAccount();
        } else if (mode.equals(hostMode)) {
            activateHostAccount();
        }

    }

    private void activateStudentAccount() {

        String studentIdStr = getIntent().getStringExtra("student_id");

        Activation.getStudentById(studentIdStr, context, null, student -> {
            if (student == null) {
                Utility.showToast(context, "Student not found");
                startActivity(new Intent(context, ActivateStudentAccountActivity.class));
            } else if (student.isActivated()) {
                Utility.showToast(context, "This account is already activated");
                startActivity(new Intent(context, ActivateStudentAccountActivity.class));
            } else {
                Intent intent = new Intent(context, SignUpActivity.class);

                intent.putExtra("last_name", student.getLastName());
                intent.putExtra("first_name", student.getFirstName());
                intent.putExtra("middle_name", student.getMiddleName());
                intent.putExtra("email", student.getInstitutionalEmail());
                intent.putExtra("student_id", student.getStudentID());
                intent.putExtra("course", student.getCourse());
                intent.putExtra("mode", mode);

                startActivity(intent);
            }

            finish();
        });
    }

    private void activateHostAccount() {

        String employeeIdStr = getIntent().getStringExtra("employee_id");

        Activation.getEmployeeById(employeeIdStr, context, null, employee -> {
            if (employee == null) {
                Utility.showToast(context, "Employee not found");
                startActivity(new Intent(context, ActivateEmployeeAccountActivity.class));
            } else if (employee.isActivated()) {
                Utility.showToast(context, "This account is already activated");
                startActivity(new Intent(context, ActivateEmployeeAccountActivity.class));
            } else {
                Intent intent = new Intent(context, SignUpActivity.class);

                intent.putExtra("last_name", employee.getLastName());
                intent.putExtra("first_name", employee.getFirstName());
                intent.putExtra("middle_name", employee.getMiddleName());
                intent.putExtra("email", employee.getInstitutionalEmail());
                intent.putExtra("employee_id", employee.getEmployeeID());
                intent.putExtra("course", notApplicable);
                intent.putExtra("mode", mode);

                startActivity(intent);
            }

            finish();
        });
    }

}
