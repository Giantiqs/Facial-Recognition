package com.example.mlseriesdemonstrator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.mlseriesdemonstrator.R;

public class SplashScreenActivity extends AppCompatActivity {

    ProgressBar circleProgressBar, horizontalProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        circleProgressBar = findViewById(R.id.PROGRESS_BAR_CIRCLE);
        horizontalProgressBar = findViewById(R.id.PROGRESS_BAR_HORIZONTAL);
    }
}