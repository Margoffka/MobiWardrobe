package com.mobiwardrobe.mobiwardrobe;

import com.google.firebase.database.Exclude;

public class Upload {
    public String mName;
    public String mImageUrl;
    public String mColor;
    public String mSeason;
    public String mWeather;
    public String mType;
    public String mKey;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String type, String color, String season, String weather, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        mName = name;
        mType = type;
        mColor = color;
        mSeason = season;
        mWeather = weather;
        mImageUrl = imageUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public String getSeason() {
        return mSeason;
    }

    public void setSeason(String season) {
        mSeason = season;
    }

    public String getWeather() {
        return mWeather;
    }

    public void setWeather(String weather) {
        mWeather = weather;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }
}
