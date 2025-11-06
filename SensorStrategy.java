package com.example.wthapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SensorStrategy implements UpdateStrategy {
    private final Random rnd = new Random();

    @Override
    public WeatherData fetchWeatherFor(String city) {
        double temp = 10 + rnd.nextDouble() * 20;
        double hum = 40 + rnd.nextDouble() * 50;
        double press = 1000 + rnd.nextDouble() * 20;

        List<HourlyData> hourly = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            hourly.add(new HourlyData(i + ":00", temp + rnd.nextDouble() * 2 - 1, "Simulated"));
        }

        return new WeatherData(city, temp, hum, press, hourly, "Simulated sensor data");
    }

    @Override
    public String name() {
        return "Sensor Simulation";
    }
}

