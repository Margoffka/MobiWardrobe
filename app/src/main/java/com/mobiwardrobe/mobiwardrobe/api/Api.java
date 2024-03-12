package com.mobiwardrobe.mobiwardrobe.api;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Api {
     public static final String APP_ID = "46ad59ca92ba76f25d277f314a48596f";
      public static Location current_location = null;

     public static String convertUnixToDate(long dt) {
          Date date = new Date(dt*1000L);
          SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm EEE MM yyyy", new Locale("ru"));
          String formatted =  simpleDateFormat.format(date);
          return formatted;
     }

     public static String convertUnixToHour(long dt) {
          Date date = new Date(dt*1000L);
          SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
          String formatted =  simpleDateFormat.format(date);
          return formatted;
     }
}
