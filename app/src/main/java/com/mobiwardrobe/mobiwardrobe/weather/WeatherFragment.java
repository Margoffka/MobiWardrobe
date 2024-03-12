package com.mobiwardrobe.mobiwardrobe.weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mobiwardrobe.mobiwardrobe.R;
import com.mobiwardrobe.mobiwardrobe.adapters.OutfitItemAdapter;
import com.mobiwardrobe.mobiwardrobe.adapters.WeatherForecastAdapter;
import com.mobiwardrobe.mobiwardrobe.api.Api;
import com.mobiwardrobe.mobiwardrobe.interfaces.WeatherCallback;
import com.mobiwardrobe.mobiwardrobe.interfaces.WeatherForecastCallback;
import com.mobiwardrobe.mobiwardrobe.model.weather.WeatherForecastResult;
import com.mobiwardrobe.mobiwardrobe.model.weather.WeatherResult;
import com.mobiwardrobe.mobiwardrobe.model.Outfit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeatherFragment extends Fragment implements WeatherCallback, WeatherForecastCallback {
     private CoordinatorLayout coordinatorLayout;
    private ImageView weatherImage;
    private TextView cityName, humidity, sunrise, sunset, pressure,
            temperature, description, dateTime, wind, geoCoords, outfitTitle;
    private LinearLayout weatherPanel;
    private ProgressBar pbWeather;

    private RecyclerView recyclerForecast, outfitRecycler;
    private ArrayList<String> imageUrls;
    private ArrayList<Outfit> outfits;
    private OutfitItemAdapter outfitItemAdapter;

    private WeatherController weatherController;

    public WeatherFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Weather");
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.root_view);

        cityName = view.findViewById(R.id.tv_weather_city_name);
        humidity = view.findViewById(R.id.tv_weather_humidity);
//        sunrise = view.findViewById(R.id.tv_weather_sunrise);
//        sunset = view.findViewById(R.id.tv_weather_sunset);
//        pressure = view.findViewById(R.id.tv_weather_pressure);
        temperature = view.findViewById(R.id.tv_weather_temperature);
        description = view.findViewById(R.id.tv_description);
        dateTime = view.findViewById(R.id.tv_date_time);
//        wind = view.findViewById(R.id.tv_weather_wind);
//        geoCoords = view.findViewById(R.id.tv_weather_coords);
        weatherImage = view.findViewById(R.id.iv_weather_image);
        outfitTitle =view.findViewById(R.id.tv_weather_outfit_title);

        weatherPanel = view.findViewById(R.id.weather_panel);
        pbWeather = view.findViewById(R.id.pb_weather);

        recyclerForecast = view.findViewById(R.id.rv_forecast);
        recyclerForecast.setHasFixedSize(true);
        recyclerForecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,
                false));

        outfits = new ArrayList<>();
        imageUrls = new ArrayList<>();
        outfitRecycler = view.findViewById(R.id.rv_outfit_weather);
        outfitRecycler.setHasFixedSize(true);
        outfitRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,
                false));

        weatherController = new WeatherController(getContext());

        setOutfitRecycler(outfitRecycler, imageUrls);
        getFromDb();
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            weatherController.startLocationUpdates(WeatherFragment.this,
                                    WeatherFragment.this);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Snackbar.make(coordinatorLayout, "Permission Denied", Snackbar.LENGTH_LONG).show();
                    }
                }).check();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        weatherController.clearDisposables();
    }

    // Implement the WeatherCallback and WeatherForecastCallback methods
    @Override
    public void onWeatherDataReceived(WeatherResult weatherResult) {
        if (isAdded()) {
            // Handle received weather data
            Glide.with(getContext()).load(new StringBuilder("https://openweathermap.org/img/w/")
                    .append(weatherResult.getWeather().get(0).getIcon())
                    .append(".png").toString()).into(weatherImage);

            cityName.setText(weatherResult.getName());
            description.setText("Влажность: ");
            temperature.setText(new StringBuilder(String.valueOf(weatherResult.getMain()
                    .getTemp())).append("°C").toString());
            dateTime.setText(Api.convertUnixToDate(weatherResult.getDt()));
//        pressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain()
//                .getPressure())).append(" hpa").toString());
            humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain()
                    .getHumidity())).append(" %").toString());
//        sunrise.setText(Api.convertUnixToHour(weatherResult.getSys().getSunrise()));
//        sunset.setText(Api.convertUnixToHour(weatherResult.getSys().getSunset()));
//        geoCoords.setText(new StringBuilder("[").append(weatherResult.getCoord()
//                .toString()).append("]").toString());

            //Display panel
            weatherPanel.setVisibility(View.VISIBLE);
            pbWeather.setVisibility(View.GONE);
        }
    }

    @Override
    public void onWeatherDataError(Throwable throwable) {
        // Handle weather data error
        Toast.makeText(requireContext().getApplicationContext(), "" + throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onForecastWeatherDataReceived(WeatherForecastResult weatherForecastResult) {
        // Handle received forecast weather data
        WeatherForecastAdapter weatherForecastAdapter = new WeatherForecastAdapter(getContext(), weatherForecastResult);
        recyclerForecast.setAdapter(weatherForecastAdapter);
    }

    @Override
    public void onForecastWeatherDataError(Throwable throwable) {
        // Handle forecast weather data error
        Log.d("ERROR", "" + throwable.getMessage());
    }

    private void setOutfitRecycler(RecyclerView recyclerView, ArrayList<String> imageUrls) {
        outfitItemAdapter = new OutfitItemAdapter(getContext(), imageUrls, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(outfitItemAdapter);
    }

    private void getFromDb() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = firebaseUser.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("users").child(userID).child("outfits");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                outfits.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Outfit outfit = dataSnapshot.getValue(Outfit.class);
                    outfit.setOutfitKey(dataSnapshot.getKey());
                    outfits.add(outfit);
                }

                // Выбираем случайный комплект из списка
                if (outfits.size() > 0) {
                    int randomIndex = new Random().nextInt(outfits.size());
                    Outfit randomOutfit = outfits.get(randomIndex);

                    outfitTitle.setText(randomOutfit.getOutfitName());
                    imageUrls.clear();
                    imageUrls.addAll(randomOutfit.getImageUrls());
                    outfitItemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}

