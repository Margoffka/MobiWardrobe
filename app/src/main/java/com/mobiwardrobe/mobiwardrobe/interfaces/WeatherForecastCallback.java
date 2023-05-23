package com.mobiwardrobe.mobiwardrobe.interfaces;

import com.mobiwardrobe.mobiwardrobe.model.WeatherForecastResult;

public interface WeatherForecastCallback {
    void onForecastWeatherDataReceived(WeatherForecastResult weatherForecastResult);
    void onForecastWeatherDataError(Throwable throwable);
}