package com.example.mlseriesdemonstrator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mlseriesdemonstrator.activities.SignInActivity;
import com.example.mlseriesdemonstrator.activities.SplashScreenActivity;
import com.example.mlseriesdemonstrator.fragments.host.EventManagerFragment;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.databinding.ActivityMainBinding;
import com.example.mlseriesdemonstrator.fragments.student.AccountFragment;
import com.example.mlseriesdemonstrator.fragments.student.AttendanceFragment;
import com.example.mlseriesdemonstrator.fragments.student.HomeFragment;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    final String HOST = "host";
    final String STUDENT = "student";
    ActivityMainBinding binding;
    User user;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            user = Utility.getUser();

            if (STUDENT.equals(user.getRole())) {
                binding.HOSTBOTTOMNAVIGATION.setVisibility(View.GONE);
                binding.STUDENTBOTTOMNAVIGATION.setVisibility(View.VISIBLE);

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
            } else if (HOST.equals(user.getRole())) {
                binding.STUDENTBOTTOMNAVIGATION.setVisibility(View.GONE);
                binding.HOSTBOTTOMNAVIGATION.setVisibility(View.VISIBLE);

                replaceFragments(
                        new com.example.mlseriesdemonstrator
                                .fragments
                                .host
                                .HomeFragment()
                );

                binding.HOSTBOTTOMNAVIGATION.setOnItemSelectedListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.BOTTOM_HOME_HOST:
                            replaceFragments(
                                    new com.example.mlseriesdemonstrator
                                            .fragments
                                            .host
                                            .HomeFragment()
                            );
                            break;
                        case R.id.BOTTOM_ATTENDANCE_HOST:
                            replaceFragments(new EventManagerFragment());
                            break;
                        case R.id.BOTTOM_ACCOUNT_HOST:
                            // replace with account settings for host
                            break;
                    }

                    return true;
                });
            } else {
                startActivity(
                        new Intent(MainActivity.this, SplashScreenActivity.class)
                );
                finish();
            }
        } else {
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
            finish();
        }

    }

    private void replaceFragments(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.FRAME_LAYOUT, fragment);
        fragmentTransaction.commit();
    }
}