package com.example.mlseriesdemonstrator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.Utility;

import java.security.NoSuchAlgorithmException;

public class EditNameActivity extends AppCompatActivity {

    private EditText firstNameTxt;
    private EditText middleNameTxt;
    private EditText lastNameTxt;
    private EditText currentPasswordTxt;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);

        Button editDetailsBtn = findViewById(R.id.EDIT_DETAILS_BTN);

        user = Utility.getUser();
        firstNameTxt = findViewById(R.id.FIRST_NAME_TXT);
        middleNameTxt = findViewById(R.id.MIDDLE_NAME_TXT);
        lastNameTxt = findViewById(R.id.LAST_NAME_TXT);
        currentPasswordTxt = findViewById(R.id.PASSWORD_TXT);

        firstNameTxt.setText(user.getFirstName());
        middleNameTxt.setText(user.getMiddleName());
        lastNameTxt.setText(user.getLastName());

        editDetailsBtn.setOnClickListener(v -> {
            try {
                editDone();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void editDone() throws NoSuchAlgorithmException {

        String firstNameStr = firstNameTxt.getText().toString();
        String middleNameStr = middleNameTxt.getText().toString();
        String lastNameStr = lastNameTxt.getText().toString();
        String currentPasswordStr = currentPasswordTxt.getText().toString();

        if (firstNameStr.isEmpty())
            firstNameTxt.setError("First name required");
        if (middleNameStr.isEmpty())
            middleNameTxt.setError("Middle name required");
        if (lastNameStr.isEmpty())
            middleNameTxt.setError("Last name required");
        if (!Utility.verifyHash(currentPasswordStr, user.getPasswordHashCode()))
            currentPasswordTxt.setError("Wrong password");

        Intent intent = new Intent(EditNameActivity.this, ConfirmActivity.class);
        intent.putExtra("first_name", firstNameStr);
        intent.putExtra("middle_name", middleNameStr);
        intent.putExtra("last_name", lastNameStr);

        startActivity(intent);
        finish();
    }
}