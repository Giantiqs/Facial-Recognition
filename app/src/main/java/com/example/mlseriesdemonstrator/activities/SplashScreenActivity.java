package com.example.mlseriesdemonstrator.activities;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

  private static final String TAG = "SplashScreenActivity";
  Context context;
  ProgressBar horizontalProgressBar;
  TextView loadingText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash_screen);

    startLoad();

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
    }, 2000);
  }

  public void startLoad() {

    // This is responsible for the progress bar animation
    loadingText = findViewById(R.id.LOADING_TEXT);
    horizontalProgressBar = findViewById(R.id.PROGRESS_BAR_HORIZONTAL);

    ValueAnimator animator = ValueAnimator.ofInt(0, 100);
    animator.setDuration(2000);
    animator.addUpdateListener(animation -> {
      int progress = (int) animation.getAnimatedValue();
      horizontalProgressBar.setProgress(progress);
    });
    animator.start();
  }

}