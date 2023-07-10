package com.example.mlseriesdemonstrator.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.classes.User;
import com.example.mlseriesdemonstrator.utilities.Utility;

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

        user = Utility.getUser();
        if (user != null) {
            String fullNameStr = user.getFirstName() + " " + user.getLastName();

            fullName.setText(fullNameStr);
            course.setText(user.getCourse());
            totalAttendance.setText("");
            earlyAttendance.setText("");
        }
    }

}