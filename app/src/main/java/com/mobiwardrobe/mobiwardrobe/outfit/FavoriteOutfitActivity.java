package com.mobiwardrobe.mobiwardrobe.outfit;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.mobiwardrobe.mobiwardrobe.adapters.OutfitsFragmentAdapter;

import java.util.ArrayList;

public class FavoriteOutfitActivity extends AppCompatActivity {

    private OutfitsFragmentAdapter outfitsFragmentAdapter;
    private RecyclerView recyclerView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference favoriteListRef;

    private FirebaseUser firebaseUser;
    private String userID;

    private ArrayList<Outfit> favoriteOutfits;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_outfits);

        recyclerView = findViewById(R.id.rv_favorite_outfits);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        favoriteOutfits = new ArrayList<>();

        outfitsFragmentAdapter = new OutfitsFragmentAdapter(this, favoriteOutfits);
        recyclerView.setAdapter(outfitsFragmentAdapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        favoriteListRef = database.getReference("users").child(userID).child("favoriteList");
        valueEventListener = favoriteListRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteOutfits.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Outfit favorite = dataSnapshot.getValue(Outfit.class);
                    favorite.setOutfitKey(dataSnapshot.getKey());
                    favoriteOutfits.add(favorite);
                }
                outfitsFragmentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}
