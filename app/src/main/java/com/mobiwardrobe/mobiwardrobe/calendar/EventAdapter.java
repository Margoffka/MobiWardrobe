package com.mobiwardrobe.mobiwardrobe.calendar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.mobiwardrobe.mobiwardrobe.adapters.OutfitItemAdapter;
import com.mobiwardrobe.mobiwardrobe.outfit.Outfit;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {

    private ArrayList<Outfit> outfits, calendarOutfits;
    FirebaseDatabase database;
    DatabaseReference reference, outfitReference;

    private FirebaseUser firebaseUser;
    private String userID;

    ValueEventListener valueEventListener, outfitListener;

    public EventAdapter(@NonNull Context context, List<Event> events, ArrayList<Outfit> outfits) {
        super(context, 0, events);
        this.outfits = outfits;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Event
        TextView eventTitle, outfitTitle;
        RecyclerView eventRecycler;
        ImageView deleteEvent, changeEvent;

        calendarOutfits = new ArrayList<>();

        Event event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);

        eventTitle = convertView.findViewById(R.id.tv_event_title);
        eventRecycler = convertView.findViewById(R.id.rv_event_items);

        eventTitle.setText(event.getName() + " " + event.getTime());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users").child(userID).child("calendar").child(event.getDate());
        String referencePath = reference.toString();
        Log.d("Reference Path", referencePath);
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                calendarOutfits.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (Outfit outfit : outfits) {
                        String outfitKey = outfit.getOutfitKey();
                        String snapshotKey = dataSnapshot.getKey();
                        if (snapshotKey.equals(outfitKey)) {
                            Outfit eventOutfit = dataSnapshot.getValue(Outfit.class);
                            eventOutfit.setOutfitKey(dataSnapshot.getKey());
                            calendarOutfits.add(eventOutfit);
                        }
                    }
                }

                Log.d("DataSnapshot", "CalendarSnapshot: " + calendarOutfits.size());
                if (!calendarOutfits.isEmpty()) {
                    ArrayList<String> imageUrls = calendarOutfits.get(position).getImageUrls();
                    OutfitItemAdapter outfitItemAdapter = new OutfitItemAdapter(getContext(), imageUrls, true);
                    eventRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                    eventRecycler.setAdapter(outfitItemAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return convertView;
    }
}