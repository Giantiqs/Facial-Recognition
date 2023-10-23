package com.example.mlseriesdemonstrator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mlseriesdemonstrator.activities.SignInActivity;
import com.example.mlseriesdemonstrator.activities.SplashScreenActivity;
import com.example.mlseriesdemonstrator.databinding.ActivityMainBinding;
import com.example.mlseriesdemonstrator.fragments.admin.AdminAccountFragment;
import com.example.mlseriesdemonstrator.fragments.admin.AdminDashBoardFragment;
import com.example.mlseriesdemonstrator.fragments.admin.EventControlPanelFragment;
import com.example.mlseriesdemonstrator.fragments.admin.UserAccountsFragment;
import com.example.mlseriesdemonstrator.fragments.host.EventManagerFragment;
import com.example.mlseriesdemonstrator.fragments.student.AccountFragment;
import com.example.mlseriesdemonstrator.fragments.student.AttendanceFragment;
import com.example.mlseriesdemonstrator.fragments.student.HomeFragment;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  Context context;
  final String HOST = "host";
  final String STUDENT = "student";
  final String ADMIN = "admin";
  ActivityMainBinding binding;
  User user;

  @SuppressLint("NonConstantResourceId")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    context = MainActivity.this;
    // Check if a user is logged in, else it will redirect to sign in screen
    if (firebaseUser != null) {
      user = Utility.getUser();
      Utility.addUserIds(user.getUID());

      String password = "";

      if (getIntent().getStringExtra("password") != null)
        password = getIntent().getStringExtra("password");

      try {
        if (!Objects.equals(password, "") && !Utility.verifyHash(password, user.getPasswordHashCode())) {
          assert password != null;
          String newPassHash = Utility.hashString(password);
          user.setPasswordHashCode(newPassHash);

          DocumentReference userRefs = Utility.getUserRef().document(user.getUID());

          userRefs.set(user)
                  .addOnCompleteListener(task -> Log.d(TAG, "new password set"))
                  .addOnFailureListener(e -> Log.d(TAG, Objects.requireNonNull(e.getLocalizedMessage())));
        } else {
          Log.d(TAG, "same password");
        }

        // Check the role of the user to know which navigation bar will be visible
        // If user has no role, redirect to splash screen in screen
        if (STUDENT.equals(user.getRole())) {
          binding.HOSTBOTTOMNAVIGATION.setVisibility(View.GONE);
          binding.STUDENTBOTTOMNAVIGATION.setVisibility(View.VISIBLE);
          binding.ADMINBOTTOMNAVIGATION.setVisibility(View.GONE);

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
          binding.ADMINBOTTOMNAVIGATION.setVisibility(View.GONE);

          replaceFragments(
                  new com.example.mlseriesdemonstrator
                          .fragments
                          .host
                          .HomeFragment()
          );

          binding.HOSTBOTTOMNAVIGATION.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
              case R.id.BOTTOM_HOME_HOST:
                replaceFragments(new com.example.mlseriesdemonstrator.fragments.host.HomeFragment());
                break;
              case R.id.BOTTOM_ATTENDANCE_HOST:
                replaceFragments(new EventManagerFragment());
                break;
              case R.id.BOTTOM_ACCOUNT_HOST:
                replaceFragments(new com.example.mlseriesdemonstrator.fragments.host.AccountFragment());
                break;
            }

            return true;
          });
        } else if (ADMIN.equals(user.getRole())) {
          binding.STUDENTBOTTOMNAVIGATION.setVisibility(View.GONE);
          binding.HOSTBOTTOMNAVIGATION.setVisibility(View.GONE);
          binding.ADMINBOTTOMNAVIGATION.setVisibility(View.VISIBLE);

          replaceFragments(new AdminDashBoardFragment());

          binding.ADMINBOTTOMNAVIGATION.setOnItemSelectedListener(item -> {
            switch(item.getItemId()) {
              case R.id.BOTTOM_ADMIN_DASHBOARD:
                replaceFragments(new AdminDashBoardFragment());
                break;
              case R.id.BOTTOM_EVENT_PANEL:
                replaceFragments(new EventControlPanelFragment());
                break;
              case R.id.BOTTOM_ACCOUNT_ADMIN:
                replaceFragments(new UserAccountsFragment());
                break;
              case R.id.ADMIN_ACCOUNT:
                replaceFragments(new AdminAccountFragment());
                break;
            }

            return true;
          });
        } else {
          startActivity(new Intent(context, SplashScreenActivity.class));
          finish();
        }
      } catch (NoSuchAlgorithmException e) {
        Log.d(TAG, "rip l bozo" + e.getLocalizedMessage());
      }
    } else {
      startActivity(new Intent(context, SignInActivity.class));
      finish();
    }

  }

  // Replace Fragments instead of changing whole screen
  private void replaceFragments(Fragment fragment) {

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    fragmentTransaction.replace(R.id.FRAME_LAYOUT, fragment);
    fragmentTransaction.commit();
  }
}