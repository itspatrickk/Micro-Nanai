package com.pioneer.nanaiv3.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pioneer.nanaiv3.R;
import com.pioneer.nanaiv3.objects.MemberInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MemberAdapterPending extends RecyclerView.Adapter<MemberViewHolderPending> implements Filterable {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    List<MemberInfo> dataList;
    private List<MemberInfo> filteredList;


    public MemberAdapterPending(Context context, List<MemberInfo> items, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.dataList = items;
        this.filteredList = items;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MemberViewHolderPending onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemberViewHolderPending(LayoutInflater.from(context).inflate(R.layout.item_member_pending,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolderPending holder, int position) {
        MemberInfo member = filteredList.get(position);
        String name = filteredList.get(position).getFname() + " " + filteredList.get(position).getLname();
        holder.nameView.setText(name.toUpperCase());
        holder.dobView.setText("DOB : " + (member.getDob() != null?member.getDob():""));
        holder.reltypeView.setText(filteredList.get(position).getPertype());

        Log.d("items", "POC :" + member.getPoc());
        Log.d("items", "getProduct1 :" + member.getProduct1());
        Log.d("items", "getProduct2 :" + member.getProduct2());
        Log.d("items", "getProduct3 :" + member.getProduct3());

        String product = filteredList.get(position).getPoc() +" - ";
        product = product + (member.getProduct1() != null?(member.getProduct1() +", "):"");
        product = product + (member.getProduct2() != null?(member.getProduct2() +", "):"");
        product = product + (member.getProduct3() != null?(member.getProduct3() +", "):"");

        if (product.length() > 5){
            product = product.substring(0, product.length()-2);
        }

        Log.d("items", "product :" + product);

        holder.productView.setText(product);
        holder.mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewInterface.onItemClick(member);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String filterPattern = constraint.toString().toLowerCase(Locale.getDefault());

                if (filterPattern.isEmpty()) {
                    results.values = dataList;
                    results.count = dataList.size();
                } else {
                    List<MemberInfo> filteredItems = new ArrayList<MemberInfo>();

                    for (MemberInfo item : dataList) {
                        if (item.getFname().toLowerCase(Locale.getDefault()).contains(filterPattern)
                            || item.getLname().toLowerCase(Locale.getDefault()).contains(filterPattern)
                            || (item.getPoc() != null && item.getPoc().toLowerCase(Locale.getDefault()).contains(filterPattern))
                        ) {
                            filteredItems.add(item);
                        }
                    }

                    results.values = filteredItems;
                    results.count = filteredItems.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (List<MemberInfo>) results.values;
//                dataList = (List<MemberInfo>) results.values;
                notifyDataSetChanged();

            }
        };
    }
}
