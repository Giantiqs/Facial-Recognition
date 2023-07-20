package com.example.mlseriesdemonstrator.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.mlseriesdemonstrator.R;

public class EditNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);

        boolean done = true;

        if (done) {
            finish();
        }
    }
}