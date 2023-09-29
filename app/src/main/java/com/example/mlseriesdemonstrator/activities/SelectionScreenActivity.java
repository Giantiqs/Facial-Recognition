package com.example.mlseriesdemonstrator.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;

public class SelectionScreenActivity extends AppCompatActivity {

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

        signIn.setOnClickListener(v -> startActivity(new Intent(context, SignInActivity.class)));

        signUp.setOnClickListener(v -> startActivity(new Intent(context, SignUpActivity.class)));
    }
}