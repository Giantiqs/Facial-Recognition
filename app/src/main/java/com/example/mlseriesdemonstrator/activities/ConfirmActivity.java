package com.example.mlseriesdemonstrator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.mlseriesdemonstrator.MainActivity;
import com.example.mlseriesdemonstrator.R;

public class ConfirmActivity extends AppCompatActivity {

    private Button confirm;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        confirm = findViewById(R.id.CONFIRM);
        cancel = findViewById(R.id.CANCEL);

        confirm.setOnClickListener(v -> {
            confirmed();
            startActivity(new Intent(ConfirmActivity.this, MainActivity.class));
            finish();
        });

        cancel.setOnClickListener(v -> {

        });
    }

    private void confirmed() {

    }
}