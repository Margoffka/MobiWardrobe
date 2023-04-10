package com.mobiwardrobe.mobiwardrobe.outfit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobiwardrobe.mobiwardrobe.R;
import com.mobiwardrobe.mobiwardrobe.upload.Upload;

import java.util.ArrayList;

public class ElementAdapter extends RecyclerView.Adapter<ElementAdapter.ElementViewHolder> {
    private ArrayList<Upload> uploads;
    private Context context;
    boolean isSelected = false;
    ArrayList<Upload> selectedItems = new ArrayList<>();

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
    public void onBindViewHolder(@NonNull ElementViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(uploads.get(position).getImageUrl()).into(holder.elementImage);
        holder.elementName.setText(uploads.get(position).getName());
           }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public class ElementViewHolder extends RecyclerView.ViewHolder {
        ImageView elementImage;
        TextView elementName;
        ImageView selectedElement;
        CardView cardView;

        public ElementViewHolder(@NonNull View itemView) {
            super(itemView);
            elementImage = itemView.findViewById(R.id.iv_element_item);
            elementName = itemView.findViewById(R.id.tv_element_name);
            selectedElement = itemView.findViewById(R.id.iv_selected_element_item);
            cardView =itemView.findViewById(R.id.cv_choose_element);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public boolean onLongClick(View v) {
                    isSelected = true;
                    if (selectedItems.contains(uploads.get(getAdapterPosition()))){
                        itemView.setBackgroundResource(Color.TRANSPARENT);
                        selectedElement.setVisibility(View.INVISIBLE);
                        selectedItems.remove(uploads.get(getAdapterPosition()));
                    } else {
                        itemView.setBackgroundResource(R.drawable.white_background);
                        selectedElement.setVisibility(View.VISIBLE);
                        selectedItems.add(uploads.get(getAdapterPosition()));
                    }
                    if (selectedItems.size() == 0){
                        isSelected = false;
                    }
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {
                   if(isSelected) {
                       if (selectedItems.contains(uploads.get(getAdapterPosition()))) {
                           itemView.setBackgroundResource(Color.TRANSPARENT);
                           selectedElement.setVisibility(View.INVISIBLE);
                           selectedItems.remove(uploads.get(getAdapterPosition()));
                       } else {
                           itemView.setBackgroundResource(R.drawable.white_background);
                           selectedElement.setVisibility(View.VISIBLE);
                           selectedItems.add(uploads.get(getAdapterPosition()));
                       }
                       if (selectedItems.size() == 0){
                           isSelected = false;
                       }
                   }
                }
            });
        }
    }

}
