package com.mobiwardrobe.mobiwardrobe.interfaces;

import com.mobiwardrobe.mobiwardrobe.model.WeatherResult;

public interface WeatherCallback {
    void onWeatherDataReceived(WeatherResult weatherResult);

    void onWeatherDataError(Throwable throwable);
}
