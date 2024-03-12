package com.mobiwardrobe.mobiwardrobe.interfaces;

import com.mobiwardrobe.mobiwardrobe.model.weather.WeatherResult;

public interface WeatherCallback {
    void onWeatherDataReceived(WeatherResult weatherResult);

    void onWeatherDataError(Throwable throwable);
}
