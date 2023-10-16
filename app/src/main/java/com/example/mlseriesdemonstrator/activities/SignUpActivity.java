package com.example.mlseriesdemonstrator.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.AccountManager;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

  private static final String TAG = "SignUpActivity";
  private Context context;
  final String studentMode = "student";
  final String hostMode = "host";
  private EditText passwordTxt;
  private EditText confirmPasswordTxt;
  Button signUpBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);
    context = SignUpActivity.this;

    passwordTxt = findViewById(R.id.PASSWORD_TXT_UP);
    confirmPasswordTxt = findViewById(R.id.CONFIRM_PASSWORD_TXT);
    signUpBtn = findViewById(R.id.SIGN_UP_BTN);

    signUpBtn.setOnClickListener(v -> {
      try {
        createAccount();
      } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
      }
    });
  }

  private void createAccount() throws NoSuchAlgorithmException {

    String password = passwordTxt.getText().toString();
    String confirmPassword = confirmPasswordTxt.getText().toString();
    String email = getIntent().getStringExtra("email");

    boolean isValidated = validateData(email, password, confirmPassword);

    if (!isValidated) return;

    createAccountFirebase(email, password);
  }

  private void createAccountFirebase(String email, String password) {

    String lastName = getIntent().getStringExtra("last_name");
    String firstName = getIntent().getStringExtra("first_name");
    String middleName = getIntent().getStringExtra("middle_name");
    String department = getIntent().getStringExtra("department");
    String course = getIntent().getStringExtra("course");

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(SignUpActivity.this, task -> {
              if (task.isSuccessful()) {

                String uid = firebaseAuth.getUid();

                String role = (
                        Objects.equals(
                                getIntent().getStringExtra("mode"),
                                studentMode
                        ) ? "student" : "host"
                );

                User user = null;

                try {
                  if (role.equals("student")) {
                    String studentID = getIntent().getStringExtra("student_id");

                    user = new User(
                            lastName,
                            firstName,
                            middleName,
                            "student",
                            studentID,
                            department,
                            course,
                            uid,
                            password
                    );
                  } else {
                    String employeeID = getIntent().getStringExtra("employee_id");

                    user = new User(
                            lastName,
                            firstName,
                            middleName,
                            "host",
                            employeeID,
                            "N/A",
                            course,
                            uid,
                            password
                    );
                  }

                } catch (NoSuchAlgorithmException e) {
                  Utility.showToast(context, e.getLocalizedMessage());
                }

                saveAccountDetails(user);

                Utility.showToast(context, "Account Created");

                Objects.requireNonNull(firebaseAuth.getCurrentUser())
                        .sendEmailVerification();

                firebaseAuth.signOut();
                finish();
              } else {
                Utility.showToast(context, Objects.requireNonNull(task
                                .getException())
                        .getLocalizedMessage()
                );
              }
            });
  }

  private void saveAccountDetails(User user) {
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    assert firebaseUser != null;
    DocumentReference documentReference = Utility.getUserRef().document(firebaseUser.getUid());

    documentReference.set(user)
            .addOnCompleteListener(task -> {
              if (task.isSuccessful()) {
                if ("student".equals(user.getRole())) {
                  String studentID = getIntent().getStringExtra("student_id");
                  AccountManager.activateStudent(studentID);
                } else if ("host".equals(user.getRole())) {
                  String employeeID = getIntent().getStringExtra("employee_id");
                  AccountManager.activateEmployee(employeeID);
                }
              } else {
                Utility.showToast(context, "Failed to save user details");
              }
            })
            .addOnFailureListener(e -> Utility.showToast(context, e.getLocalizedMessage()));
  }


  private boolean validateData(String email, String password, String confirmPassword) {
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      Utility.showToast(context, "Email error");
      return false;
    }

    if (password.length() < 8) {
      passwordTxt.setError("Password length must be greater than 8");
      return false;
    }

    if (!password.equals(confirmPassword)) {
      confirmPasswordTxt.setError("Passwords do not match");
      return false;
    }

    return true;
  }

}