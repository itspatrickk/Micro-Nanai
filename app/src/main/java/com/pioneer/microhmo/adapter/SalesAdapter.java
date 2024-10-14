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

public class SalesAdapter extends RecyclerView.Adapter<SalesViewHolder> implements Filterable {

    String UPLOADING_STATUS = "Uploading";
    String QUEUE_STATUS = "Queue";

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    List<MemberInfo> dataList;
    private List<MemberInfo> filteredList;


    public SalesAdapter(Context context, List<MemberInfo> items, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.dataList = items;
        this.filteredList = items;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public SalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SalesViewHolder(LayoutInflater.from(context).inflate(R.layout.item_member_sales,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SalesViewHolder holder, int position) {
        MemberInfo member = filteredList.get(position);
        String productDesc =  member.getProduct() != null?member.getProduct():"";

        productDesc = productDesc + (member.getProduct1() != null?(","+member.getProduct1()):"");
        productDesc = productDesc + (member.getProduct2() != null?(","+member.getProduct2()):"");
        productDesc = productDesc + (member.getProduct3() != null?(","+member.getProduct3()):"");
        if (productDesc.startsWith(","))
            productDesc = productDesc.substring(1);

        productDesc = productDesc.replace("CARDCARE PLUS", "CC+");
        productDesc = productDesc.replace("CARD CARE PLUS", "CC+");
        productDesc = productDesc.replace("CARDCARE", "CC+");
        productDesc = productDesc.replace("KABUKLOD PLAN", "K");
        productDesc = productDesc.replace("KABUKLOD", "K");

        productDesc = productDesc.replace("SAGIP PLAN INDIVIDUAL", "SPI");
        productDesc = productDesc.replace("SAGIP PLAN FAMILY PLUS", "SPF");
        productDesc = productDesc.replace("SAGIP PLAN FAMILY", "SPF");
        productDesc = productDesc.replace("SAGIP PLAN PLATINUM", "SPP");

        productDesc = productDesc.replace("SAGIP INDIVIDUAL", "SPI");
        productDesc = productDesc.replace("SAGIP FAMILY PLUS", "SPF");
        productDesc = productDesc.replace("SAGIP FAMILY", "SPF");

        productDesc = productDesc.replace(",,,", "");
        productDesc = productDesc.replace(",,", "");


        productDesc = productDesc.replace(", , ,", "");
        productDesc = productDesc.replace(", ,", "");

        if (productDesc.startsWith(",")){
            productDesc = productDesc.substring(1);
        }
        if (productDesc.endsWith(",")){
            productDesc = productDesc.substring(0, productDesc.length()-1);
        }

        String name = member.getFname() + " " + member.getLname();
        holder.nameView.setText(name.toUpperCase());
        holder.productView.setText(productDesc);// + "-" + member.getId());
        holder.referenceno.setText(member.getPoc());

        holder.effectivityView.setText(member.getEffectivity());
        holder.premiumView.setText(member.getPremium());

        holder.referencenolabel.setText("Reference No.:");
        holder.membereffectivitylabel.setText("Effectivity Date:");
        holder.memberproductslabel.setText("Products:");
        holder.memberpremiumlabel.setText("Premium:");
//ToDo uncomment this

        if (member.getCurrentstat() != null &&
                ( member.getCurrentstat().equalsIgnoreCase(UPLOADING_STATUS) ||
                        member.getCurrentstat().equalsIgnoreCase(QUEUE_STATUS))
        ){
            holder.currentstatus.setText(member.getCurrentstat());
        }else{
            holder.fifthLayer.setVisibility(View.GONE);
        }

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
