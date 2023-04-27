package com.mobiwardrobe.mobiwardrobe.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mobiwardrobe.mobiwardrobe.R;
import com.mobiwardrobe.mobiwardrobe.adapters.WeatherForecastAdapter;
import com.mobiwardrobe.mobiwardrobe.api.Api;
import com.mobiwardrobe.mobiwardrobe.model.WeatherForecastResult;
import com.mobiwardrobe.mobiwardrobe.model.WeatherResult;
import com.mobiwardrobe.mobiwardrobe.retrofit.IOpenWeatherMap;
import com.mobiwardrobe.mobiwardrobe.retrofit.RetrofitClient;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class WeatherFragment extends Fragment {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    CoordinatorLayout coordinatorLayout;
    ImageView weatherImage;
    TextView cityName, humidity, sunrise, sunset, pressure,
            temperature, description, dateTime, wind, geoCoords;
    LinearLayout weatherPanel;
    ProgressBar pbWeather;

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;

    RecyclerView recyclerForecast;

    public WeatherFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Weather");
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.root_view);

        cityName = view.findViewById(R.id.tv_weather_city_name);
        humidity = view.findViewById(R.id.tv_weather_humidity);
        sunrise = view.findViewById(R.id.tv_weather_sunrise);
        sunset = view.findViewById(R.id.tv_weather_sunset);
        pressure = view.findViewById(R.id.tv_weather_pressure);
        temperature = view.findViewById(R.id.tv_weather_temperature);
        description = view.findViewById(R.id.tv_description);
        dateTime = view.findViewById(R.id.tv_date_time);
        wind = view.findViewById(R.id.tv_weather_wind);
        geoCoords = view.findViewById(R.id.tv_weather_coords);
        weatherImage = view.findViewById(R.id.iv_weather_image);

        weatherPanel = view.findViewById(R.id.weather_panel);
        pbWeather = view.findViewById(R.id.pb_weather);

        recyclerForecast = view.findViewById(R.id.rv_forecast);
        recyclerForecast.setHasFixedSize(true);
        recyclerForecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,
                false));

        //Request permission
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            buildLocationRequest();
                            buildLocationCallBack();

                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Snackbar.make(coordinatorLayout, "Permission Denied", Snackbar.LENGTH_LONG).show();
                    }
                }).check();

        return view;
    }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Api.current_location = locationResult.getLastLocation();

                getWeatherInformation();
                getForecastWeatherInformation();
            }
        };
    }


    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10.0f);
    }

    private void getWeatherInformation() {
        compositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(Api.current_location.getLatitude()),
                        String.valueOf(Api.current_location.getLongitude()),
                        Api.APP_ID,
                        "metric", "ru")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                               @Override
                               public void accept(WeatherResult weatherResult) throws Exception {
                                   Glide.with(getContext()).load(new StringBuilder("https://openweathermap.org/img/w/")
                                           .append(weatherResult.getWeather().get(0).getIcon())
                                           .append(".png").toString()).into(weatherImage);

                                   cityName.setText(weatherResult.getName());
                                   description.setText(new StringBuilder("Погода в: ")
                                           .append(weatherResult.getName()).toString());
                                   temperature.setText(new StringBuilder(String.valueOf(weatherResult.getMain()
                                           .getTemp())).append("°C").toString());
                                   dateTime.setText(Api.convertUnixToDate(weatherResult.getDt()));
                                   pressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain()
                                           .getPressure())).append(" hpa").toString());
                                   humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain()
                                           .getHumidity())).append(" %").toString());
                                   sunrise.setText(Api.convertUnixToHour(weatherResult.getSys().getSunrise()));
                                   sunset.setText(Api.convertUnixToHour(weatherResult.getSys().getSunset()));
                                   geoCoords.setText(new StringBuilder("[").append(weatherResult.getCoord()
                                           .toString()).append("]").toString());

                                   //Display panel
                                   weatherPanel.setVisibility(View.VISIBLE);
                                   pbWeather.setVisibility(View.GONE);
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                               }
                           }
                )
        );
    }

    private void getForecastWeatherInformation() {
        compositeDisposable.add(mService.getForecastWeatherByLatLng(String.valueOf(Api.current_location.getLatitude()),
                        String.valueOf(Api.current_location.getLongitude()),
                        Api.APP_ID,
                        "metric", "ru")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecastResult>() {
                               @Override
                               public void accept(WeatherForecastResult weatherForecastResult) throws Exception {
                                   displayForecastWeather(weatherForecastResult);
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.d("ERROR", "" + throwable.getMessage());
                               }
                           }
                )
        );
    }

    private void displayForecastWeather(WeatherForecastResult weatherForecastResult ) {
         WeatherForecastAdapter weatherForecastAdapter = new WeatherForecastAdapter(getContext(), weatherForecastResult);
        recyclerForecast.setAdapter(weatherForecastAdapter);
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
