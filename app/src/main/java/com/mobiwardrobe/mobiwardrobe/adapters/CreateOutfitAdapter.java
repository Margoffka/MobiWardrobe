package com.mobiwardrobe.mobiwardrobe.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobiwardrobe.mobiwardrobe.R;

import java.util.ArrayList;

public class CreateOutfitAdapter extends RecyclerView.Adapter<CreateOutfitAdapter.CreateOutfitHolder> {
    private Context context;
    private ArrayList<Uri> uriArrayList;

    public CreateOutfitAdapter(Context context, ArrayList<Uri> uriArrayList) {
        this.context = context;
        this.uriArrayList = uriArrayList;
    }

    @NonNull
    @Override
    public CreateOutfitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_show_element, parent, false);
        return new CreateOutfitHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreateOutfitHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(uriArrayList.get(position)).into(holder.showElementImage);
//        holder.showElementImage.setImageURI(uriArrayList.get(position));
        holder.deleteElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uriArrayList.remove(uriArrayList.get(position));
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public static class CreateOutfitHolder extends RecyclerView.ViewHolder {
        ImageView showElementImage;
        ImageView deleteElement;

        public CreateOutfitHolder(@NonNull View itemView) {
            super(itemView);
            showElementImage = itemView.findViewById(R.id.iv_show_element_item);
            deleteElement = itemView.findViewById(R.id.iv_delete_element_item);
        }
    }
}
