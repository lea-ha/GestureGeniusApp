package com.gesturegenius;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyAdapterClass extends RecyclerView.Adapter<MyViewClass> implements Filterable {
    Context context;
    List<ItemClass> items;
    List<ItemClass> filteredItems;

    private Picasso picasso;


    public MyAdapterClass(Context context, List<ItemClass> items) {
        this.context = context;
        this.items = items;
        picasso = Picasso.get();
    }

    @NonNull
    @Override
    public MyViewClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewClass(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewClass holder, int position) {
        ItemClass currentItem = filteredItems.get(position);
        holder.arabic_name.setText(filteredItems.get(position).getEnglishName());
        holder.english_name.setText(filteredItems.get(position).getArabicName());
        String imageUrl = filteredItems.get(position).getImageURL();
        picasso.load(imageUrl).into(holder.imgView);
    }

    @Override
    public int getItemCount() {
        if (filteredItems == null) {
            return 0;
        } else {
            return filteredItems.size();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchString = constraint.toString().toLowerCase();

                if (searchString.isEmpty()) {
                    filteredItems = items;
                } else {
                    List<ItemClass> tempFilteredList = new ArrayList<>();

                    for (ItemClass item : items) {
                        if (item.getEnglishName().toLowerCase().contains(searchString)) {
                            tempFilteredList.add(item);
                        }
                    }

                    filteredItems = tempFilteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredItems;
                filterResults.count = filteredItems.size();

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredItems = (List<ItemClass>) results.values;
                notifyDataSetChanged();
            }

        };

    }
}
