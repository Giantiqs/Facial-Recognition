package com.example.mlseriesdemonstrator.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.classes.User;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AccountFragment extends Fragment {

    TextView fullName;
    TextView course;
    TextView totalAttendance;
    TextView earlyAttendance;
    Button editName;
    Button resetPassword;
    Button updateFace;
    User user;

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
        if (user != null) {
            String fullNameStr = user.getFirstName() + " " + user.getLastName();

            fullName.setText(fullNameStr);
            course.setText(user.getCourse());
            totalAttendance.setText("");
            earlyAttendance.setText("");
        }
    }

    public void getUserDetails(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection("users").document(uid);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    // Handle the retrieved user object here
                } else {
                    // Document does not exist
                }
            }
        });
    }
}