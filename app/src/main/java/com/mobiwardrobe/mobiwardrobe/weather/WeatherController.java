package com.mobiwardrobe.mobiwardrobe.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.mobiwardrobe.mobiwardrobe.api.Api;
import com.mobiwardrobe.mobiwardrobe.interfaces.WeatherCallback;
import com.mobiwardrobe.mobiwardrobe.interfaces.WeatherForecastCallback;
import com.mobiwardrobe.mobiwardrobe.model.weather.WeatherForecastResult;
import com.mobiwardrobe.mobiwardrobe.model.weather.WeatherResult;
import com.mobiwardrobe.mobiwardrobe.retrofit.IOpenWeatherMap;
import com.mobiwardrobe.mobiwardrobe.retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class WeatherController {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    private IOpenWeatherMap mService;
    private CompositeDisposable compositeDisposable;

    private Context context;

    public WeatherController(Context context) {
        this.context = context;
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }

    public void startLocationUpdates(WeatherCallback weatherCallback, WeatherForecastCallback forecastCallback) {
        buildLocationRequest();
        buildLocationCallBack(weatherCallback, forecastCallback);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
    }

        private void buildLocationCallBack(WeatherCallback callback, WeatherForecastCallback forecastCallback) {
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null) {
                        Location location = locationResult.getLastLocation();
                        // Pass the location to the fragment
                        getWeatherInformation(location, callback);
                        getForecastWeatherInformation(location, forecastCallback);
                    }
                }
            };
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

