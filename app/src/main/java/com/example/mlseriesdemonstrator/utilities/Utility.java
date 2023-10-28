package com.example.mlseriesdemonstrator.utilities;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.example.mlseriesdemonstrator.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utility {

  static User user;
  static LoadingCompleteListener loadingCompleteListener;

  public interface LoadingCompleteListener {
    void onLoadingComplete(User user);
  }

  public static void showToast(Context context, String message) {
    // Shows a message below the screen
    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
  }

  public static CollectionReference getUserRef() {

    // Returns the collection reference of the user
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    assert currentUser != null;
    return FirebaseFirestore.getInstance()
            .collection("users")
            .document(currentUser.getUid())
            .collection("user_details");
  }

  public static void setUserDetails(LoadingCompleteListener listener) {

    // Wait for the data from the firebase

    loadingCompleteListener = listener;

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
                  loadingCompleteListener.onLoadingComplete(user);
                }
              } else {
                Log.d("User Details", "No document found");
              }
            })
            .addOnFailureListener(e -> Log.e(
                    "Error",
                    "Error getting documents", e)
            );
  }

  public static User getUser() {
    return user;
  }

  public static String hashString(String data) throws NoSuchAlgorithmException {

    // This is for returning the hash code of the password
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] hashBytes = md.digest(data.getBytes());

    // Convert the byte array to a hexadecimal string
    StringBuilder hexString = new StringBuilder();
    for (byte b : hashBytes) {
      String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) hexString.append('0');
      hexString.append(hex);
    }

    return hexString.toString();
  }

  public static boolean verifyHash(String candidateData, String originalHash) throws NoSuchAlgorithmException {

    // Check if the hash of two data is the same

    String candidateHash = hashString(candidateData);

    return originalHash.equals(candidateHash);
  }

  public static void addUserIds(String userId) {

    CollectionReference reference = FirebaseFirestore.getInstance().collection("user_id");

    Pair<String, String> pair = new Pair<>(userId, userId);

    reference.document(userId).set(pair);
  }

  public static void addEventIds(String eventId) {

    CollectionReference reference = FirebaseFirestore.getInstance().collection("event_id");

    Pair<String, String> pair = new Pair<>(eventId, eventId);

    reference.document(eventId).set(pair);
  }

}
