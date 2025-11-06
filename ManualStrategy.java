package com.example.wthapp;

import java.util.ArrayList;
import java.util.List;

public class ManualStrategy implements UpdateStrategy {
    private final double temp;
    private final double hum;
    private final double press;

    public ManualStrategy(double temp, double hum, double press) {
        this.temp = temp;
        this.hum = hum;
        this.press = press;
    }

    @Override
    public WeatherData fetchWeatherFor(String city) {
        List<HourlyData> hourly = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            hourly.add(new HourlyData(i + ":00", temp + (i - 6) * 0.2, "Manual input"));
        }
        return new WeatherData(city, temp, hum, press, hourly, "Manual input");
    }

    @Override
    public String name() {
        return "Manual";
    }
}
