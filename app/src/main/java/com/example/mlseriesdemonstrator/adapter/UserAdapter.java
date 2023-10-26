package com.example.mlseriesdemonstrator.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.User;
import com.example.mlseriesdemonstrator.utilities.Utility;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
  static final String TAG = "UserAdapter";
  List<User> users = new ArrayList<>();
  private List<User> filteredUsers = new ArrayList<>();

  private final Context   context;

  // Make the UsersCallback interface public
  public interface UsersCallback {
    void onUsersRetrieved(List<User> users);
  }

  public UserAdapter(Context context) {
    this.context = context;
  }

  public void fetchUsers(UsersCallback callback) {

    CollectionReference reference = FirebaseFirestore.getInstance().collection("user_id");

    reference.get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
              List<User> users = new ArrayList<>();

              for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                // Get the user ID from the document
                String userId = documentSnapshot.getId();

                // Now, fetch the user details using the obtained user ID
                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(userId)
                        .collection("user_details")
                        .get()
                        .addOnSuccessListener(userDetailsSnapshots -> {
                          if (!userDetailsSnapshots.isEmpty()) {
                            User user = userDetailsSnapshots.toObjects(User.class).get(0);
                            users.add(user);
                            if (users.size() == queryDocumentSnapshots.size()) {
                              // All users have been fetched
                              callback.onUsersRetrieved(users);
                            }
                          }
                        })
                        .addOnFailureListener(e -> {
                          Log.e(TAG, "Error fetching user details: " + e.getMessage());
                          callback.onUsersRetrieved(null);
                        });
              }
            })
            .addOnFailureListener(e -> {
              Log.e(TAG, "Error fetching user IDs: " + e.getMessage());
              callback.onUsersRetrieved(null);
            });
  }

  @NonNull
  @Override
  public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new UserViewHolder(
            LayoutInflater.from(context).inflate(R.layout.account_view_holder, parent, false)
    );
  }

  public void setUsers(List<User> users) {
    this.users = users;
    notifyDataSetChanged(); // Notify the RecyclerView to refresh the view
  }

  @Override
  public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
    User user = users.get(position);
    String fullName = String.format(
            "%s %s %s",
            user.getFirstName(),
            user.getMiddleName().charAt(0),
            user.getLastName()
    );

    holder.institutionalId.setText(user.getInstitutionalID());
    holder.institutionalEmail.setText(user.getInstitutionalEmail());
    holder.fullName.setText(fullName);

    holder.itemView.setOnLongClickListener(v -> {
      Utility.showToast(context, "hi");

      return true;
    });
  }

  @Override
  public int getItemCount() {
    return users.size();
  }

  public void sortUsersByID() {
    // Sort the users by ID (implement sorting logic)
    Collections.sort(users, (user1, user2) -> user1.getInstitutionalID().compareTo(user2.getInstitutionalID()));
    notifyDataSetChanged(); // Notify the RecyclerView to refresh the view
  }

  public void sortUsersByName() {
    // Sort the users by name (implement sorting logic)
    Collections.sort(users, (user1, user2) -> {
      String fullName1 = user1.getFirstName() + user1.getLastName();
      String fullName2 = user2.getFirstName() + user2.getLastName();
      return fullName1.compareTo(fullName2);
    });
    notifyDataSetChanged(); // Notify the RecyclerView to refresh the view
  }

  public void sortUsersByEmail() {
    // Sort the users by email (implement sorting logic)
    Collections.sort(users, (user1, user2) -> user1.getInstitutionalEmail().compareTo(user2.getInstitutionalEmail()));
    notifyDataSetChanged(); // Notify the RecyclerView to refresh the view
  }


  public void filterUsers(String searchText) {
    // Clear the filteredUsers list
    filteredUsers.clear();

    if (searchText.isEmpty()) {
      // If search text is empty, show all users
      filteredUsers.addAll(users);
    } else {
      // Filter users based on the search text
      for (User user : users) {
        if (user.getInstitutionalEmail().toLowerCase().contains(searchText.toLowerCase())) {
          filteredUsers.add(user);
        }
      }
    }

    notifyDataSetChanged(); // Notify the RecyclerView to refresh the view
  }


}
