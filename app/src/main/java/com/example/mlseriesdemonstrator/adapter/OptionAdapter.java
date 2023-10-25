package com.example.mlseriesdemonstrator.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlseriesdemonstrator.R;
import com.example.mlseriesdemonstrator.model.Options;
import com.example.mlseriesdemonstrator.view_holder.OptionViewHolder;

import java.util.ArrayList;

public class OptionAdapter extends RecyclerView.Adapter<OptionViewHolder> {

  private static final String TAG = "OptionAdapter";
  Context context;
  ArrayList<Options> options;
  FragmentManager fragmentManager;

  public OptionAdapter(Context context, FragmentManager fragmentManager, ArrayList<Options> options) {
    this.context = context;
    this.options = options;
    this.fragmentManager = fragmentManager;
  }

  @NonNull
  @Override
  public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    Log.d(TAG, "hallo hallo");

    return new OptionViewHolder(
            LayoutInflater.from(context).inflate(R.layout.option_view, parent, false)
    );
  }

  @Override
  public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
    holder.optionMsg.setText(options.get(position).getMessage());
    holder.optionBtn.setOnClickListener(v -> replaceFragments(options.get(position).getFragment()));
  }

  @Override
  public int getItemCount() {
    return options.size();
  }

  private void replaceFragments(Fragment fragment) {

    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.FRAME_LAYOUT, fragment);
    fragmentTransaction.commit();
  }
}
