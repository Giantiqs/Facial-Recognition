package com.example.mlseriesdemonstrator.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.utilities.CsvBridge;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.MemoryCacheSettings;
import com.google.firebase.firestore.PersistentCacheSettings;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

  Context context;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash_screen);

    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder(
            fireStore.getFirestoreSettings())
            .setLocalCacheSettings(MemoryCacheSettings.newBuilder().build())
            .setLocalCacheSettings(PersistentCacheSettings.newBuilder()
                    .build())
            .build();

    fireStore.setFirestoreSettings(settings);

    // Set the content of the screen to this variable
    context = SplashScreenActivity.this;

    new Handler().postDelayed(() -> {

      // Get the current user
      FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

      // If the user is still logged in, proceed to loading screen
      // Else go to selection screen
      if (firebaseUser == null) {
        startActivity(new Intent(context, SelectionScreenActivity.class));
      } else {
        startActivity(new Intent(context, LoadingActivity.class));
      }

      finish();
    }, 700);
  }

}