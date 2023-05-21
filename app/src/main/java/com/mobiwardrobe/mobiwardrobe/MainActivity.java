package com.mobiwardrobe.mobiwardrobe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobiwardrobe.mobiwardrobe.calendar.CalendarFragment;
import com.mobiwardrobe.mobiwardrobe.clothes.ClothesFragment;
import com.mobiwardrobe.mobiwardrobe.outfit.FavoriteOutfitActivity;
import com.mobiwardrobe.mobiwardrobe.outfit.OutfitFragment;
import com.mobiwardrobe.mobiwardrobe.outfitupload.CreateOutfitActivity;
import com.mobiwardrobe.mobiwardrobe.profile.ProfileActivity;
import com.mobiwardrobe.mobiwardrobe.upload.UploadImageActivity;
import com.mobiwardrobe.mobiwardrobe.weather.WeatherFragment;


public class MainActivity extends AppCompatActivity {

    ImageView profileView;
    ImageView toFavorite;
    private FloatingActionButton fabAdd, addClothes, addOutfits;
    Float translationYaxis = 100f;
    Boolean menuOpen = false;
    OvershootInterpolator interpolator = new OvershootInterpolator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.item_clothes);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ClothesFragment()).commit();

        toFavorite = findViewById(R.id.iv_go_to_favorites);
        profileView = findViewById(R.id.iv_profile);

        showMenu();

        toFavorite.setOnClickListener(view -> {
             startActivity(new Intent(MainActivity.this, FavoriteOutfitActivity.class));
        });

        profileView.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        });
    }

    private void showMenu() {
        fabAdd = findViewById(R.id.fab_add);
        addClothes = findViewById(R.id.fab_add_clothes);
        addOutfits = findViewById(R.id.fab_add_outfit);

        addClothes.setAlpha(0f);
        addOutfits.setAlpha(0f);

        addClothes.setTranslationY(translationYaxis);
        addOutfits.setTranslationY(translationYaxis);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menuOpen) {
                    closeMenu();
                } else {
                    openMenu();
                }
            }
        });

        addClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UploadImageActivity.class));
                closeMenu();
            }
        });

        addOutfits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateOutfitActivity.class));
                closeMenu();
            }
        });
    }

    private void openMenu() {
        menuOpen = !menuOpen;
        fabAdd.setImageResource(R.drawable.ic_baseline_close_24);
        addClothes.animate().translationY(0f).alpha(1f).setInterpolator(interpolator)
                .setDuration(300).start();
        addOutfits.animate().translationY(0f).alpha(1f).setInterpolator(interpolator)
                .setDuration(300).start();
    }

    private void closeMenu() {
        menuOpen = !menuOpen;
        fabAdd.setImageResource(R.drawable.ic_baseline_add_24);
        addClothes.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator)
                .setDuration(300).start();
        addOutfits.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator)
                .setDuration(300).start();
    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.item_weather:
                selectedFragment = new WeatherFragment();
                break;
            case R.id.item_clothes:
                selectedFragment = new ClothesFragment();
                break;
            case R.id.item_outfit:
                selectedFragment = new OutfitFragment();
                break;
            case R.id.item_calendar:
                selectedFragment = new CalendarFragment();
                break;
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }
        return true;
    };

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

}


