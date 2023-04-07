package com.mobiwardrobe.mobiwardrobe.upload;

public class Upload {
    public String name;
    public String imageUrl;
    public String color;
    public String season;
    public String weather;
    public String type;
    public String key;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String color, String season, String weather, String type, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        this.name = name;
        this.imageUrl = imageUrl;
        this.color = color;
        this.season = season;
        this.weather = weather;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
