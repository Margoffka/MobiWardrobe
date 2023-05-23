package com.mobiwardrobe.mobiwardrobe.weather;

import android.location.Location;

import com.mobiwardrobe.mobiwardrobe.api.Api;
import com.mobiwardrobe.mobiwardrobe.interfaces.WeatherCallback;
import com.mobiwardrobe.mobiwardrobe.interfaces.WeatherForecastCallback;
import com.mobiwardrobe.mobiwardrobe.model.WeatherForecastResult;
import com.mobiwardrobe.mobiwardrobe.model.WeatherResult;
import com.mobiwardrobe.mobiwardrobe.retrofit.IOpenWeatherMap;
import com.mobiwardrobe.mobiwardrobe.retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class WeatherController{
        private IOpenWeatherMap mService;
        private CompositeDisposable compositeDisposable;

        public WeatherController() {
            compositeDisposable = new CompositeDisposable();
            Retrofit retrofit = RetrofitClient.getInstance();
            mService = retrofit.create(IOpenWeatherMap.class);
        }

        public void getWeatherInformation(Location location, WeatherCallback callback) {
            compositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(location.getLatitude()),
                            String.valueOf(location.getLongitude()),
                            Api.APP_ID,
                            "metric", "ru")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<WeatherResult>() {
                        @Override
                        public void accept(WeatherResult weatherResult) throws Exception {
                            callback.onWeatherDataReceived(weatherResult);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            callback.onWeatherDataError(throwable);
                        }
                    })
            );
        }

        public void getForecastWeatherInformation(Location location, WeatherForecastCallback callback) {
            compositeDisposable.add(mService.getForecastWeatherByLatLng(String.valueOf(location.getLatitude()),
                            String.valueOf(location.getLongitude()),
                            Api.APP_ID,
                            "metric", "ru")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<WeatherForecastResult>() {
                        @Override
                        public void accept(WeatherForecastResult weatherForecastResult) throws Exception {
                            callback.onForecastWeatherDataReceived(weatherForecastResult);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            callback.onForecastWeatherDataError(throwable);
                        }
                    })
            );
        }

        public void clearDisposables() {
            compositeDisposable.clear();
        }

}

