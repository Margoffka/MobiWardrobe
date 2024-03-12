package com.mobiwardrobe.mobiwardrobe.clothes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobiwardrobe.mobiwardrobe.R;
import com.mobiwardrobe.mobiwardrobe.adapters.ImageAdapter;
import com.mobiwardrobe.mobiwardrobe.interfaces.OnItemClickListener;
import com.mobiwardrobe.mobiwardrobe.model.Upload;

import java.util.ArrayList;
import java.util.List;


public class ClothesFragment extends Fragment implements OnItemClickListener {
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;

    private ProgressBar progressBar;

    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    private FirebaseUser firebaseUser;
    private String userID;

    private List<Upload> uploads;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Clothes");
        View view = inflater.inflate(R.layout.fragment_clothes, container, false);

        recyclerView = view.findViewById(R.id.rv_clothes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        uploads = new ArrayList<>();
        imageAdapter = new ImageAdapter(getContext(), uploads);
        recyclerView.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener(ClothesFragment.this);

        progressBar = view.findViewById(R.id.pb_clothes);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID).child("clothes");

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploads.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Upload upload = itemSnapshot.getValue(Upload.class);
                    upload.setKey(itemSnapshot.getKey());
                    uploads.add(upload);
                }
                imageAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), DetailsClothesActivity.class);
        // passing array index
        intent.putExtra("Image", uploads.get(position).getImageUrl());
        intent.putExtra("Name", uploads.get(position).getName());
        intent.putExtra("Type", uploads.get(position).getType());
        intent.putExtra("Color", uploads.get(position).getColor());
        intent.putExtra("Season", uploads.get(position).getSeason());
        intent.putExtra("Weather", uploads.get(position).getWeather());
        intent.putExtra("Key", uploads.get(position).getKey());
        startActivity(intent);
    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(requireContext(), "Whatever click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Upload selectedItem = uploads.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = firebaseStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                databaseReference.child(selectedKey).removeValue();
                Toast.makeText(requireContext(), "Удалено", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(valueEventListener);
    }
}
