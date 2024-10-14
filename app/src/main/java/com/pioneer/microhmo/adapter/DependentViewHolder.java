package com.pioneer.microhmo.adapter;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pioneer.microhmo.R;

public class DependentViewHolder extends RecyclerView.ViewHolder {

    TextView nameView, dobView, reltypeView;

    RelativeLayout mainContainer;
    public DependentViewHolder(@NonNull View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.member_name);
        dobView = itemView.findViewById(R.id.member_dob);
        reltypeView = itemView.findViewById(R.id.member_reltype);
        mainContainer = itemView.findViewById(R.id.main_container);
    }
}