package com.example.mlseriesdemonstrator.fragments.student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.ChangePasswordActivity;
import com.example.mlseriesdemonstrator.activities.EditNameActivity;
import com.example.mlseriesdemonstrator.activities.SplashScreenActivity;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class AccountFragment extends Fragment {

    TextView fullNameTxt;
    TextView courseTxt;
    TextView totalAttendanceTxt;
    TextView earlyAttendanceTxt;
    Button resetPasswordBtn;
    Button updateFaceBtn;
    Button logout;
    User user;
    Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Make the elements interactive
        fullNameTxt = view.findViewById(R.id.FULL_NAME);
        courseTxt = view.findViewById(R.id.COURSE);
        totalAttendanceTxt = view.findViewById(R.id.TOTAL_ATTENDANCE);
        earlyAttendanceTxt = view.findViewById(R.id.TOTAL_EARLY_ATTENDANCE);
        resetPasswordBtn = view.findViewById(R.id.RESET_PASSWORD);
        updateFaceBtn = view.findViewById(R.id.UPDATE_FACE);
        logout = view.findViewById(R.id.LOGOUT);

        context = getActivity();

        // Display the details of the student in the screen
        setTexts();

        resetPasswordBtn.setOnClickListener(v -> {
            startActivity(new Intent(context, ChangePasswordActivity.class));
        });

        updateFaceBtn.setOnClickListener(v -> {

        });

        logout.setOnClickListener(v -> {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                firebaseAuth.signOut();

                startActivity(
                        new Intent(
                                getActivity(),
                                SplashScreenActivity.class
                        )
                );

                requireActivity().finish();
        });

        return view;
    }

    private void setTexts() {

        user = Utility.getUser();

        String fullName = user.getFirstName() + " " + user.getLastName();

        fullNameTxt.setText(fullName);
        courseTxt.setText(user.getCourse());
    }


}