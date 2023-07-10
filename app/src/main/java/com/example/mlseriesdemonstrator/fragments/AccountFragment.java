package com.example.mlseriesdemonstrator.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.classes.User;
import com.example.mlseriesdemonstrator.helpers.vision.recogniser.FaceRecognitionProcessor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AccountFragment extends Fragment {

    TextView fullName;
    TextView course;
    TextView totalAttendance;
    TextView earlyAttendance;
    Button editName;
    Button resetPassword;
    Button updateFace;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        fullName = view.findViewById(R.id.FULL_NAME);
        course = view.findViewById(R.id.COURSE);
        totalAttendance = view.findViewById(R.id.TOTAL_ATTENDANCE);
        earlyAttendance = view.findViewById(R.id.TOTAL_EARLY_ATTENDANCE);
        editName = view.findViewById(R.id.EDIT_NAME);
        resetPassword = view.findViewById(R.id.RESET_PASSWORD);
        updateFace = view.findViewById(R.id.UPDATE_FACE);

        setTexts();

        editName.setOnClickListener(v -> {

        });

        resetPassword.setOnClickListener(v -> {

        });

        updateFace.setOnClickListener(v -> {

        });

        return view;
    }

    private void setTexts() {

        // Move this logic to Utility.java but tmrw oke
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference userDocumentRef = firestore.collection("users")
                .document(firebaseUser.getUid());

        userDocumentRef.collection("user_details").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        User user = queryDocumentSnapshots
                                .getDocuments()
                                .get(0)
                                .toObject(User.class);

                        if (user != null) {
                            Log.d("User Details", user.getFirstName() + " " + user.getLastName());
                            String fullNameStr = user.getFirstName() + " " + user.getLastName();
                            fullName.setText(fullNameStr);
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


}