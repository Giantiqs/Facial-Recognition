package com.example.mlseriesdemonstrator.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.utilities.Activation;

public class ActivateAccountActivity extends AppCompatActivity {

    Context context;
    EditText studentNumberInput;
    Button idFinderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_account);
        Activation.addPeople();
        context = ActivateAccountActivity.this;
        studentNumberInput = findViewById(R.id.STUDENT_NUMBER);
        idFinderBtn = findViewById(R.id.CHECK_STUDENT);

        idFinderBtn.setOnClickListener(v -> {
            String studentIdStr = studentNumberInput.getText().toString();

            Intent intent = new Intent(context, LoadingActivity2.class);

            if (studentIdStr.isEmpty()) {
                studentNumberInput.setError("Input smth plz");
                return;
            }

            intent.putExtra("student_id", studentIdStr);

            startActivity(intent);
            finish();
        });
    }
}
