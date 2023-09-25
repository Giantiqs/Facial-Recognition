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
        context = SelectionScreenActivity.this;
        signIn = findViewById(R.id.SIGN_IN_BTN);
        signUp = findViewById(R.id.SIGN_UP_BTN);

        signIn.setOnClickListener(v -> startActivity(new Intent(context, SignInActivity.class)));

        signUp.setOnClickListener(v -> startActivity(new Intent(context, SignUpActivity.class)));
    }
}