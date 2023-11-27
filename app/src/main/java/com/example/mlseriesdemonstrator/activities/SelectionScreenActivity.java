package com.example.mlseriesdemonstrator.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.student.ActivateStudentAccountActivity;
import com.example.mlseriesdemonstrator.utilities.AccountManager;
import com.example.mlseriesdemonstrator.utilities.AppContext;

public class SelectionScreenActivity extends AppCompatActivity {

  Context context;
  Button signIn;
  Button signUp;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_selection_screen);

    showEULADialog();

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

  private void showEULADialog() {


    AlertDialog.Builder builder = new AlertDialog.Builder(SelectionScreenActivity.this);
    builder.setTitle("End-User License Agreement (EULA)");
    builder.setPositiveButton("Agree", (dialog, id) -> dialog.dismiss());
    builder.setNegativeButton("Disagree", (dialog, id) -> finish());
    builder.create().show();
  }
}