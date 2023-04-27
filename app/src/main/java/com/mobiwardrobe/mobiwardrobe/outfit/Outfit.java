package com.mobiwardrobe.mobiwardrobe.outfit;

import java.util.ArrayList;

public class Outfit {
    private String outfitName;
    private String outfitKey;
    private ArrayList<String> imageUrls;

    public Outfit() {
    }

    public Outfit(String outfitName, ArrayList<String> imageUrls) {
        this.outfitName = outfitName;
        this.imageUrls = imageUrls;
        this.outfitKey = "";
    }

    public String getOutfitName() {
        return outfitName;
    }

    public void setOutfitName(String outfitName) {
        this.outfitName = outfitName;
    }

    public String getOutfitKey() {
        return outfitKey;
    }

    public void setOutfitKey(String outfitKey) {
        this.outfitKey = outfitKey;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
