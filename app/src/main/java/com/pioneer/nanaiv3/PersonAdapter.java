package com.pioneer.nanaiv3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonViewHolder> {

    Context context;
    List<ItemPerson> items;

    public PersonAdapter(Context context, List<ItemPerson> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PersonViewHolder(LayoutInflater.from(context).inflate(R.layout.item_person,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        holder.nameView.setText(items.get(position).getName());
        holder.dobView.setText("DOB : " + items.get(position).getDob());
        holder.effdateView.setText("Effectivity : " + items.get(position).getEffdate());
        holder.pocView.setText("POC : " + items.get(position).getPoc());
        holder.productView.setText("Product : " + items.get(position).getProduct());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
