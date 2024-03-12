package com.mobiwardrobe.mobiwardrobe.model;

import java.util.ArrayList;

public class Outfit {
    private String outfitName;
    private String outfitKey;
    private String outfitDescription;
    private String outfitWeather;
    private ArrayList<String> imageUrls;

    public Outfit() {
    }

    public Outfit(String outfitName, String outfitWeather, String outfitDescription, ArrayList<String> imageUrls) {
        this.outfitName = outfitName;
        this.outfitWeather = outfitWeather;
        this.imageUrls = imageUrls;
        this.outfitDescription = outfitDescription;
        this.outfitKey = "";
    }

    public String getOutfitName() {
        return outfitName;
    }

    public void setOutfitName(String outfitName) {
        this.outfitName = outfitName;
    }

    public String getOutfitDescription() {
        return outfitDescription;
    }

    public void setOutfitDescription(String outfitDescription) {
        this.outfitDescription = outfitDescription;
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

    public String getOutfitWeather() {
        return outfitWeather;
    }

    public void setOutfitWeather(String outfitWeather) {
        this.outfitWeather = outfitWeather;
    }
}
