package com.mobiwardrobe.mobiwardrobe.calendar;

import static com.mobiwardrobe.mobiwardrobe.calendar.CalendarUtils.daysInMonthArray;
import static com.mobiwardrobe.mobiwardrobe.calendar.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;
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

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.mobiwardrobe.mobiwardrobe.R;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;

public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;

    private Button previousMonthBtn;
    private Button nextMonthBtn;
    private Button newEventBtn;

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
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
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

//    @Override
//    protected void onResume()
//    {
//        super.onResume();
//        setEventAdapter();
//    }

    private void setEventAdapter()
    {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

}
