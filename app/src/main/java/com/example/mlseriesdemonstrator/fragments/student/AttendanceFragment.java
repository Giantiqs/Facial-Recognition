package com.example.mlseriesdemonstrator.fragments.student;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.facial_recognition.FaceRecognitionActivity;

public class AttendanceFragment extends Fragment {

  ConstraintLayout getStartedBtn;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_attendance, container, false);

    getStartedBtn = view.findViewById(R.id.GET_STARTED);

    getStartedBtn.setOnClickListener(v ->
            startActivity(new Intent(getActivity(), FaceRecognitionActivity.class))
    );

    return view;
  }
}