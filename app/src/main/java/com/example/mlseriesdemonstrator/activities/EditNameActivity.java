package com.example.mlseriesdemonstrator.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.mlseriesdemonstrator.R;

public class EditNameActivity extends AppCompatActivity {

    Button editDetailsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);

        editDetailsBtn = findViewById(R.id.EDIT_DETAILS_BTN);

        editDetailsBtn.setOnClickListener(v -> editDone());
    }

    private void editDone() {
        finish();
    }
}