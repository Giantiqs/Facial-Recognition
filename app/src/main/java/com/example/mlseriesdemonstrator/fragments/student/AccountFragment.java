package com.example.mlseriesdemonstrator.fragments.student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.ChangePasswordActivity;
import com.example.mlseriesdemonstrator.activities.SplashScreenActivity;
import com.example.mlseriesdemonstrator.facial_recognition.FaceRecognitionActivity;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.EventManager;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class AccountFragment extends Fragment {

  private static final String TAG = "AccountFragment";
  TextView fullNameTxt;
  TextView courseTxt;
  TextView totalAttendanceTxt;
  Button resetPasswordBtn;
  Button updateFaceBtn;
  Button logout;
  User user;
  Context context;
  ArrayList<String> eventIds = new ArrayList<>();
  TextView nameAcronymTxt;
  int totalAttendanceCount = 0;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_account, container, false);

    // Make the elements interactive
    fullNameTxt = view.findViewById(R.id.FULL_NAME);
    courseTxt = view.findViewById(R.id.COURSE);
    totalAttendanceTxt = view.findViewById(R.id.TOTAL_ATTENDANCE);
    resetPasswordBtn = view.findViewById(R.id.RESET_PASSWORD);
    updateFaceBtn = view.findViewById(R.id.UPDATE_FACE);
    logout = view.findViewById(R.id.LOGOUT);
    nameAcronymTxt = view.findViewById(R.id.NAME_ACR);

    initEventIds();

    context = getActivity();

    // Display the details of the student on the screen
    setTexts();

    EventManager.getStartedEvents(context, user, events -> {
      if (events.size() == 0) {
        updateFaceBtn.setVisibility(View.VISIBLE);
      }
    });

    resetPasswordBtn.setOnClickListener(v ->
            startActivity(new Intent(context, ChangePasswordActivity.class))
    );

    updateFaceBtn.setOnClickListener(v -> {
      Intent intent = new Intent(context, FaceRecognitionActivity.class);

      intent.putExtra("mode", "update face");

      startActivity(intent);
    });

    logout.setOnClickListener(v -> {
      FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

      firebaseAuth.signOut();

      startActivity(new Intent(getActivity(), SplashScreenActivity.class));

      requireActivity().finish();
    });

    return view;
  }

  private void setTexts() {
    user = Utility.getUser();
    String fullName = user.getFirstName() + " " + user.getLastName();
    fullNameTxt.setText(fullName);
    courseTxt.setText(user.getCourse());

    String nameAcr = String.valueOf(user.getFirstName().charAt(0)) + user.getLastName().charAt(0);

    nameAcronymTxt.setText(nameAcr);
  }

  public void getAttendanceCount(ArrayList<String> eventIds) {

    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    CollectionReference attendanceCollectionRef = fireStore.collection("attendance");
    String userInstitutionalID = user.getInstitutionalID();

    // Initialize the total attendance count

    for (String id : eventIds) {
      attendanceCollectionRef
              .document(id)
              .collection(userInstitutionalID)
              .get()
              .addOnSuccessListener(queryDocumentSnapshots -> {
                int attendanceCount = queryDocumentSnapshots.size();
                totalAttendanceCount += attendanceCount; // Accumulate the counts
                totalAttendanceTxt.setText(String.valueOf(totalAttendanceCount));
              })
              .addOnFailureListener(e -> Log.e(TAG, "Error fetching attendance count: " + e.getMessage()));
    }
  }


  private void initEventIds() {

    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    CollectionReference eventIdsCollectionRef = fireStore.collection("event_id");

    eventIdsCollectionRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
      eventIds.clear(); // Clear the ArrayList before adding new event IDs
      for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
        String eventId = documentSnapshot.getId();
        eventIds.add(eventId); // Add each event ID to the ArrayList
      }

      getAttendanceCount(eventIds);
    }).addOnFailureListener(e -> Log.d(TAG, Objects.requireNonNull(e.getLocalizedMessage())));
  }

}
