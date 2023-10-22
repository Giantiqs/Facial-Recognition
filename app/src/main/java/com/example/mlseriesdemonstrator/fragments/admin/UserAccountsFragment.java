package com.example.mlseriesdemonstrator.fragments.admin;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.adapter.UserAdapter;
import com.example.mlseriesdemonstrator.model.User;

public class UserAccountsFragment extends Fragment {

  private static final String TAG = "UserAccountsFragment";

  RecyclerView accountsRecyclerView;
  UserAdapter userAdapter;

  public UserAccountsFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_user_accounts, container, false);
    accountsRecyclerView = view.findViewById(R.id.USERS_RECYCLER_VIEW);

    accountsRecyclerView = view.findViewById(R.id.USERS_RECYCLER_VIEW);

    // Create a layout manager for the RecyclerView
    LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
    accountsRecyclerView.setLayoutManager(layoutManager);

    // Create an instance of UserAdapter
    userAdapter = new UserAdapter(requireActivity());

    // Set the adapter on your RecyclerView
    accountsRecyclerView.setAdapter(userAdapter);

    // Fetch user data and populate the RecyclerView
    userAdapter.fetchUsers(users -> {
      if (users != null) {
        for (User user : users) {
          Log.d(TAG, user.getLastName());
        }
        userAdapter.notifyDataSetChanged();
      } else {
        // Handle the case where user data couldn't be fetched
        // You might want to show an error message to the user.
        Log.d(TAG, "no user");
      }
    });

    return view;
  }
}
