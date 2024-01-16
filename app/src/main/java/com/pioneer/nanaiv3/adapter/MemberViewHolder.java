package com.pioneer.nanaiv3.adapter;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pioneer.nanaiv3.R;

public class MemberViewHolder extends RecyclerView.ViewHolder {

    TextView nameView, dobView,  reltypeView;

    RelativeLayout mainContainer;
    public MemberViewHolder(@NonNull View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.member_name);
        dobView = itemView.findViewById(R.id.member_dob);
        reltypeView = itemView.findViewById(R.id.member_reltype);
        mainContainer = itemView.findViewById(R.id.main_container);
    }
}