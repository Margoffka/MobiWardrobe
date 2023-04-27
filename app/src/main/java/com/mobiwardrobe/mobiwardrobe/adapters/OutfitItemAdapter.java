package com.mobiwardrobe.mobiwardrobe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobiwardrobe.mobiwardrobe.R;

import java.util.ArrayList;

public class OutfitItemAdapter extends RecyclerView.Adapter<OutfitItemAdapter.OutfitItemHolder>{
    private Context context;
    private ArrayList<String> urlsList;

    public OutfitItemAdapter(Context context, ArrayList<String> urlsList) {
        this.context = context;
        this.urlsList = urlsList;
    }

    @NonNull
    @Override
    public OutfitItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_outfit, parent, false);
        return new OutfitItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OutfitItemHolder holder, int position) {
        Glide.with(context).load(urlsList.get(position)).into(holder.outfitItem);
    }


    @Override
    public int getItemCount() {
        return urlsList.size();
    }

    public static class OutfitItemHolder extends RecyclerView.ViewHolder {
        ImageView outfitItem;

        public OutfitItemHolder(@NonNull View itemView) {
            super(itemView);
            outfitItem = itemView.findViewById(R.id.iv_outfit_item);
        }
    }
}
