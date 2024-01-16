package com.pioneer.nanaiv3.adapter;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pioneer.nanaiv3.R;

public class MemberViewHolderPending extends RecyclerView.ViewHolder {

    TextView nameView, dobView, productView, reltypeView;

    RelativeLayout mainContainer;
    public MemberViewHolderPending(@NonNull View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.member_name);
        dobView = itemView.findViewById(R.id.member_dob);
        productView = itemView.findViewById(R.id.member_product);
        reltypeView = itemView.findViewById(R.id.member_reltype);
        mainContainer = itemView.findViewById(R.id.main_container);
    }
}