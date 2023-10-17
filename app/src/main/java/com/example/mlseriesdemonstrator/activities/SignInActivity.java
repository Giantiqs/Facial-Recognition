package com.example.mlseriesdemonstrator.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.utilities.AccountManager;
import com.example.mlseriesdemonstrator.utilities.AppContext;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.auth.FirebaseAuth;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

  /*
  * @todo
  *  ADD A PROGRESS BAR THAT WILL BE VISIBLE IF THE SIGN IN WAS SUCCESSFUL
  *
   */

  private static final String TAG = "SignInActivity";
  final String mode = "login";
  Context context;
  EditText inputTxt;
  EditText passwordTxt;
  TextView forgotPasswordTxt;
  Button signInBtn;
  ProgressBar progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_in);
    Log.d(TAG, "hi po");

    // Make the buttons interactive
    inputTxt = findViewById(R.id.INPUT_TXT);
    passwordTxt = findViewById(R.id.PASSWORD_TXT);
    forgotPasswordTxt = findViewById(R.id.FORGOT_PASSWORD_TXT);
    signInBtn = findViewById(R.id.SIGN_IN_BTN);
    progressBar = findViewById(R.id.SIGN_IN_PROGRESS_BAR);

    // Set the content of the screen
    context = SignInActivity.this;

    progressBar.setVisibility(View.GONE);

    // When the button is pressed, perform action
    signInBtn.setOnClickListener(v -> {
      try {
        loginAccount();
      } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
      }
    });

    forgotPasswordTxt.setOnClickListener(v -> startActivity(new Intent(context, ForgotPasswordActivity.class)));
  }

  private boolean isEmail(String input) {
    return Patterns.EMAIL_ADDRESS.matcher(input).matches();
  }

  private boolean validateData(String email) {
    if (email == null) {
      inputTxt.setError("Email is empty.");
      return false;
    }

    // Check if the email input is correct
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      inputTxt.setError("Email is invalid.");
      return false;
    }

    return true;
  }

  private void loginAccountFirebase(String email, String password) {

    // Check if user exists and if not, display errors in the screen below
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
              if (task.isSuccessful()) {
                if (Objects.requireNonNull(firebaseAuth.getCurrentUser()).isEmailVerified()) {
                  Intent intent = new Intent(context, LoadingActivity.class);

                  intent.putExtra("password", password);

                  Context selectionContext = AppContext.getContext();

                  if (selectionContext instanceof Activity) {
                    ((Activity) selectionContext).finish();
                  }

                  progressBar.setVisibility(View.VISIBLE);

                  startActivity(intent);
                  finish();
                } else {
                  Utility.showToast(context, "Email is not verified.");
                }
              } else {
                Utility.showToast(context, Objects.requireNonNull(task
                                .getException())
                        .getLocalizedMessage()
                );
              }
            });
  }

  private void loginAccount() throws NoSuchAlgorithmException {
    // Get texts from the user input
    String password = passwordTxt.getText().toString();
    String input = inputTxt.getText().toString();

    if (input.isEmpty() && password.isEmpty()) {
      inputTxt.setError("IE or ID is required.");
      passwordTxt.setError("Password is required.");
      return;
    }

    if (input.isEmpty()) {
      inputTxt.setError("IE or ID is required.");
      return;
    }

    if (password.isEmpty()) {
      passwordTxt.setError("Password is required.");
      return;
    }

    if (!isEmail(input)) {
      retrieveEmail(input, email -> {
        if (email != null) {
          loginAccountFirebase(email, password);
        } else {
          Utility.showToast(context, "No email found for this id.");
        }
      });
    } else {
      // The input is already an email, proceed with login
      loginAccountFirebase(input, password);
    }
  }

  private void retrieveEmail(String input, final EmailCallback callback) {
    retrieveStudentAndEmployee(input, callback);
  }

  private void retrieveStudentAndEmployee(String input, final EmailCallback callback) {
    AccountManager.getStudentById(input, context, mode, student -> {
      if (student != null) {
        callback.onEmailRetrieved(student.getInstitutionalEmail());
      } else {
        retrieveEmployee(input, callback);
      }
    });
  }

  private void retrieveEmployee(String input, final EmailCallback callback) {
    AccountManager.getEmployeeById(input, context, mode, employee -> {
      if (employee != null) {
        callback.onEmailRetrieved(employee.getInstitutionalEmail());
      } else {
        callback.onEmailRetrieved(null);
      }
    });
  }

  interface EmailCallback {
    void onEmailRetrieved(String email);
  }
}