package com.example.mlseriesdemonstrator.activities.student;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.mlseriesdemonstrator.MainActivity;
import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.Attendance;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.Utility;

import java.util.concurrent.Executor;

public class FingerPrintAuthActivity extends AppCompatActivity {

  private final int REQUEST_CODE = 101011;
  private static final String TAG = "FingerPrintAuthActivity";
  private User user;
  private Executor executor;
  private BiometricPrompt biometricPrompt;
  private BiometricPrompt.PromptInfo promptInfo;
  ImageView fpIconBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_finger_print_auth);

    user = Utility.getUser();

    String fullName = String.format("%s %s", user.getFirstName(), user.getLastName());
    String eventId = getIntent().getStringExtra("event_id");
    String institutionalEmail = user.getInstitutionalEmail();

    BiometricManager biometricManager = BiometricManager.from(this);
    switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
      case BiometricManager.BIOMETRIC_SUCCESS:
        Log.d(TAG, "App can authenticate using biometrics.");
        break;
      case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
        Log.e(TAG, "No biometric features available on this device.");
        break;
      case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
        Log.e(TAG, "Biometric features are currently unavailable.");
        break;
      case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
        // Prompts the user to create credentials that your app accepts.
        final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
        enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
        startActivityForResult(enrollIntent, REQUEST_CODE);
        break;
    }

    executor = ContextCompat.getMainExecutor(this);
    biometricPrompt = new BiometricPrompt(FingerPrintAuthActivity.this,
            executor, new BiometricPrompt.AuthenticationCallback() {
      @Override
      public void onAuthenticationError(int errorCode,
                                        @NonNull CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                .show();
      }

      @Override
      public void onAuthenticationSucceeded(
              @NonNull BiometricPrompt.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        Toast.makeText(getApplicationContext(),
                "Authentication succeeded!", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                .show();
      }
    });

    promptInfo = new BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build();

    fpIconBtn = findViewById(R.id.FP_BTN);
    fpIconBtn.setOnClickListener(view -> biometricPrompt.authenticate(promptInfo));

    Attendance attendance = new Attendance(institutionalEmail, fullName, eventId);
  }
}