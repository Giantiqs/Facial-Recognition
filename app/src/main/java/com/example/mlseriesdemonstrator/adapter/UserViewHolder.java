package com.example.mlseriesdemonstrator.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;

public class UserViewHolder extends RecyclerView.ViewHolder {

  private static final String TAG = "UserViewHolder";
  public TextView institutionalId;
  public TextView fullName;
  public TextView institutionalEmail;

  public UserViewHolder(@NonNull View itemView) {
    super(itemView);

    institutionalId = itemView.findViewById(R.id.INSTITUTIONAL_ID);
    institutionalEmail = itemView.findViewById(R.id.INSTITUTIONAL_EMAIL);
    fullName = itemView.findViewById(R.id.USER_FULL_NAME);
  }
}
