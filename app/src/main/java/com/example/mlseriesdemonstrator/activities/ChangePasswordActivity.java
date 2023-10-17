package com.example.mlseriesdemonstrator.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.Utility;

import java.security.NoSuchAlgorithmException;

public class ChangePasswordActivity extends AppCompatActivity {

  private static final String TAG = "ChangePasswordActivity";
  private Context context;
  private EditText oldPasswordTxt;
  private EditText newPasswordTxt;
  private EditText reEnteredNewPasswordTxt;
  private User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_change_password);
    Log.d(TAG, "m");

    // Make the elements in the screen interactive.
    Button changePasswordBtn = findViewById(R.id.CHANGE_PASSWORD_BTN);
    oldPasswordTxt = findViewById(R.id.OLD_PASSWORD);
    newPasswordTxt = findViewById(R.id.NEW_PASSWORD);
    reEnteredNewPasswordTxt = findViewById(R.id.RE_ENTER_NEW_PASSWORD);

    context = ChangePasswordActivity.this;

    // Set the user details in this variable.
    user = Utility.getUser();

    // When the button is clicked, perform the actions.
    changePasswordBtn.setOnClickListener(v -> {

      // Get texts from user input.
      String oldPasswordStr = oldPasswordTxt.getText().toString();
      String newPassword = newPasswordTxt.getText().toString();
      String reEnteredNewPasswordStr = reEnteredNewPasswordTxt.getText().toString();

      try {
        // Validate the passwords input
        if (validateData(oldPasswordStr, newPassword, reEnteredNewPasswordStr)) {
          // If the password passed the test, do this action.
          confirmData(oldPasswordStr, newPassword);
        }
      } catch (NoSuchAlgorithmException e) {
        Utility.showToast(context, e.getLocalizedMessage());
      }
    });
  }

  private boolean validateData(
          String oldPasswordStr,
          String newPasswordStr,
          String reEnteredNewPasswordStr
  ) throws NoSuchAlgorithmException {

    // Check if the old password input is same as the password that user has through their hash.
    if (!Utility.verifyHash(oldPasswordStr, user.getPasswordHashCode())) {
      oldPasswordTxt.setError("Wrong password.");
      return false;
    }

    // Check if newPasswordStr is same as reEnteredNewPasswordStr.
    if (!newPasswordStr.equals(reEnteredNewPasswordStr)) {
      reEnteredNewPasswordTxt.setError("Password doesn't match.");
      return false;
    }

    if (newPasswordStr.isEmpty()) {
      newPasswordTxt.setError("This field is required");
      return false;
    }

    if (reEnteredNewPasswordStr.isEmpty()) {
      reEnteredNewPasswordTxt.setError("This field is required");
      return false;
    }

    return newPasswordStr.length() >= 8;
  }

  private void confirmData(String oldPasswordStr, String newPasswordStr) {

    // Intent is responsible for passing and receiving of data from screen to screen.
    Intent intent = new Intent(context, ConfirmActivity.class);

    // Puts the values to be passed to another screen.
    intent.putExtra("new_password", newPasswordStr);
    intent.putExtra("old_password", oldPasswordStr);
    intent.putExtra("mode", "change_password");

    // Starts the target activity
    startActivity(intent);
  }
}