package com.mobiwardrobe.mobiwardrobe.outfitupload;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobiwardrobe.mobiwardrobe.R;
import com.mobiwardrobe.mobiwardrobe.adapters.ElementAdapter;
import com.mobiwardrobe.mobiwardrobe.interfaces.ChooseElementListener;
import com.mobiwardrobe.mobiwardrobe.upload.Upload;

import java.util.ArrayList;

public class ChooseElementsActivity extends AppCompatActivity implements ChooseElementListener {
    private ElementAdapter elementAdapter;
    private RecyclerView recyclerView;
    private ImageView confirmElements;

    private ArrayList<Upload> uploads;

    private DatabaseReference databaseReference;

    private FirebaseUser firebaseUser;
    private String userID;

    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_elements);

        recyclerView = findViewById(R.id.rv_elements);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID).child("clothes");

        uploads = new ArrayList<>();
        elementAdapter = new ElementAdapter(this, uploads);
        recyclerView.setAdapter(elementAdapter);
        elementAdapter.setChooseElementListener(ChooseElementsActivity.this);

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Upload upload = dataSnapshot.getValue(Upload.class);
                    uploads.add(upload);
                }
                elementAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onChooseElement(ArrayList<String> arrayList) {
        confirmElements = findViewById(R.id.iv_confirm_elements);
        confirmElements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseElementsActivity.this, CreateOutfitActivity.class);
                // passing array index
//                intent.putExtra("Image", uploads.get(position).getImageUrl());
//                intent.putExtra("Name", uploads.get(position).getName());
//                intent.putExtra("Type", uploads.get(position).getType());
//                intent.putExtra("Color", uploads.get(position).getColor());
//                intent.putExtra("Season", uploads.get(position).getSeason());
//                intent.putExtra("Weather", uploads.get(position).getWeather());
//                intent.putExtra("Key", uploads.get(position).getKey());
                intent.putExtra("ArrayUrls", arrayList);
                startActivity(intent);
                finish();
            }
        });
    }
}