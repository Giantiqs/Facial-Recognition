package com.example.mlseriesdemonstrator.fragments.host;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mlseriesdemonstrator.R;

public class HomeFragment extends Fragment {

    ConstraintLayout card1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home2, container, false);

        card1 = view.findViewById(R.id.FIRST_CARD_HOST);

        return view;
    }
}