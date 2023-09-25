package com.example.mlseriesdemonstrator.activities;

import android.content.Context;
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

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    Context context;
    EditText emailTxt;
    EditText passwordTxt;
    TextView forgotPasswordTxt;
    Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        context = SignInActivity.this;
        emailTxt = findViewById(R.id.EMAIL_TXT);
        passwordTxt = findViewById(R.id.PASSWORD_TXT);
        forgotPasswordTxt = findViewById(R.id.FORGOT_PASSWORD_TXT);
        signInBtn = findViewById(R.id.SIGN_IN_BTN);

        signInBtn.setOnClickListener(v -> {
            try {
                loginAccount();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void loginAccount() throws NoSuchAlgorithmException {

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
                           startActivity(new Intent(context, LoadingActivity.class));
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

    private boolean validateData(String email, String password) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTxt.setError("Email is invalid.");
            return false;
        }

        return true;
    }
}