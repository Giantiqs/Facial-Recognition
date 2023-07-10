package com.example.mlseriesdemonstrator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    EditText emailTxt;
    EditText passwordTxt;
    TextView forgotPasswordTxt;
    Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        emailTxt = findViewById(R.id.EMAIL_TXT);
        passwordTxt = findViewById(R.id.PASSWORD_TXT);
        forgotPasswordTxt = findViewById(R.id.FORGOT_PASSWORD_TXT);
        signInBtn = findViewById(R.id.SIGN_IN_BTN);

        signInBtn.setOnClickListener(v -> loginAccount());
    }

    private void loginAccount() {

        String password = passwordTxt.getText().toString();
        String email = emailTxt.getText().toString();

        boolean isValidated = validateData(email, password);

        if (!isValidated) return;

        loginAccountFirebase(email, password);
    }

    private void loginAccountFirebase(String email, String password) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       if (Objects.requireNonNull(firebaseAuth.getCurrentUser()).isEmailVerified()) {
                           // go to student/ host screen
                           // for now use student

                           startActivity(new Intent(
                                   SignInActivity.this,
                                   MainActivity.class
                                   )
                           );
                           finish();
                       } else {
                           Utility.showToast(
                                   SignInActivity.this,
                                   "Email is not verified."
                           );
                       }
                   } else {
                       // ignore for now
                   }
                });
    }

    private boolean validateData(String email, String password) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTxt.setError("Email is invalid.");
            return false;
        }

        return true;
    }
}