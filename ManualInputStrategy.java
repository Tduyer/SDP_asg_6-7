package com.example.kmtn;

public class ManualInputStrategy implements UpdateStrategy {
    private final double temp;
    private final double hum;
    private final double press;

    public ManualInputStrategy(double temp, double hum, double press) {
        this.temp = temp;
        this.hum = hum;
        this.press = press;
    }

    @Override
    public WeatherData fetchWeatherData() {
        return new WeatherData(temp, hum, press);
    }

    @Override
    public String name() {
        return "Manual Input";
    }
}
