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

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.activities.SplashScreenActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AdminAccountFragment extends Fragment {

  Button logoutBtn;
  Context context;

  public AdminAccountFragment() {
    // Required empty public constructor
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

    logoutBtn.setOnClickListener(v -> {
      FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

      firebaseAuth.signOut();

      startActivity(new Intent(context, SplashScreenActivity.class));
      ((Activity) context).finish();
    });

    return view;
  }
}