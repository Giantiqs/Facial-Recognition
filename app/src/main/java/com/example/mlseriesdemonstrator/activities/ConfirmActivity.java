package com.example.mlseriesdemonstrator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.mlseriesdemonstrator.MainActivity;
import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        Button confirm = findViewById(R.id.CONFIRM);
        Button cancel = findViewById(R.id.CANCEL);

        confirm.setOnClickListener(v -> {
            confirmed();

            startActivity(
                    new Intent(
                            ConfirmActivity.this,
                            LoadingActivity.class
                    )
            );

            finish();
        });

        cancel.setOnClickListener(v -> {

        });
    }

    private void confirmed() {
        String firstName = getIntent().getStringExtra("first_name");
        String middleName = getIntent().getStringExtra("middle_name");
        String lastName = getIntent().getStringExtra("last_name");

        assert firstName != null;
        assert middleName != null;
        assert lastName != null;

        Log.d("FIRST NAME:", !firstName.isEmpty() ? firstName : "l bozo");
        Log.d("MIDDLE NAME:", !middleName.isEmpty() ? middleName : "l bozo");
        Log.d("LAST NAME:", !lastName.isEmpty() ? lastName : "l bozo");

        getIntent().removeExtra("first_name");
        getIntent().removeExtra("middle_name");
        getIntent().removeExtra("last_name");

        User user = Utility.getUser();

        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);

        DocumentReference documentReference = Utility.getUserRef().document();

        documentReference.set(user).addOnCompleteListener(task -> {

        });
    }

}