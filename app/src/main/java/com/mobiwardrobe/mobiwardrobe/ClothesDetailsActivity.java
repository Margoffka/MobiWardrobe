package com.mobiwardrobe.mobiwardrobe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.mobiwardrobe.mobiwardrobe.clothes.ImageAdapter;
import com.mobiwardrobe.mobiwardrobe.upload.Upload;

import java.util.ArrayList;

public class ClothesDetailsActivity extends AppCompatActivity {
    private Context context;
    private ArrayList<Upload> uploads;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_details);

        // get intent data
        Intent intent = getIntent();

        // Selected image id
        int position = intent.getExtras().getInt("id");
        ImageAdapter imageAdapter = new ImageAdapter(context, uploads);

        ImageView imageView = (ImageView) findViewById(R.id.iv_clothes_details);
        Glide.with(this).load(getIntent().getStringExtra("id"))
                .into(imageView);
//        imageView.setImageResource(imageAdapter.getItem());
        }

}
