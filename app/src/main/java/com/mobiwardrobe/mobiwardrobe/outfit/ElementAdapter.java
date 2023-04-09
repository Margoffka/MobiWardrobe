package com.mobiwardrobe.mobiwardrobe.outfit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobiwardrobe.mobiwardrobe.R;
import com.mobiwardrobe.mobiwardrobe.upload.Upload;

import java.util.ArrayList;

public class ElementAdapter extends RecyclerView.Adapter<ElementAdapter.ElementViewHolder> {
    private ArrayList<Upload> uploads;
    private Context context;

    public ElementAdapter(Context context, ArrayList<Upload> uploads) {
        this.context = context;
        this.uploads = uploads;
    }
    @NonNull
    @Override
    public ElementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_element, parent, false);
        return new ElementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ElementViewHolder holder, int position) {
        Glide.with(context).load(uploads.get(position).getImageUrl()).into(holder.elementImage);
        holder.elementName.setText(uploads.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public static class ElementViewHolder extends RecyclerView.ViewHolder {
        ImageView elementImage;
        TextView elementName;
        public ElementViewHolder(@NonNull View itemView) {
            super(itemView);
            elementImage = itemView.findViewById(R.id.iv_element_item);
            elementName = itemView.findViewById(R.id.tv_element_name);
        }
    }
}
