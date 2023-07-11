package com.example.mlseriesdemonstrator.utilities;

import android.content.Context;
import android.util.Log;
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

    public static void setUserDetails() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        DocumentReference userDocumentRef = db.collection("users")
                .document(firebaseUser.getUid());

        userDocumentRef.collection("user_details").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        user = queryDocumentSnapshots
                                .getDocuments()
                                .get(0)
                                .toObject(User.class);

                        if (user != null) {
                            Log.d("User Details", user.getFirstName() + " " + user.getLastName());
                        }
                    } else {
                        Log.d("User Details", "No document found");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Error", "Error getting documents", e);
                    // Handle error case here
                });
    }

    public static User getUser() {
        return user;
    }

}
