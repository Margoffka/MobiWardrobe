package com.mobiwardrobe.mobiwardrobe.retrofit;

import com.mobiwardrobe.mobiwardrobe.model.weather.WeatherForecastResult;
import com.mobiwardrobe.mobiwardrobe.model.weather.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {
    @GET("weather")
    Observable<WeatherResult> getWeatherByLatLng(@Query("lat") String lat,
                                                 @Query("lon") String lng,
                                                 @Query("appid") String appid,
                                                 @Query("units") String unit,
                                                 @Query("lang") String lang);

    @GET("forecast")
    Observable<WeatherForecastResult> getForecastWeatherByLatLng(@Query("lat") String lat,
                                                                 @Query("lon") String lng,
                                                                 @Query("appid") String appid,
                                                                 @Query("units") String unit,
                                                                 @Query("lang") String lang);
}
