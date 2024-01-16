package com.pioneer.nanaiv3;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PersonViewHolder extends RecyclerView.ViewHolder {

    TextView nameView, dobView, effdateView, productView, pocView;

    public PersonViewHolder(@NonNull View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.name);
        effdateView = itemView.findViewById(R.id.effdate);
        dobView = itemView.findViewById(R.id.dob);
        productView = itemView.findViewById(R.id.product);
        pocView = itemView.findViewById(R.id.pocno);
    }
}