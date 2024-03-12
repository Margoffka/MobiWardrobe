package com.mobiwardrobe.mobiwardrobe.profile;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.mobiwardrobe.mobiwardrobe.model.User;
import com.mobiwardrobe.mobiwardrobe.notifications.AlarmHelper;

public class ProfileActivity extends AppCompatActivity {

    FirebaseUser user;
    DatabaseReference reference;
    String userID;
    FirebaseAuth mAuth;
    Button logoutBtn;
    ImageView setAlarm;
    private int selectedHour;
    private int selectedMinute;
    TextView watch;

    ImageButton backButton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("users");
        userID = user.getUid();
        mAuth = FirebaseAuth.getInstance();
        logoutBtn = findViewById(R.id.bt_logout);
        setAlarm = findViewById(R.id.iv_set_alarm);
        backButton = findViewById(R.id.bt_show_uploads);

        final TextView titleName = findViewById(R.id.tv_title_name);
        final TextView titleEmail = findViewById(R.id.tv_title_email);
        final TextView profileName = findViewById(R.id.tv_profile_name);
        final TextView profileEmail = findViewById(R.id.tv_profile_email);
        watch = findViewById(R.id.tv_watch);

        watch.setText("");

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

        setAlarm.setOnClickListener(v -> {
            showTimePickerDialog();
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
            }
        });
    }


    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selectedHour = hourOfDay;
                selectedMinute = minute;

                // Установка будильника
                AlarmHelper.setRepeatingAlarm(ProfileActivity.this, selectedHour, selectedMinute);
                watch.setText(selectedHour + ":" + selectedMinute);
                Log.d("MESSAGE", selectedHour + " " + selectedMinute);
            }
        }, 0, 0, true);

        timePickerDialog.show();
    }
}
