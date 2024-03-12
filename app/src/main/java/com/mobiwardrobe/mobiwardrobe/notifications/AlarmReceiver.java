package com.mobiwardrobe.mobiwardrobe.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mobiwardrobe.mobiwardrobe.interfaces.WeatherCallback;
import com.mobiwardrobe.mobiwardrobe.interfaces.WeatherForecastCallback;
import com.mobiwardrobe.mobiwardrobe.model.weather.Weather;
import com.mobiwardrobe.mobiwardrobe.model.weather.WeatherForecastResult;
import com.mobiwardrobe.mobiwardrobe.model.weather.WeatherResult;
import com.mobiwardrobe.mobiwardrobe.weather.WeatherController;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver implements WeatherCallback, WeatherForecastCallback {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        // Получение текущего времени
        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        WeatherController weatherController = new WeatherController(context);
        weatherController.startLocationUpdates(this,
                this);

        // Проверка наличия разрешений на доступ к местоположению
    }

    @Override
    public void onWeatherDataReceived(WeatherResult weatherResult) {
        Weather weather = weatherResult.getWeather().get(0);
        String precipitation = weather.getMain();

        // Определение заголовка и текста уведомления в зависимости от погоды и текущего времени
        String title;
        String message;
        if (precipitation.equalsIgnoreCase("Небольшой дождь")) {
            title = "Идут дожди!";
            message = "Не забудьте добавить к образу зонтик.";
        } else if (precipitation.equalsIgnoreCase("Облачно с проясениями")) {
            title = "Сегодня облачно!";
            message = "Не забудьте подобрать комплект.";
        } else {
            title = "Сегодня пасмурно!";
            message = "Не забудьте подобрать комплект.";
        }

        // Отправка уведомления
        NotificationHelper.sendNotification(context, title, message);
    }

    @Override
    public void onWeatherDataError(Throwable throwable) {
        Log.e("WeatherController", "Error getting weather data: " + throwable.getMessage());
    }

    @Override
    public void onForecastWeatherDataReceived(WeatherForecastResult weatherForecastResult) {

    }

    @Override
    public void onForecastWeatherDataError(Throwable throwable) {

    }
}
