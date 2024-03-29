package com.mobiwardrobe.mobiwardrobe.calendar;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobiwardrobe.mobiwardrobe.R;
import com.mobiwardrobe.mobiwardrobe.adapters.OutfitsFragmentAdapter;
import com.mobiwardrobe.mobiwardrobe.interfaces.OutfitClickListener;
import com.mobiwardrobe.mobiwardrobe.model.Event;
import com.mobiwardrobe.mobiwardrobe.model.Outfit;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.util.ArrayList;


public class EventEditActivity extends AppCompatActivity implements OutfitClickListener {
    private EditText eventNameET;
    private TextView eventDateTV;
    private Button saveEvent;

    private LocalTime time;
    private LocalDate date;

    FirebaseDatabase database;
    DatabaseReference eventReference, outfitReference;

    private FirebaseUser firebaseUser;
    private String userID;

    private OutfitsFragmentAdapter outfitsFragmentAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Outfit> outfits;
    ValueEventListener valueEventListener;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        time = LocalTime.now();
        date = CalendarUtils.selectedDate;
        eventDateTV.setText(CalendarUtils.formattedDate(date));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        database = FirebaseDatabase.getInstance();
        eventReference = database.getReference("users").child(userID).child("calendar");
        outfitReference = database.getReference("users").child(userID).child("outfits");

        valueEventListener = outfitReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                outfits.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Outfit outfit = dataSnapshot.getValue(Outfit.class);
                    outfit.setOutfitKey(dataSnapshot.getKey());
                    outfits.add(outfit);
                }
                outfitsFragmentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void initWidgets() {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        saveEvent = findViewById(R.id.bt_event_save);

        outfits = new ArrayList<>();

        recyclerView = findViewById(R.id.rv_edit_event);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        outfitsFragmentAdapter = new OutfitsFragmentAdapter(this, outfits);
        recyclerView.setAdapter(outfitsFragmentAdapter);
        outfitsFragmentAdapter.setOutfitClickListener(EventEditActivity.this);
    }

    public void saveEventAction(Outfit eventOutfit, String outfitKey) {
        String eventName = eventNameET.getText().toString().trim();
        String dateTxt = date.toString();

        Event newEvent = new Event(eventName, dateTxt);
        eventReference.child(dateTxt).setValue(newEvent);
        eventReference.child(dateTxt).child(outfitKey).setValue(eventOutfit);
        finish();
    }

    private int previousPosition = RecyclerView.NO_POSITION;
    private View previousView = null;

    @Override
    public void onOutfitClick(int position, View view) {

        if (previousView != null) {
            Drawable previousDrawable = ContextCompat.getDrawable(EventEditActivity.this, R.drawable.grey_border_background);
            previousView.setBackground(previousDrawable);
        }

        String outfitKey = outfits.get(position).getOutfitKey();
        String name = outfits.get(position).getOutfitName();
        ArrayList<String> imageUrls = outfits.get(position).getImageUrls();

        Outfit eventOutfit = outfits.get(position);
        eventOutfit.setOutfitKey(outfitKey);
        eventOutfit.setOutfitName(name);
        eventOutfit.setImageUrls(imageUrls);

        Drawable drawable = ContextCompat.getDrawable(EventEditActivity.this, R.drawable.white_background);
        view.setBackground(drawable);

        previousPosition = position;
        previousView = view;

        saveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEventAction(eventOutfit, outfitKey);
            }
        });
    }

    @Override
    public void onDeleteClick(int position) {

    }
}
