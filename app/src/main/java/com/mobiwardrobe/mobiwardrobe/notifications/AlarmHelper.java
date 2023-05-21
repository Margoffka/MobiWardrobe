package com.mobiwardrobe.mobiwardrobe.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmHelper {
    private static final int ALARM_REQUEST_CODE = 10;

    public static void setRepeatingAlarm(Context context, int hour, int minute) {
        // Получение Calendar-объекта с выбранным временем
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        // Проверка, прошло ли выбранное время на текущий день
        Calendar currentTime = Calendar.getInstance();
        if (calendar.compareTo(currentTime) <= 0) {
            // Если выбранное время уже прошло на текущий день, добавляем один день
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Создание намерения для запуска BroadcastReceiver
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Получение AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Установка повторяющегося будильника с интервалом в 24 часа
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
