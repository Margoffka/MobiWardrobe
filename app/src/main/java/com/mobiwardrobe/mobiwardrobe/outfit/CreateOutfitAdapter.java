package com.mobiwardrobe.mobiwardrobe.outfit;

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
    CountOfImagesWhenRemoved countOfImagesWhenRemoved;

    public CreateOutfitAdapter(Context context, ArrayList<Uri> uriArrayList,
                               CountOfImagesWhenRemoved countOfImagesWhenRemoved) {
        this.context = context;
        this.uriArrayList = uriArrayList;
        this.countOfImagesWhenRemoved = countOfImagesWhenRemoved;
    }

    @NonNull
    @Override
    public CreateOutfitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_show_element, parent, false);
        return new CreateOutfitHolder(view, countOfImagesWhenRemoved);
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
                countOfImagesWhenRemoved.clicked(uriArrayList.size());
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
        CountOfImagesWhenRemoved countOfImagesWhenRemoved;

        public CreateOutfitHolder(@NonNull View itemView, CountOfImagesWhenRemoved countOfImagesWhenRemoved) {
            super(itemView);
            showElementImage = itemView.findViewById(R.id.iv_show_element_item);
            deleteElement = itemView.findViewById(R.id.iv_delete_element_item);
        }
    }

    public interface CountOfImagesWhenRemoved {
        void clicked(int getSize);
    }

}
