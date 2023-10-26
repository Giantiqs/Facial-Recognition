package com.example.mlseriesdemonstrator.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.Event;
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
  List<User> originalUsers;
  List<User> filteredUsers;

  private final Context context;

  // Make the UsersCallback interface public
  public interface UsersCallback {
    void onUsersRetrieved(List<User> users);
  }

  public UserAdapter(Context context, ArrayList<User> originalUsers) {
    this.context = context;
    this.originalUsers = originalUsers;
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

  @SuppressLint("NotifyDataSetChanged")
  public void setUsers(List<User> users) {
    this.originalUsers = users;
    notifyDataSetChanged(); // Notify the RecyclerView to refresh the view
  }

  @Override
  public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
    User user = originalUsers.get(position);
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
    return originalUsers.size();
  }

  public void setData(List<User> users) {
    this.originalUsers = users;
    this.filteredUsers = users;
    notifyDataSetChanged();
  }

  public List<User> getOriginalData() {
    return originalUsers;
  }

  public void filterUsers(List<User> filteredUsers) {
    this.filteredUsers = filteredUsers;
    notifyDataSetChanged();
  }


}
