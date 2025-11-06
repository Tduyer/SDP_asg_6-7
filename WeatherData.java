package com.example.kmtn;

public class WeatherData {
    private final double temperatureC;
    private final double humidity;
    private final double pressure;

    public WeatherData(double temperatureC, double humidity, double pressure) {
        this.temperatureC = temperatureC;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    public double getTemperatureC() { return temperatureC; }
    public double getHumidity() { return humidity; }
    public double getPressure() { return pressure; }

    public double getTemperatureF() { return temperatureC * 9.0 / 5.0 + 32.0; }

    @Override
    public String toString() {
        return String.format("Temp: %.2f°C (%.2f°F), Humidity: %.2f%%, Pressure: %.2f hPa",
                temperatureC, getTemperatureF(), humidity, pressure);
    }
}
