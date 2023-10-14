package com.example.mlseriesdemonstrator.fragments.student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.facial_recognition.FaceRecognitionActivity;

public class AttendanceFragment extends Fragment {

  private static final String TAG = "AttendanceFragment";
  ConstraintLayout getStartedBtn;
  Context context;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_attendance, container, false);

    context = getActivity();
    getStartedBtn = view.findViewById(R.id.GET_STARTED);

    getStartedBtn.setOnClickListener(v -> {

      Intent intent = new Intent(context, FaceRecognitionActivity.class);
      String attendance = "attendance";

      intent.putExtra("mode", attendance);

      startActivity(intent);
    });

    return view;
  }
}