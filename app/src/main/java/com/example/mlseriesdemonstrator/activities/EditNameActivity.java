package com.example.mlseriesdemonstrator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.mlseriesdemonstrator.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditNameActivity extends AppCompatActivity {

    private EditText currentPassword;
    private EditText firstNameTxt;
    private EditText middleNameTxt;
    private EditText lastNameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);
        Button editDetailsBtn = findViewById(R.id.EDIT_DETAILS_BTN);
        currentPassword = findViewById(R.id.CURRENT_PASSWORD);
        firstNameTxt = findViewById(R.id.FIRST_NAME_TXT);
        middleNameTxt = findViewById(R.id.MIDDLE_NAME_TXT);
        lastNameTxt = findViewById(R.id.LAST_NAME_TXT);


        editDetailsBtn.setOnClickListener(v -> editDone());
    }

    private void editDone() {
        String currentPasswordStr = currentPassword.getText().toString();
        String firstNameStr = firstNameTxt.getText().toString();
        String middleNameStr = middleNameTxt.getText().toString();
        String lastNameStr = lastNameTxt.getText().toString();

        // create a method that can change user details of curr user without using the password
        // continue later

        if (!currentPasswordStr.equals("put firebase account password here")) {
            currentPassword.setError("Password incorrect.");
            return;
        }

        Intent intent = new Intent(EditNameActivity.this, EditNameActivity.class);
        intent.putExtra("first_name", firstNameStr);
        intent.putExtra("middle_name", middleNameStr);
        intent.putExtra("last_name", lastNameStr);

        startActivity(intent);
        finish();
    }
}