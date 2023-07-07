package com.example.mlseriesdemonstrator.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mlseriesdemonstrator.R;

public class AttendanceFragment extends Fragment {

    Button getStartedBtn;

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

        getStartedBtn.setOnClickListener(v -> {});

        return view;
    }
}