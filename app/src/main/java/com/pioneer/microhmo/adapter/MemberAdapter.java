package com.pioneer.microhmo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pioneer.microhmo.R;
import com.pioneer.microhmo.objects.MemberInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MemberAdapter extends RecyclerView.Adapter<MemberViewHolder> implements Filterable {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    List<MemberInfo> dataList;
    private List<MemberInfo> filteredList;


    public MemberAdapter(Context context, List<MemberInfo> items, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.dataList = items;
        this.filteredList = items;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemberViewHolder(LayoutInflater.from(context).inflate(R.layout.item_member,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        MemberInfo member = filteredList.get(position);
        String productDesc =  member.getProduct() != null?member.getProduct():"";

        String reltype = filteredList.get(position).getReltype();
        if (reltype == null) reltype = "";
        if (reltype.equalsIgnoreCase("null")) reltype = "";

        productDesc = productDesc + (member.getProduct1() != null?(","+member.getProduct1()):"");
        productDesc = productDesc + (member.getProduct2() != null?(","+member.getProduct2()):"");
        productDesc = productDesc + (member.getProduct3() != null?(","+member.getProduct3()):"");
        if (productDesc.startsWith(","))
            productDesc = productDesc.substring(1);

        productDesc = "Product  : " +productDesc + "-"+ member.getPoc();

        String name = member.getFname() + " " + member.getLname();
        holder.nameView.setText(name.toUpperCase());
        holder.dobView.setText("DOB:  " + (member.getDob() != null?member.getDob():""));
        holder.reltypeView.setText("Reltype:  " +reltype);
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

    public void onItemSwipedLeft(int position) {

    }

    public void onItemSwipedRight(int position) {

    }
}
