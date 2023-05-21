package com.mobiwardrobe.mobiwardrobe.interfaces;

import android.view.View;

public interface OutfitClickListener {
    void onOutfitClick(int position, View view);
    void onDeleteClick(int position);
}
