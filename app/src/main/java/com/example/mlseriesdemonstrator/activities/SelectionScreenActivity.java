package com.example.mlseriesdemonstrator.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.student.ActivateStudentAccountActivity;
import com.example.mlseriesdemonstrator.parcel.ParcelableContext;
import com.example.mlseriesdemonstrator.utilities.AppContext;

public class SelectionScreenActivity extends AppCompatActivity {

  private static final String TAG = "SelectionScreenActivity";
  Context context;
  Button signIn;
  Button signUp;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_selection_screen);

    // Make the buttons interactive
    signIn = findViewById(R.id.SIGN_IN_BTN);
    signUp = findViewById(R.id.SIGN_UP_BTN);

    // Set the screen content
    context = SelectionScreenActivity.this;

    // Go to the screen of which button was pressed

    signIn.setOnClickListener(v -> {
      Intent intent = new Intent(context, SignInActivity.class);

      AppContext.setContext(context);

      startActivity(intent);
    });

    signUp.setOnClickListener(v -> startActivity(new Intent(context, ActivateStudentAccountActivity.class)));
  }
}