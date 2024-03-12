package com.mobiwardrobe.mobiwardrobe.outfit;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobiwardrobe.mobiwardrobe.R;
import com.mobiwardrobe.mobiwardrobe.adapters.OutfitItemAdapter;
import com.mobiwardrobe.mobiwardrobe.model.Outfit;

import java.util.ArrayList;

public class OutfitDetailsActivity extends AppCompatActivity {
    private OutfitItemAdapter outfitItemAdapter;
    private RecyclerView recyclerView;

    private TextView outfitName, outfitWeather, outfitDescription;
    private ArrayList<String> imageUrls;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference, favoriteReference, favoriteListRef;
    private FirebaseUser firebaseUser;
    private String userID;

    private ArrayList<Outfit> outfits;

    private ImageButton deleteButton;
    private ImageButton backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_outfit_details);
        super.onCreate(savedInstanceState);

        outfitName = findViewById(R.id.tv_outfit_details_name);
        outfitWeather = findViewById(R.id.tv_details_outfit_weather);
        outfitDescription = findViewById(R.id.tv_details_description);
        imageUrls = new ArrayList<>();
        deleteButton = findViewById(R.id.ib_details_delete);
        backButton = findViewById(R.id.bt_details_back);

        recyclerView = findViewById(R.id.rv_outfit_details);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        String outfitKey = "";

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            outfitName.setText(bundle.getString("Name"));
            imageUrls = bundle.getStringArrayList("ImageUrls");
            outfitWeather.setText(bundle.getString("Weather"));
            outfitDescription.setText(bundle.getString("Description"));
            outfitKey = bundle.getString("Key");
        }

        outfitItemAdapter = new OutfitItemAdapter(this, imageUrls, false);
        recyclerView.setAdapter(outfitItemAdapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID).child("outfits");
        favoriteReference = database.getReference("users").child(userID).child("favorites");
        favoriteListRef = database.getReference("users").child(userID).child("favoriteList");

        outfits = new ArrayList<>();
        String finalOutfitKey = outfitKey;

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (databaseReference != null){
                    databaseReference.child(finalOutfitKey).removeValue();
                }
              if (favoriteReference != null) {
                  favoriteReference.child(finalOutfitKey).removeValue();
              }
              if (favoriteListRef != null) {
                  favoriteListRef.child(finalOutfitKey).removeValue();
              }
                Toast.makeText(OutfitDetailsActivity.this, "Комплект удалён", Toast.LENGTH_SHORT).show();
              finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
