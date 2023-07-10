package com.example.mlseriesdemonstrator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mlseriesdemonstrator.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    ProgressBar horizontalProgressBar;
    TextView loadingText;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        startLoad();

        new Handler().postDelayed(() -> {

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            if (firebaseUser == null) {
                startActivity(new Intent(
                        SplashScreenActivity.this,
                        SelectionScreenActivity.class)
                );
            } else {
                startActivity(new Intent(
                        SplashScreenActivity.this,
                        MainActivity.class)
                );
            }

            finish();
        }, 5000);
    }

    public void startLoad() {
        loadingText = findViewById(R.id.LOADING_TEXT);
        horizontalProgressBar = findViewById(R.id.PROGRESS_BAR_HORIZONTAL);

        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(5000);
        animator.addUpdateListener(animation -> {
            int progress = (int) animation.getAnimatedValue();
            horizontalProgressBar.setProgress(progress);
        });
        animator.start();
    }

}