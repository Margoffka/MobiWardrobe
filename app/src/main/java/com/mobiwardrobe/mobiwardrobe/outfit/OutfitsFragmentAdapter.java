package com.mobiwardrobe.mobiwardrobe.outfit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiwardrobe.mobiwardrobe.R;

import java.util.ArrayList;

public class OutfitsFragmentAdapter extends RecyclerView.Adapter<OutfitsFragmentAdapter.OutfitFragmentHolder>{
    private Context context;
    private ArrayList<Outfit> outfits;
    private OutfitClickListener outfitClickListener;

    public OutfitsFragmentAdapter(Context context, ArrayList<Outfit> outfits) {
        this.context = context;
        this.outfits = outfits;
    }

    @NonNull
    @Override
    public OutfitFragmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OutfitFragmentHolder(LayoutInflater.from(context).inflate(R.layout.recycler_outfit, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull OutfitFragmentHolder holder, int position) {
        holder.outfitTitle.setText(outfits.get(position).getOutfitName());
        setItemRecycler(holder.outfitRecycler, outfits.get(position).getImageUrls());
    }

    @Override
    public int getItemCount() {
        return outfits.size();
    }

    public final class OutfitFragmentHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView outfitTitle;
        RecyclerView outfitRecycler;
        private ImageView deleteOutfit;

        public OutfitFragmentHolder(@NonNull View itemView) {
            super(itemView);

            outfitTitle = itemView.findViewById(R.id.tv_outfit_title);
            outfitRecycler = itemView.findViewById(R.id.rv_outfit_items);
            deleteOutfit = itemView.findViewById(R.id.iv_delete_outfit);

            deleteOutfit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    outfitClickListener.onDeleteClick(getAdapterPosition());
                }
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (outfitClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    outfitClickListener.onOutfitClick(position);
                }
            }
        }
    }

    public void setOutfitClickListener(OutfitClickListener listener) {
        outfitClickListener = listener;
    }

    private void setItemRecycler(RecyclerView recyclerView, ArrayList<String> imageUrls){

        OutfitItemAdapter outfitItemAdapter = new OutfitItemAdapter(context, imageUrls);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(outfitItemAdapter);

    }
}
