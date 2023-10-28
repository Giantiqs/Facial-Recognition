package com.example.mlseriesdemonstrator.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;

public class UserDetailsActivity extends AppCompatActivity {

  String UID;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_details);

    Intent intent = getIntent();
    String lastName = intent.getStringExtra("last_name");
    String firstName = intent.getStringExtra("first_name");
    String middleName = intent.getStringExtra("middle_name");
    String role = intent.getStringExtra("role");
    String institutionalID = intent.getStringExtra("institutional_id");
    String department = intent.getStringExtra("department");
    String course = intent.getStringExtra("course");
    UID = intent.getStringExtra("UID");
    String password = intent.getStringExtra("password");
    String institutionalEmail = intent.getStringExtra("IE");

    TextView lastNameTextView = findViewById(R.id.lastNameTextView);
    TextView firstNameTextView = findViewById(R.id.firstNameTextView);
    TextView middleNameTextView = findViewById(R.id.middleNameTextView);
    TextView roleTextView = findViewById(R.id.roleTextView);
    TextView institutionalIdTextView = findViewById(R.id.institutionalIdTextView);
    TextView departmentTextView = findViewById(R.id.departmentTextView);
    TextView courseTextView = findViewById(R.id.courseTextView);
    TextView passwordTextView = findViewById(R.id.passwordTextView);
    TextView institutionalEmailTextView = findViewById(R.id.institutionalEmailTextView);

    lastNameTextView.setText(lastName);
    firstNameTextView.setText(firstName);
    middleNameTextView.setText(middleName);
    roleTextView.setText(role);
    institutionalIdTextView.setText(institutionalID);
    departmentTextView.setText(department);
    courseTextView.setText(course);
    passwordTextView.setText(password);
    institutionalEmailTextView.setText(institutionalEmail);
  }

}