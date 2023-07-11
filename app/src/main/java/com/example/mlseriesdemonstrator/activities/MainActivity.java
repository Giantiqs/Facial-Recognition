package com.example.mlseriesdemonstrator.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.databinding.ActivityMainBinding;
import com.example.mlseriesdemonstrator.fragments.AccountFragment;
import com.example.mlseriesdemonstrator.fragments.AttendanceFragment;
import com.example.mlseriesdemonstrator.fragments.HomeFragment;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            initUserData();
        } else {
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
            finish();
        }

        replaceFragments(new HomeFragment());

        binding.STUDENTBOTTOMNAVIGATION.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.BOTTOM_HOME:
                    replaceFragments(new HomeFragment());
                    break;
                case R.id.BOTTOM_ATTENDANCE:
                    replaceFragments(new AttendanceFragment());
                    break;
                case R.id.BOTTOM_ACCOUNT:
                    replaceFragments(new AccountFragment());
                    break;
            }

            return true;
        });
    }

    private void initUserData() {

        Utility.setUserDetails();
    }

    private void replaceFragments(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.FRAME_LAYOUT, fragment);
        fragmentTransaction.commitNow();
    }
}