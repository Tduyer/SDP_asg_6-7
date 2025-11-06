package com.example.wthapp;

import java.util.List;

public class WeatherData {
    private final String city;
    private final double temperature;
    private final double humidity;
    private final double pressure;
    private final List<HourlyData> hourly;
    private final String description;

    public WeatherData(String city, double temperature, double humidity, double pressure,
                       List<HourlyData> hourly, String description) {
        this.city = city;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.hourly = hourly;
        this.description = description;
    }

    public String getCity() { return city; }
    public double getTemperature() { return temperature; }
    public double getHumidity() { return humidity; }
    public double getPressure() { return pressure; }
    public List<HourlyData> getHourly() { return hourly; }
    public String getDescription() { return description; }
}
