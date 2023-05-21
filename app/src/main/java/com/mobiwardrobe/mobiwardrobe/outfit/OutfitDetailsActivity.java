package com.mobiwardrobe.mobiwardrobe.outfit;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiwardrobe.mobiwardrobe.R;
import com.mobiwardrobe.mobiwardrobe.adapters.OutfitItemAdapter;

import java.util.ArrayList;

public class OutfitDetailsActivity extends AppCompatActivity {
    private OutfitItemAdapter outfitItemAdapter;
    private RecyclerView recyclerView;

    private TextView outfitName;
    private ArrayList<String> imageUrls;
    private ArrayList<Uri> uris;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_outfit_details);
        super.onCreate(savedInstanceState);

        outfitName = findViewById(R.id.tv_outfit_details_name);
        imageUrls = new ArrayList<>();

        recyclerView = findViewById(R.id.rv_outfit_details);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            outfitName.setText(bundle.getString("Name"));
            imageUrls = bundle.getStringArrayList("ImageUrls");
        }

        outfitItemAdapter = new OutfitItemAdapter(this, imageUrls, false);
        recyclerView.setAdapter(outfitItemAdapter);
    }
}
