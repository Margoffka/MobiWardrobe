package com.mobiwardrobe.mobiwardrobe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobiwardrobe.mobiwardrobe.R;
import com.mobiwardrobe.mobiwardrobe.api.Api;
import com.mobiwardrobe.mobiwardrobe.model.weather.WeatherForecastResult;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.ForecastHolder> {
    Context context;
    WeatherForecastResult weatherForecastResult;

    public WeatherForecastAdapter(Context context, WeatherForecastResult weatherForecastResult) {
        this.context = context;
        this.weatherForecastResult = weatherForecastResult;
    }

    @NonNull
    @Override
    public ForecastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_weather_forecast, parent, false);
        return new ForecastHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastHolder holder, int position) {
        Glide.with(context).load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(weatherForecastResult.list.get(position).weather.get(0).getIcon())
                .append(".png").toString()).into(holder.imageForecast);
        holder.forecastDateTime.setText(new StringBuilder(Api.convertUnixToDate(weatherForecastResult
                .list.get(position).dt)));

        holder.forecastDescription.setText(new StringBuilder(weatherForecastResult.list.get(position)
                .weather.get(0).getDescription()));

        holder.forecastTemperature.setText(new StringBuilder(String.valueOf(weatherForecastResult.list.get(position)
                .main.getTemp())).append("°C"));
    }

    @Override
    public int getItemCount() {
        return weatherForecastResult.list.size();
    }

    public class ForecastHolder extends RecyclerView.ViewHolder{
        TextView forecastDateTime, forecastDescription, forecastTemperature;
        ImageView imageForecast;

        public ForecastHolder(@NonNull View itemView) {
            super(itemView);

            imageForecast = itemView.findViewById(R.id.iv_weather_image);
            forecastDateTime = itemView.findViewById(R.id.tv_forecast_date);
            forecastDescription = itemView.findViewById(R.id.tv_forecast_description);
            forecastTemperature = itemView.findViewById(R.id.tv_weather_temperature);
        }
    }
}
