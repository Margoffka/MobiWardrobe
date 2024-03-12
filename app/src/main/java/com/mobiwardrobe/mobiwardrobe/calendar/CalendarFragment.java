package com.mobiwardrobe.mobiwardrobe.calendar;

import static com.mobiwardrobe.mobiwardrobe.calendar.CalendarUtils.daysInMonthArray;
import static com.mobiwardrobe.mobiwardrobe.calendar.CalendarUtils.monthYearFromDate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.mobiwardrobe.mobiwardrobe.R;
import com.mobiwardrobe.mobiwardrobe.model.Event;
import com.mobiwardrobe.mobiwardrobe.model.Outfit;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;

public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;

    private Button previousMonthBtn;
    private Button nextMonthBtn;
    private Button newEventBtn;

    FirebaseDatabase database;
    DatabaseReference reference, outfitReference;

    private FirebaseUser firebaseUser;
    private String userID;

    ValueEventListener valueEventListener, outfitListener;
    private ArrayList<Outfit> outfits;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Calendar");
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        AndroidThreeTen.init(requireContext());

        //init widgets
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);

        eventListView = view.findViewById(R.id.eventListView);
        CalendarUtils.selectedDate = LocalDate.now();

        previousMonthBtn = view.findViewById(R.id.bt_previous_month);
        nextMonthBtn = view.findViewById(R.id.bt_next_month);
        newEventBtn = view.findViewById(R.id.bt_new_event);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        database = FirebaseDatabase.getInstance();

        setMonthView();

        //previous month action
        previousMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
                setMonthView();
            }
        });
        //next month action
        nextMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
                setMonthView();
            }
        });
        //add new event
        newEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), EventEditActivity.class));
            }
        });

        return view;
    }


    private void setMonthView() {
        String monthYear = monthYearFromDate(CalendarUtils.selectedDate);
        String capitalizedMonthYear = monthYear.substring(0, 1).toUpperCase() + monthYear.substring(1);
        monthYearText.setText(capitalizedMonthYear);
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();
    }


    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter() {

        outfits = new ArrayList<>();
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);

        EventAdapter eventAdapter = new EventAdapter(getContext(), dailyEvents, outfits);
        eventListView.setAdapter(eventAdapter);

        outfitReference = FirebaseDatabase.getInstance().getReference("users").child(userID).child("outfits");
        outfitListener = outfitReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                outfits.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Outfit outfit = dataSnapshot.getValue(Outfit.class);
                    outfit.setOutfitKey(dataSnapshot.getKey());
                    outfits.add(outfit);
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        reference = database.getReference("users").child(userID).child("calendar");
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("DataSnapshot", "DataSnapshot: " + snapshot);
                Event.eventsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Event event = dataSnapshot.getValue(Event.class);
                    Event.eventsList.add(event);
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
