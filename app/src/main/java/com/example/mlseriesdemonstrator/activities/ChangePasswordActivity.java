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

import okhttp3.internal.Util;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldPasswordTxt;
    private EditText newPasswordTxt;
    private EditText reEnteredNewPasswordTxt;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        
        Button changePasswordBtn = findViewById(R.id.CHANGE_PASSWORD_BTN);
        
        user = Utility.getUser();
        oldPasswordTxt = findViewById(R.id.OLD_PASSWORD);
        newPasswordTxt = findViewById(R.id.NEW_PASSWORD);
        reEnteredNewPasswordTxt = findViewById(R.id.RE_ENTER_NEW_PASSWORD);

        String oldPasswordStr = oldPasswordTxt.getText().toString();
        String newPassword = newPasswordTxt.getText().toString();
        String reEnteredNewPasswordStr = reEnteredNewPasswordTxt.getText().toString();
        
        changePasswordBtn.setOnClickListener(v -> {

            try {
                if (validateData(oldPasswordStr, newPassword, reEnteredNewPasswordStr)) {
                    confirmData(newPassword);
                }
            } catch (NoSuchAlgorithmException e) {
                Utility.showToast(ChangePasswordActivity.this, e.getLocalizedMessage());
            }
        });
    }

    private boolean validateData(
            String oldPasswordStr,
            String newPasswordStr,
            String reEnteredNewPasswordStr
    ) throws NoSuchAlgorithmException {

        if (!Utility.verifyHash(oldPasswordStr, user.getPasswordHashCode())) 
            return false;
        if (!newPasswordStr.equals(reEnteredNewPasswordStr))
            return false;
        
        return newPasswordStr.length() >= 8;
    }

    private void confirmData(String newPasswordStr) {

        Intent intent = new Intent(ChangePasswordActivity.this, ConfirmActivity.class);

        intent.putExtra("new_password", newPasswordStr);
        intent.putExtra("mode", "change_password");

        startActivity(intent);
    }
}