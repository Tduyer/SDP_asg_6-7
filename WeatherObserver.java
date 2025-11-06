package com.example.wthapp;

public interface WeatherObserver {
    void onWeatherUpdated(WeatherData data);
    String id();
}
