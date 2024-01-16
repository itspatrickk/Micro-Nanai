package com.pioneer.nanaiv3.adapter;

import android.content.Context;
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

public class DependentAdapter extends RecyclerView.Adapter<DependentViewHolder> implements Filterable {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    List<MemberInfo> dataList;
    private List<MemberInfo> filteredList;


    public DependentAdapter(Context context, List<MemberInfo> items, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.dataList = items;
        this.filteredList = items;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public DependentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DependentViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dependent,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DependentViewHolder holder, int position) {
        MemberInfo member = filteredList.get(position);
        String name = filteredList.get(position).getFname() + " " + filteredList.get(position).getLname();
        String reltype = filteredList.get(position).getReltype();
        if (reltype == null) reltype = "";
        if (reltype.equalsIgnoreCase("null")) reltype = "";
        holder.nameView.setText(name.toUpperCase());
        holder.dobView.setText("DOB:  " + (filteredList.get(position).getDob() != null?filteredList.get(position).getDob():""));
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
                notifyDataSetChanged();

            }
        };
    }

    public void onItemSwipedLeft(int position) {

    }

    public void onItemSwipedRight(int position) {

    }
}
