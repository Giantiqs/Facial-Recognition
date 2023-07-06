package com.example.mlseriesdemonstrator.activities;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.classes.User;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    EditText lastNameTxt, firstNameTxt, middleNameTxt, passwordTxt, confirmPasswordTxt, emailTxt;
    Button signUpBtn;

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
        signUpBtn = findViewById(R.id.SIGN_UP_BTN);

        signUpBtn.setOnClickListener(v -> createAccount());
    }

    private void createAccount() {

        String lastName = lastNameTxt.getText().toString();
        String firstName = firstNameTxt.getText().toString();
        String middleName = middleNameTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        String confirmPassword = confirmPasswordTxt.getText().toString();
        String email = emailTxt.getText().toString();

        boolean isValidated = validateData(email, password, confirmPassword);

        if (!isValidated) return;

        createAccountFirebase(email, password, lastName, firstName, middleName);
    }

    private void createAccountFirebase(String email,
                                       String password,
                                       String lastName,
                                       String firstName,
                                       String middleName) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, task -> {
                   if (task.isSuccessful()) {

                       User user = new User(lastName, firstName, middleName, "student");

                       saveAccountDetails(user);

                       Utility.showToast(
                               SignUpActivity.this,
                               "Account Created"
                       );

                       Objects.requireNonNull(firebaseAuth.getCurrentUser()).sendEmailVerification();
                       firebaseAuth.signOut();
                       finish();
                   } else {
                       Utility.showToast(
                               SignUpActivity.this,
                               Objects.requireNonNull(task.getException()).getLocalizedMessage()
                       );
                   }
                });
    }

    private void saveAccountDetails(User user) {

        DocumentReference documentReference = Utility.getUserRef().document();

        documentReference.set(user).addOnCompleteListener(task -> {

        });
    }

    private boolean validateData(String email, String password, String confirmPassword) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTxt.setError("Email is invalid.");
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