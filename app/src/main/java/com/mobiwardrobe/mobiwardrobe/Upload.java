package com.mobiwardrobe.mobiwardrobe;

import com.google.firebase.database.Exclude;

public class Upload {
    public String mName;
    public String mImageUrl;
    public String mKey;
    public String mType;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String type, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        mName = name;
        mType = type;
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

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }
}
