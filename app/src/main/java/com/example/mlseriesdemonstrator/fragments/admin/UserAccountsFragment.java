package com.example.mlseriesdemonstrator.fragments.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.adapter.UserAdapter;
import com.example.mlseriesdemonstrator.model.Event;
import com.example.mlseriesdemonstrator.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserAccountsFragment extends Fragment {
  private UserAdapter userAdapter;
  private EditText searchBox;
  private Spinner sortBySpinner;
  private RecyclerView usersRecyclerView;

  public UserAccountsFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_user_accounts, container, false);

    // Initialize views and adapters
    userAdapter = new UserAdapter(requireContext(), new ArrayList<>());
    usersRecyclerView = view.findViewById(R.id.USERS_RECYCLER_VIEW);
    searchBox = view.findViewById(R.id.SEARCH_BOX);
    sortBySpinner = view.findViewById(R.id.SORT_BY_SPINNER);

    // Set the adapter for the RecyclerView
    usersRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    usersRecyclerView.setAdapter(userAdapter);

    // Fetch and set the initial user data
    userAdapter.fetchUsers(users -> {
      if (users != null)
        userAdapter.setUsers(users);
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
      List<User> filteredUsers = new ArrayList<>();
      for (User user : userAdapter.getOriginalData()) {
        if (user.getLastName().toLowerCase().contains(searchText.toLowerCase())) {
          filteredUsers.add(user);
        }
      }

      Log.d("UserAccountsFragment", "Filtered Users Count: " + filteredUsers.size());

      userAdapter.filterUsers(filteredUsers);

      Log.d("UserAccountsFragment", "Filtered Users Count After Filter: " + userAdapter.getItemCount());
    }
  }

}
