package com.example.mlseriesdemonstrator.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.student.ActivateStudentAccountActivity;
import com.example.mlseriesdemonstrator.utilities.AccountManager;
import com.example.mlseriesdemonstrator.utilities.AppContext;

import java.util.Objects;

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

    LayoutInflater inflater = SelectionScreenActivity.this.getLayoutInflater();
    View eulaDialogView = inflater.inflate(R.layout.eula_dialog, null);

    AlertDialog.Builder builder = new AlertDialog.Builder(SelectionScreenActivity.this);
    builder.setView(eulaDialogView);

    builder.setPositiveButton(Html.fromHtml("<font color='#8ab4f8'>Agree</font>"), (dialog, id) -> dialog.dismiss());
    builder.setNegativeButton(Html.fromHtml("<font color='#8ab4f8'>Disagree</font>"), (dialog, id) -> finish());

    AlertDialog dialog = builder.create();
    Objects.requireNonNull(
                    dialog.getWindow())
            .setBackgroundDrawable(
                    new ColorDrawable(Color.parseColor("#162c46")
                    )
            );

    dialog.show();
  }

}