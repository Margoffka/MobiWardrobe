package com.mobiwardrobe.mobiwardrobe.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobiwardrobe.mobiwardrobe.R;

public class WeatherFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Weather");
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        return view;
    }
}