package com.example.mlseriesdemonstrator.fragments.host;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.EditNameActivity;
import com.example.mlseriesdemonstrator.activities.SplashScreenActivity;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountFragment extends Fragment {

    TextView fullNameTxt;
    TextView roleTxt;
    TextView eventCount;
    Button editHostName;
    Button resetPassword;
    Button logout;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.fragment_account2, container, false);

        user = Utility.getUser();

        // Make elements interactive in the screen
        fullNameTxt = view.findViewById(R.id.HOST_FULL_NAME);
        roleTxt = view.findViewById(R.id.ROLE);
        eventCount = view.findViewById(R.id.EVENT_COUNT);
        editHostName = view.findViewById(R.id.HOST_EDIT_NAME);
        resetPassword = view.findViewById(R.id.HOST_RESET_PASSWORD);
        logout = view.findViewById(R.id.LOGOUT);

        // Set the details of the host
        setTexts();

        editHostName.setOnClickListener(v -> startActivity(
                new Intent(getActivity(), EditNameActivity.class)
        ));

        resetPassword.setOnClickListener(v -> {

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

        String fullName = user.getFirstName() + " " + user.getLastName();

        fullNameTxt.setText(fullName);
    }

}