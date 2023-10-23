package com.example.mlseriesdemonstrator.fragments.admin;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.adapter.UserAdapter;
import com.example.mlseriesdemonstrator.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class UserAccountsFragment extends Fragment {

  private static final String TAG = "UserAccountsFragment";

  RecyclerView accountsRecyclerView;
  UserAdapter userAdapter;
  Context context;
  String[] sortOptions = {"Sort by ID", "Sort by Name", "Sort by Email"};

  public UserAccountsFragment() {
    // meow
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
    context = requireContext();

    userAdapter = new UserAdapter(context);

    accountsRecyclerView.setLayoutManager(new LinearLayoutManager(context));

    accountsRecyclerView.setAdapter(userAdapter);

    userAdapter.fetchUsers(users -> {
      if (users != null) {
        // Notify the adapter that the data has changed
        userAdapter.setUsers(users);
      }
    });

    ArrayAdapter<String> sortOptionsAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, sortOptions);
    sortOptionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    Spinner sortBySpinner = view.findViewById(R.id.SORT_BY_SPINNER);
    sortBySpinner.setAdapter(sortOptionsAdapter);

    sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        // Handle the selected sort option here
        String selectedSortOption = sortOptions[position];

        if (selectedSortOption.equals("Sort by ID")) {
          userAdapter.sortUsersByID();
        } else if (selectedSortOption.equals("Sort by Name")) {
          userAdapter.sortUsersByName();
        } else if (selectedSortOption.equals("Sort by Email")) {
          userAdapter.sortUsersByEmail();
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parentView) {
        // Do nothing
      }
    });

    return view;
  }
}
