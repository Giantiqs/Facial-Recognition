package com.example.mlseriesdemonstrator.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.firestore.DocumentReference;

import java.security.NoSuchAlgorithmException;

public class ConfirmActivity extends AppCompatActivity {

    Context context;
    Button confirm;
    Button cancel;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        user = Utility.getUser();
        context = ConfirmActivity.this;
        confirm = findViewById(R.id.CONFIRM);
        cancel = findViewById(R.id.CANCEL);

        confirm.setOnClickListener(v -> {
            try {
                confirmed();
            } catch (NoSuchAlgorithmException ignored) {

            }

            startActivity(
                    new Intent(
                            context,
                            LoadingActivity.class
                    )
            );

            finish();
        });

        cancel.setOnClickListener(v -> finish());
    }

    private void confirmed() throws NoSuchAlgorithmException {

        String mode = getIntent().getStringExtra("mode");

        assert mode != null;
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

        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);

        DocumentReference documentReference = Utility.getUserRef().document(user.getUID());

        documentReference.set(user).addOnCompleteListener(
                task -> Utility.showToast(context, "Details updated!")
        ).addOnFailureListener(e -> Utility.showToast(context, e.getLocalizedMessage()));
    }

    private void changePasswordConfirmed() throws NoSuchAlgorithmException {

        String newPassword = getIntent().getStringExtra("new_password");
        String oldPassword = getIntent().getStringExtra("old_password");
        getIntent().removeExtra("mode");

        user.setPasswordHashCode(oldPassword, newPassword, context);
    }

}