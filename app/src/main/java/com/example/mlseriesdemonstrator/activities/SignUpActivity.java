package com.example.mlseriesdemonstrator.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private Context context;
    private EditText lastNameTxt;
    private EditText firstNameTxt;
    private EditText middleNameTxt;
    private EditText passwordTxt;
    private EditText confirmPasswordTxt;
    private EditText emailTxt;
    private EditText studentIDTxt;
    private EditText courseTxt;
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context = SignUpActivity.this;
        lastNameTxt = findViewById(R.id.LAST_NAME_TXT);
        firstNameTxt = findViewById(R.id.FIRST_NAME_TXT);
        middleNameTxt = findViewById(R.id.MIDDLE_NAME_TXT);
        emailTxt = findViewById(R.id.EMAIL_TXT_UP);
        studentIDTxt = findViewById(R.id.STUDENT_ID);
        courseTxt = findViewById(R.id.COURSE);
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
        String email = emailTxt.getText().toString();

        boolean isValidated = validateData(email, password, confirmPassword);

        if (!isValidated) return;

        createAccountFirebase(email, password);
    }

    private void createAccountFirebase(String email, String password) {

        String lastName = lastNameTxt.getText().toString();
        String firstName = firstNameTxt.getText().toString();
        String middleName = middleNameTxt.getText().toString();
        String studentID = studentIDTxt.getText().toString();
        String course = courseTxt.getText().toString();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, task -> {
                   if (task.isSuccessful()) {

                       String uid = firebaseAuth.getUid();

                       User user = null;

                       try {
                           user = new User(
                                   lastName,
                                   firstName,
                                   middleName,
                                   "student",
                                   studentID,
                                   course,
                                   uid,
                                   password
                           );
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

        documentReference.set(user).addOnCompleteListener(task ->
            Utility.showToast(context, "Please check your email for verification")
        ).addOnFailureListener(e -> Utility.showToast(context, e.getLocalizedMessage()));
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

    private void getDetailsUsingStudentID() {
        /*
            Create a collection in firestore that will contain

            use the student id to access the data from the database and set it to the user

            add the user to the users document after setting and if no data is found against that -
            - student id, show a message that this id is invalid


         */
    }
}