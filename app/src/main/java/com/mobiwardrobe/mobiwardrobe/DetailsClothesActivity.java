package com.mobiwardrobe.mobiwardrobe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailsClothesActivity extends AppCompatActivity {
    private ImageView detailsImage;
    private TextView detailsName;
    private TextView detailsType;
    private TextView detailsColor;
    private TextView detailsSeason;
    private TextView detailsWeather;
    private ImageButton deleteButton;
    private Button editButton;

    private FirebaseUser firebaseUser;
    private String userID;

    String key = "";
    String imageUrl = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_details);

        detailsName = findViewById(R.id.tv_details_name);
        detailsType = findViewById(R.id.tv_details_type);
        detailsColor = findViewById(R.id.tv_details_color);
        detailsSeason = findViewById(R.id.tv_details_season);
        detailsWeather = findViewById(R.id.tv_details_weather);
        deleteButton = findViewById(R.id.bt_details_delete);
        editButton = findViewById(R.id.bt_details_edit);
        detailsImage = findViewById(R.id.iv_details);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            detailsName.setText(bundle.getString("Name"));
            detailsType.setText(bundle.getString("Type"));
            detailsColor.setText(bundle.getString("Color"));
            detailsSeason.setText(bundle.getString("Season"));
            detailsWeather.setText(bundle.getString("Weather"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailsImage);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users")
                        .child(userID).child("clothes");
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(DetailsClothesActivity.this, "Удалено", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsClothesActivity.this, EditClothesActivity.class)
                        .putExtra("Name", detailsName.getText().toString())
                        .putExtra("Type", detailsType.getText().toString())
                        .putExtra("Color", detailsColor.getText().toString())
                        .putExtra("Season", detailsSeason.getText().toString())
                        .putExtra("Weather", detailsWeather.getText().toString())
                        .putExtra("Image", imageUrl)
                        .putExtra("Key", key);
                startActivity(intent);
            }
        });

    }
}
