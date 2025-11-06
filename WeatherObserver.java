package com.example.kmtn;

public interface WeatherObserver {
    void update(WeatherData data);
    String getName();
}
