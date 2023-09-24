package com.example.mlseriesdemonstrator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.firestore.DocumentReference;

public class ConfirmActivity extends AppCompatActivity {

    Button confirm;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        confirm = findViewById(R.id.CONFIRM);
        cancel = findViewById(R.id.CANCEL);

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

        cancel.setOnClickListener(v -> finish());
    }

    private void confirmed() {
        String mode = getIntent().getStringExtra("mode");

        if (mode.equals("edit_name"))
            editNameConfirmed();
        else if (mode.equals("change_password"))
            changePasswordConfirmed();
    }

    private void editNameConfirmed() {
        String firstName = getIntent().getStringExtra("first_name");
        String middleName = getIntent().getStringExtra("middle_name");
        String lastName = getIntent().getStringExtra("last_name");

        getIntent().removeExtra("first_name");
        getIntent().removeExtra("middle_name");
        getIntent().removeExtra("last_name");
        getIntent().removeExtra("mode");

        User user = Utility.getUser();

        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);

        DocumentReference documentReference = Utility.getUserRef().document(user.getUID());

        documentReference.set(user).addOnCompleteListener(task -> {

        });
    }

    private void changePasswordConfirmed() {

    }

}