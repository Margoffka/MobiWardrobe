package com.mobiwardrobe.mobiwardrobe.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.mobiwardrobe.mobiwardrobe.api.Api;
import com.mobiwardrobe.mobiwardrobe.interfaces.WeatherCallback;
import com.mobiwardrobe.mobiwardrobe.model.Weather;
import com.mobiwardrobe.mobiwardrobe.model.WeatherResult;
import com.mobiwardrobe.mobiwardrobe.weather.WeatherController;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Получение текущего времени
        Calendar currentTime = Calendar.getInstance();
        Log.d("MESSAGE RECEIVER", String.valueOf(currentTime));
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);

        // Получение данных о погоде на текущий момент (ваш код для получения данных о погоде)
        WeatherController weatherController = new WeatherController();
        Location currentLocation = Api.current_location; // Получение текущей локации
        weatherController.getWeatherInformation(currentLocation, new WeatherCallback() {
            @Override
            public void onWeatherDataReceived(WeatherResult weatherResult) {
                // Получение информации о погоде
                Weather weather = weatherResult.getWeather().get(0);
                String precipitation = weather.getMain();

                // Определение заголовка и текста уведомления в зависимости от погоды и текущего времени
                String title = "Название уведомления";
                String message = "Текст уведомления";
                if (precipitation.equals("Rain")) {
                    title = "Идут дожди!";
                    message = "Не забудьте взять зонт с собой.";
                } else if (precipitation.equals("Snow")) {
                    title = "Идет снег!";
                    message = "Будьте осторожны на дороге.";
                } else {
                    title = "Не нашли";
                    message = "Мы ваши погодные условия";
                }

                // Отправка уведомления
                NotificationHelper.sendNotification(context, title, message);
            }

            @Override
            public void onWeatherDataError(Throwable throwable) {
                Log.e("WeatherController", "Error getting weather data: " + throwable.getMessage());
            }
        });
    }
}
