package com.example.kmtn;

import java.util.Random;

public class SensorUpdateStrategy implements UpdateStrategy {
    private final Random random = new Random();
    private double lastTemp = 20.0, lastHum = 50.0, lastPress = 1013.0;

    @Override
    public WeatherData fetchWeatherData() {
        lastTemp += (random.nextDouble() - 0.5) * 2.0;
        lastHum += (random.nextDouble() - 0.5) * 4.0;
        lastPress += (random.nextDouble() - 0.5) * 2.0;
        lastHum = Math.max(0.0, Math.min(100.0, lastHum));
        return new WeatherData(lastTemp, lastHum, lastPress);
    }

    @Override
    public String name() {
        return "Sensor Simulation";
    }
}
