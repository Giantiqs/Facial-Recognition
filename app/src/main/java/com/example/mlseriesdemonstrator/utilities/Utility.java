package com.example.mlseriesdemonstrator.utilities;

import android.content.Context;
import android.widget.Toast;

import com.example.mlseriesdemonstrator.classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Utility {

    static User user;

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static CollectionReference getUserRef() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        return FirebaseFirestore.getInstance()
                .collection("users")
                .document(currentUser.getUid())
                .collection("user_details");
    }

    public static void getUserDetails(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection("users").document(uid);

        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                user = documentSnapshot.toObject(User.class);
                // Handle the retrieved user object here
            } else {
                // Document does not exist
            }
        });
    }

    public static User getUser() {
        return user;
    }

}
