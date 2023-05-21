package com.mobiwardrobe.mobiwardrobe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobiwardrobe.mobiwardrobe.R;
import com.mobiwardrobe.mobiwardrobe.calendar.EventEditActivity;
import com.mobiwardrobe.mobiwardrobe.interfaces.OutfitClickListener;
import com.mobiwardrobe.mobiwardrobe.outfit.Outfit;

import java.util.ArrayList;

public class OutfitsFragmentAdapter extends RecyclerView.Adapter<OutfitsFragmentAdapter.OutfitFragmentHolder>{
    private Context context;
    private ArrayList<Outfit> outfits;
    private OutfitClickListener outfitClickListener;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference favoriteReference, favoriteListRef;
    private Boolean favoriteChecker = false;

    private FirebaseUser firebaseUser;
    private String userID;

    public OutfitsFragmentAdapter(Context context, ArrayList<Outfit> outfits) {
        this.context = context;
        this.outfits = outfits;
    }

    @NonNull
    @Override
    public OutfitFragmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OutfitFragmentHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler_outfit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OutfitFragmentHolder holder, int position) {
        holder.outfitTitle.setText(outfits.get(position).getOutfitName());
        setItemRecycler(holder.outfitRecycler, outfits.get(position).getImageUrls());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        favoriteReference = database.getReference("users").child(userID).child("favorites");
        favoriteListRef = database.getReference("users").child(userID).child("favoriteList");

        String name = outfits.get(position).getOutfitName();
        ArrayList<String> imageUrls = outfits.get(position).getImageUrls();
        holder.favoriteChecker(outfits.get(position).getOutfitKey());

        String outfitKey = outfits.get(position).getOutfitKey();

        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteChecker = true;
                favoriteReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (favoriteChecker.equals(true)){
                            if (snapshot.hasChild(outfitKey)){
                                favoriteReference.child(outfitKey).removeValue();
                                favoriteListRef.child(outfitKey).removeValue();
                            } else {
                                Outfit favoriteOutfit = outfits.get(holder.getAdapterPosition());
                                favoriteOutfit.setOutfitKey(outfitKey);
                                favoriteOutfit.setOutfitName(name);
                                favoriteOutfit.setImageUrls(imageUrls);

                                favoriteReference.child(outfitKey).setValue(true);
                                favoriteListRef.child(outfitKey).setValue(favoriteOutfit);
                            }
                            favoriteChecker = false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return outfits.size();
    }

    public final class OutfitFragmentHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView outfitTitle;
        RecyclerView outfitRecycler;
        ImageView deleteOutfit;
        ImageView favoriteButton;

        public OutfitFragmentHolder(@NonNull View itemView) {
            super(itemView);

            outfitTitle = itemView.findViewById(R.id.tv_outfit_title);
            outfitRecycler = itemView.findViewById(R.id.rv_outfit_items);
            deleteOutfit = itemView.findViewById(R.id.iv_delete_outfit);

            if(context instanceof EventEditActivity){
                deleteOutfit.setVisibility(View.INVISIBLE);
            }

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
                    outfitClickListener.onOutfitClick(position, itemView);
                }
            }
        }

        public void favoriteChecker(String outfitKey) {
            favoriteButton = itemView.findViewById(R.id.iv_add_to_favorites);

            if(context instanceof EventEditActivity){
                favoriteButton.setVisibility(View.INVISIBLE);
            }

            favoriteReference = database.getReference("users").child(userID).child("favorites");
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            userID = firebaseUser.getUid();

            favoriteReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(outfitKey)){
                        favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24);
                    } else {
                        favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void setOutfitClickListener(OutfitClickListener listener) {
        outfitClickListener = listener;
    }

    private void setItemRecycler(RecyclerView recyclerView, ArrayList<String> imageUrls){
        OutfitItemAdapter outfitItemAdapter = new OutfitItemAdapter(context, imageUrls, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(outfitItemAdapter);
    }
}
