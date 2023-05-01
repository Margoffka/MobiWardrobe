package com.mobiwardrobe.mobiwardrobe.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobiwardrobe.mobiwardrobe.MainActivity;
import com.mobiwardrobe.mobiwardrobe.R;
import com.mobiwardrobe.mobiwardrobe.authorization.LoginActivity;
import com.mobiwardrobe.mobiwardrobe.authorization.User;

public class ProfileActivity extends AppCompatActivity {

    FirebaseUser user;
    DatabaseReference reference;
    String userID;
    FirebaseAuth mAuth;
    Button logoutBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("users");
        userID = user.getUid();
        mAuth = FirebaseAuth.getInstance();
        logoutBtn = findViewById(R.id.bt_logout);

        final TextView titleName = (TextView) findViewById(R.id.tv_title_name);
        final TextView titleEmail = (TextView) findViewById(R.id.tv_title_email);
        final TextView profileName = (TextView) findViewById(R.id.tv_profile_name);
        final TextView profileEmail = (TextView) findViewById(R.id.tv_profile_email);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String firstname = userProfile.firstname;
                    String email = userProfile.email;

                    titleName.setText(firstname);
                    titleEmail.setText(email);
                    profileName.setText(firstname);
                    profileEmail.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Что-то пошло не так!", Toast.LENGTH_LONG).show();
            }
        });

        logoutBtn.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        });
    }
}
