package com.mobiwardrobe.mobiwardrobe.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Получение текущего времени
        Calendar currentTime = Calendar.getInstance();
        Log.d("MESSAGE RECEIVER", String.valueOf(currentTime));
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);

        // Получение данных о погоде на текущий момент (ваш код для получения данных о погоде)
        // ...

        // Определение заголовка и текста уведомления в зависимости от погоды и текущего времени
        String title = "Название уведомления";
        String message = "Текст уведомления";

        // Отправка уведомления
        NotificationHelper.sendNotification(context, title, message);
    }
}
