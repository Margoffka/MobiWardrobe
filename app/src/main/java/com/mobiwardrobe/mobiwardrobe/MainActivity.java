package com.mobiwardrobe.mobiwardrobe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.mobiwardrobe.mobiwardrobe.authorization.LoginActivity;
import com.mobiwardrobe.mobiwardrobe.calendar.CalendarFragment;
import com.mobiwardrobe.mobiwardrobe.clothes.ClothesFragment;
import com.mobiwardrobe.mobiwardrobe.outfit.OutfitFragment;
import com.mobiwardrobe.mobiwardrobe.profile.ProfileActivity;
import com.mobiwardrobe.mobiwardrobe.upload.UploadImageActivity;
import com.mobiwardrobe.mobiwardrobe.weather.WeatherFragment;


public class MainActivity extends AppCompatActivity {

    private Button logoutBtn;
    private Button profileButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ClothesFragment()).commit();

        logoutBtn = findViewById(R.id.bt_logout);
        profileButton = findViewById(R.id.bt_profile);
        mAuth = FirebaseAuth.getInstance();

        //Change Activity to UploadImageActivity

        final FloatingActionButton addImageBtn = findViewById(R.id.bt_add);

        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UploadImageActivity.class));
            }
        });

        logoutBtn.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        profileButton.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        });
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

        assert selectedFragment != null;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        return true;
    };

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

}


