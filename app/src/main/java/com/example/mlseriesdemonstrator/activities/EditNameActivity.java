package com.example.mlseriesdemonstrator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import okhttp3.internal.Util;

public class EditNameActivity extends AppCompatActivity {

    private EditText firstNameTxt;
    private EditText middleNameTxt;
    private EditText lastNameTxt;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);

        user = Utility.getUser();
        Button editDetailsBtn = findViewById(R.id.EDIT_DETAILS_BTN);
        firstNameTxt = findViewById(R.id.FIRST_NAME_TXT);
        middleNameTxt = findViewById(R.id.MIDDLE_NAME_TXT);
        lastNameTxt = findViewById(R.id.LAST_NAME_TXT);


        editDetailsBtn.setOnClickListener(v -> editDone());
    }

    private void editDone() {

        String firstNameStr = firstNameTxt.getText().toString();
        String middleNameStr = middleNameTxt.getText().toString();
        String lastNameStr = lastNameTxt.getText().toString();

        firstNameTxt.setHint(user.getFirstName());
        middleNameTxt.setHint(user.getMiddleName());
        lastNameTxt.setHint(user.getLastName());

        if (firstNameStr.isEmpty()) {
            firstNameTxt.setError("First name required");
        }

        Intent intent = new Intent(EditNameActivity.this, EditNameActivity.class);
        intent.putExtra("first_name", firstNameStr);
        intent.putExtra("middle_name", middleNameStr);
        intent.putExtra("last_name", lastNameStr);

        startActivity(intent);
        finish();
    }
}