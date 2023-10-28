package com.example.mlseriesdemonstrator.fragments.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.adapter.UserAdapter;

import java.util.ArrayList;

public class UserAccountsFragment extends Fragment {

  private UserAdapter userAdapter;
  EditText searchBox;
  RecyclerView usersRecyclerView;

  public UserAccountsFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_user_accounts, container, false);

    // Initialize views and adapters
    userAdapter = new UserAdapter(requireContext(), new ArrayList<>());
    usersRecyclerView = view.findViewById(R.id.USERS_RECYCLER_VIEW);
    searchBox = view.findViewById(R.id.SEARCH_BOX);

    // Set the adapter for the RecyclerView
    usersRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    usersRecyclerView.setAdapter(userAdapter);

    // Fetch and set the initial user data
    userAdapter.fetchUsers(users -> {
      if (users != null)
        userAdapter.setData(users);
    });

    searchBox.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        filterUsers(s.toString());
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    return view;
  }

  private void filterUsers(String searchText) {
    if (userAdapter != null) {
      userAdapter.filterUsers(searchText);
    }
  }

}
