package com.example.kmtn;

public class ScheduledUpdateStrategy implements UpdateStrategy {
    private final SensorUpdateStrategy sensor = new SensorUpdateStrategy();

    @Override
    public WeatherData fetchWeatherData() {
        return sensor.fetchWeatherData();
    }

    @Override
    public String name() {
        return "Scheduled Auto";
    }
}
