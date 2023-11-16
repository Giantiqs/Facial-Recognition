package com.example.mlseriesdemonstrator.fragments.admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.ChangePasswordActivity;
import com.example.mlseriesdemonstrator.activities.SplashScreenActivity;
import com.example.mlseriesdemonstrator.activities.admin.EmergencyAttendanceActivity;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.auth.FirebaseAuth;

public class AdminAccountFragment extends Fragment {

  Button logoutBtn;
  Button resetPasswordBtn;
  Button emergency;
  Context context;
  TextView adminFullNameTxt;
  User user;
  TextView nameAcronymTxt;

  public AdminAccountFragment() {

  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment

    View view = inflater.inflate(R.layout.fragment_admin_account, container, false);

    context = requireContext();
    logoutBtn = view.findViewById(R.id.ADMIN_LOGOUT);
    resetPasswordBtn = view.findViewById(R.id.ADMIN_RESET_PASSWORD);
    emergency = view.findViewById(R.id.EMERGENCY);
    adminFullNameTxt = view.findViewById(R.id.ADMIN_FULL_NAME);
    user = Utility.getUser();
    nameAcronymTxt = view.findViewById(R.id.NAME_ACR);

    setTexts();

    emergency.setOnClickListener(v -> {
      startActivity(new Intent(requireActivity(), EmergencyAttendanceActivity.class));
//      ((Activity)context).finish();
    });

    resetPasswordBtn.setOnClickListener(
            v -> startActivity(new Intent(context, ChangePasswordActivity.class))
    );

    logoutBtn.setOnClickListener(v -> {
      FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

      firebaseAuth.signOut();

      startActivity(new Intent(context, SplashScreenActivity.class));
      ((Activity) context).finish();
    });

    return view;
  }

  private void setTexts() {

    String fullName = user.getFirstName() + " " + user.getLastName();
    String nameAcr = String.valueOf(user.getFirstName().charAt(0)) + user.getLastName().charAt(0);

    adminFullNameTxt.setText(fullName);
    nameAcronymTxt.setText(nameAcr);
  }
}