package com.example.wthapp;

public interface UpdateStrategy {
    WeatherData fetchWeatherFor(String city);
    String name();
}
