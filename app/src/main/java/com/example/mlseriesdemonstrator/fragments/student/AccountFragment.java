package com.example.mlseriesdemonstrator.fragments.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.Utility;

public class AccountFragment extends Fragment {

    TextView fullNameTxt;
    TextView courseTxt;
    TextView totalAttendanceTxt;
    TextView earlyAttendanceTxt;
    Button editNameBtn;
    Button resetPasswordBtn;
    Button updateFaceBtn;
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

        fullNameTxt = view.findViewById(R.id.FULL_NAME);
        courseTxt = view.findViewById(R.id.COURSE);
        totalAttendanceTxt = view.findViewById(R.id.TOTAL_ATTENDANCE);
        earlyAttendanceTxt = view.findViewById(R.id.TOTAL_EARLY_ATTENDANCE);
        editNameBtn = view.findViewById(R.id.EDIT_NAME);
        resetPasswordBtn = view.findViewById(R.id.RESET_PASSWORD);
        updateFaceBtn = view.findViewById(R.id.UPDATE_FACE);

        setTexts();

        editNameBtn.setOnClickListener(v -> {

        });

        resetPasswordBtn.setOnClickListener(v -> {

        });

        updateFaceBtn.setOnClickListener(v -> {

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