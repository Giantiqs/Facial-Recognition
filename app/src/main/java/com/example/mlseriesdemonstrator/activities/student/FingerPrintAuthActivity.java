package com.example.mlseriesdemonstrator.activities.student;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mlseriesdemonstrator.MainActivity;
import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.Attendance;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.Executor;

public class FingerPrintAuthActivity extends AppCompatActivity {

  private final int REQUEST_CODE = 101011;
  private static final String TAG = "FingerPrintAuthActivity";
  private User user;
  private Executor executor;
  private BiometricPrompt biometricPrompt;
  private BiometricPrompt.PromptInfo promptInfo;
  ImageView fpIconBtn;

  @RequiresApi(api = Build.VERSION_CODES.R)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_finger_print_auth);

    user = Utility.getUser();

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
      case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
      case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
      case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
        break;
    }

    executor = ContextCompat.getMainExecutor(this);
    biometricPrompt = new BiometricPrompt(FingerPrintAuthActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
      @Override
      public void onAuthenticationError(int errorCode,
                                        @NonNull CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        Utility.showToast(getApplicationContext(), "Authentication error: " + errString);
      }

      @Override
      public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        String fullName = String.format("%s %s", user.getFirstName(), user.getLastName());
        String eventId = getIntent().getStringExtra("event_id");

        initEvent(fullName, eventId);
      }

      @Override
      public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        Utility.showToast(getApplicationContext(), "Authentication failed");
      }
    });

    promptInfo = new BiometricPrompt.PromptInfo.Builder()
            .setTitle("Verify your fingerprint")
            .setSubtitle("Register your attendance using your fingerprint")
            .setNegativeButtonText("Cancel")
            .build();

    fpIconBtn = findViewById(R.id.FP_BTN);
    fpIconBtn.setOnClickListener(view -> biometricPrompt.authenticate(promptInfo));
  }

  private void addToAttendance(String fullName, Event event) {

    String id = user.getInstitutionalID();
    String eventId = event.getEventId();
    Attendance attendance = new Attendance(user.getInstitutionalID(), fullName, eventId);
    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    CollectionReference attendanceCollectionRef = fireStore.collection("attendance");

    attendanceCollectionRef.document(eventId)
            .collection("_attendance")
            .document(id)
            .set(attendance)
            .addOnSuccessListener(documentReference -> {
              Log.d(TAG, "Added to attendance: " + fullName);
              // Handle success case here
              Utility.showToast(this, "Attendance Registered");
              Utility.currentEvent = event;

              startActivity(new Intent(this, MainActivity.class));
            })
            .addOnFailureListener(e -> {
              Log.e(TAG, "Error adding to attendance", e);
              // Handle error case here
            });
  }

  private boolean isUserInsideGeofence(LatLng userLocation, LatLng geofenceLocation, float geofenceRadius) {
    float[] results = new float[1];
    Location.distanceBetween(userLocation.latitude, userLocation.longitude, geofenceLocation.latitude, geofenceLocation.longitude, results);
    return results[0] <= geofenceRadius;
  }

  private void initEvent(String fullName, String eventId) {

    EventManager.getEventByEventId(eventId, this, events -> {
      if (!events.isEmpty()) {
        Event event = events.get(0);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

          FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

          fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
              LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

              LatLng geofenceLocation = new LatLng(
                      event.getLocation().getCustomLatLng().getLatitude(),
                      event.getLocation().getCustomLatLng().getLongitude()
              );

              float geofenceRadius = event.getLocation().getGeofenceRadius();

              if (isUserInsideGeofence(userLocation, geofenceLocation, geofenceRadius)) {
                addToAttendance(fullName, event);
              } else {
                Utility.showToast(this, "Not inside geofence");

              }
            }
          });
        } else {
          Utility.showToast(this, "Location permission not granted");
          enableUserLocation();
        }
      }
    });
  }

  private void enableUserLocation() {

    final int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

    } else {
      if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                FINE_LOCATION_ACCESS_REQUEST_CODE
        );
      } else {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                FINE_LOCATION_ACCESS_REQUEST_CODE
        );
      }
    }
  }
}