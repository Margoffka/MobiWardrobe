package com.mobiwardrobe.mobiwardrobe.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobiwardrobe.mobiwardrobe.R;
import com.mobiwardrobe.mobiwardrobe.interfaces.ChooseElementListener;
import com.mobiwardrobe.mobiwardrobe.upload.Upload;

import java.util.ArrayList;

public class ElementAdapter extends RecyclerView.Adapter<ElementAdapter.ElementViewHolder> {
    private ArrayList<Upload> uploads;
    private Context context;
    ArrayList<String> urls = new ArrayList<>();
    ChooseElementListener chooseElementListener;

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
        if (uploads != null && uploads.size() > 0){
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.checkBox.isChecked()) {
                        urls.add(uploads.get(position).getImageUrl());
                    } else {
                        urls.remove(uploads.get(position).getImageUrl());
                    }
                    chooseElementListener.onChooseElement(urls);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public class ElementViewHolder extends RecyclerView.ViewHolder {
        ImageView elementImage;
        TextView elementName;
        CardView cardView;
        CheckBox checkBox;

        public ElementViewHolder(@NonNull View itemView) {
            super(itemView);
            elementImage = itemView.findViewById(R.id.iv_element_item);
            elementName = itemView.findViewById(R.id.tv_element_name);
            cardView = itemView.findViewById(R.id.cv_choose_element);
            checkBox = itemView.findViewById(R.id.checkbox_element);
        }
    }

    public void setChooseElementListener(ChooseElementListener chooseElementListener) {
        this.chooseElementListener = chooseElementListener;
    }
}
