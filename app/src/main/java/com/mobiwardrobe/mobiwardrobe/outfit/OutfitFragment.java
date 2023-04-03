package com.mobiwardrobe.mobiwardrobe.outfit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobiwardrobe.mobiwardrobe.R;

public class OutfitFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Outfit");
        View view = inflater.inflate(R.layout.fragment_outfit, container, false);
        return view;
    }
}
