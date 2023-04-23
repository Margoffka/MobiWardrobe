package com.mobiwardrobe.mobiwardrobe.outfit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import java.util.ArrayList;

public class OutfitFragment extends Fragment implements OutfitClickListener {
    private OutfitsFragmentAdapter outfitsFragmentAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageView deleteOutfit;

    private ArrayList<Outfit> outfits;

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private String userID;

    private ValueEventListener valueEventListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Outfit");
        View view = inflater.inflate(R.layout.fragment_outfit, container, false);
        progressBar = view.findViewById(R.id.pb_outfit_fragment);

        recyclerView = view.findViewById(R.id.rv_outfit_fragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        outfits = new ArrayList<>();

        outfitsFragmentAdapter = new OutfitsFragmentAdapter(getContext(), outfits);
        recyclerView.setAdapter(outfitsFragmentAdapter);
        outfitsFragmentAdapter.setOutfitClickListener(OutfitFragment.this);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID).child("outfits");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                outfits.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Outfit outfit = dataSnapshot.getValue(Outfit.class);
                    outfits.add(outfit);
                }
                outfitsFragmentAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return view;
    }

    @Override
    public void onOutfitClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {
        Outfit selectedItem = outfits.get(position);
        final String selectedKey = selectedItem.getOutfitName();
        databaseReference.child(selectedKey).removeValue();
        Toast.makeText(requireContext(), "Комплект удалён", Toast.LENGTH_SHORT).show();
    }
}
