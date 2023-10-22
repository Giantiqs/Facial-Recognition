package com.example.mlseriesdemonstrator.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
  private static final String TAG = "UserAdapter";
  private List<User> users = new ArrayList<>();
  private final Context   context;

  // Make the UsersCallback interface public
  public interface UsersCallback {
    void onUsersRetrieved(List<User> users);
  }

  public UserAdapter(Context context) {
    this.context = context;
  }

  public void fetchUsers(UsersCallback callback) {

    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

    /*
    * Write a code here that will retrieve users in users collection
     */
  }


  @NonNull
  @Override
  public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new UserViewHolder(
            LayoutInflater.from(context).inflate(R.layout.account_view_holder, parent, false)
    );
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
    holder.institutionalEmail.setText(""); // You might want to set the actual email here
    holder.fullName.setText(fullName);
    holder.password.setText(user.getPassword());

    holder.itemView.setOnClickListener(v -> {
      // Handle item click here
    });
  }

  @Override
  public int getItemCount() {
    return users.size();
  }
}
