package com.example.mlseriesdemonstrator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {

  private static final String TAG = "ForgotPasswordActivity";
  EditText emailEditTxt;
  Button sendEmailBtn;
  FirebaseAuth auth;
  Context context;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_forgot_password);

    emailEditTxt = findViewById(R.id.EMAIL_EDIT_TXT);
    sendEmailBtn = findViewById(R.id.SEND_EMAIL_BTN);
    context = ForgotPasswordActivity.this;

    auth = FirebaseAuth.getInstance();

    sendEmailBtn.setOnClickListener(v -> {
      String emailStr = emailEditTxt.getText().toString();

      if (TextUtils.isEmpty(emailStr)) {
        resetPassword(emailStr);
      } else {
        emailEditTxt.setError("Enter your email");
      }
    });
  }

  private void resetPassword(String emailStr) {
    auth.sendPasswordResetEmail(emailStr)
            .addOnSuccessListener(unused -> {
              Utility.showToast(context, "Reset your password by checking your email.");
              finish();
            })
            .addOnFailureListener(e -> Log.d(TAG, Objects.requireNonNull(e.getLocalizedMessage())));
  }
}