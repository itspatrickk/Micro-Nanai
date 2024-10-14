package com.pioneer.microhmo.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pioneer.microhmo.R;

public class SalesViewHolder extends RecyclerView.ViewHolder {

    TextView premiumView, effectivityView, productView, referenceno, nameView, currentstatus;

    TextView referencenolabel,membereffectivitylabel,memberproductslabel,memberpremiumlabel;

    LinearLayout fifthLayer;

    RelativeLayout mainContainer;
    public SalesViewHolder(@NonNull View itemView) {
        super(itemView);
        premiumView = itemView.findViewById(R.id.member_premium);
        effectivityView = itemView.findViewById(R.id.member_effectivity);
        productView = itemView.findViewById(R.id.member_products);
        referenceno = itemView.findViewById(R.id.member_pocno);
        nameView =  itemView.findViewById(R.id.member_name);
        mainContainer = itemView.findViewById(R.id.main_container);
        currentstatus = itemView.findViewById(R.id.currentstatus);
        fifthLayer = itemView.findViewById(R.id.fifth_layer);


        referencenolabel = itemView.findViewById(R.id.referenceno_label);
        membereffectivitylabel = itemView.findViewById(R.id.member_effectivity_label);
        memberproductslabel = itemView.findViewById(R.id.member_products_label);
        memberpremiumlabel = itemView.findViewById(R.id.member_premium_label);

    }
}