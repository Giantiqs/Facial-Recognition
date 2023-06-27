package com.example.mlseriesdemonstrator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.mlseriesdemonstrator.R;

public class SignUpActivity extends AppCompatActivity {

    EditText lastNameTxt, firstNameTxt, middleNameTxt, passwordTxt, confirmPasswordTxt, emailTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        lastNameTxt = findViewById(R.id.LAST_NAME_TXT);
        firstNameTxt = findViewById(R.id.FIRST_NAME_TXT);
        middleNameTxt = findViewById(R.id.MIDDLE_NAME_tXT);
        passwordTxt = findViewById(R.id.PASSWORD_TXT_UP);
        confirmPasswordTxt = findViewById(R.id.CONFIRM_PASSWORD_TXT);
        emailTxt = findViewById(R.id.EMAIL_TXT_UP);

        
    }
}