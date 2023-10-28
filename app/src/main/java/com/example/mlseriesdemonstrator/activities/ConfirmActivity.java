package com.example.mlseriesdemonstrator.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.security.NoSuchAlgorithmException;

public class ConfirmActivity extends AppCompatActivity {

  Context context;
  Button confirm;
  Button cancel;
  private User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_confirm);

    // Set user details.
    user = Utility.getUser();

    // Assign screen context.
    context = ConfirmActivity.this;

    // Make buttons interactive.
    confirm = findViewById(R.id.CONFIRM);
    cancel = findViewById(R.id.CANCEL);

    // Once this button has been clicked, perform action.
    confirm.setOnClickListener(v -> {
      try {
        confirmed();
      } catch (NoSuchAlgorithmException ignored) {

      }

      startActivity(
              new Intent(
                      context,
                      SplashScreenActivity.class
              )
      );

      finish();
    });

    cancel.setOnClickListener(v -> finish());
  }

  private void confirmed() throws NoSuchAlgorithmException {

    // Receive the value of the intent extra with a key "mode".
    String mode = getIntent().getStringExtra("mode");

    // If mode is edit_name, edit the details of the user.
    // Else if the mode is change_password, change the password
    assert mode != null;
    if (mode.equals("edit_name"))
      editNameConfirmed();
    else if (mode.equals("change_password"))
      changePasswordConfirmed();
  }

  private void editNameConfirmed() {
    // Get the input from the user
    String firstName = getIntent().getStringExtra("first_name");
    String middleName = getIntent().getStringExtra("middle_name");
    String lastName = getIntent().getStringExtra("last_name");

    // Remove the intent extras
    getIntent().removeExtra("first_name");
    getIntent().removeExtra("middle_name");
    getIntent().removeExtra("last_name");
    getIntent().removeExtra("mode");

    // Change the details of the user
    user.setFirstName(firstName);
    user.setMiddleName(middleName);
    user.setLastName(lastName);

    // Change the document reference of the user
    DocumentReference documentReference = Utility.getUserRef().document(user.getUID());

    // Change the user details in the fire store database
    documentReference.set(user).addOnCompleteListener(
            task -> Utility.showToast(context, "Details updated!")
    ).addOnFailureListener(e -> Utility.showToast(context, e.getLocalizedMessage()));
  }

  private void changePasswordConfirmed() {

    // Get the data from the previous screen
    String newPassword = getIntent().getStringExtra("new_password");
    String oldPassword = getIntent().getStringExtra("old_password");
    // Remove the move intent extra
    getIntent().removeExtra("mode");

    // Set the new password of the user
    user.setPasswordHashCode(oldPassword, newPassword, context);

    CollectionReference userRef = Utility.getUserRef();

    userRef.document(user.getUID()).set(user);
  }

}