package com.example.wthapp;

import android.widget.TextView;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WeatherStation {
    private UpdateStrategy strategy;
    private final TextView logView;
    private final List<WeatherObserver> observers = new ArrayList<>();
    private final Map<String, WeatherData> dataMap = new ConcurrentHashMap<>();

    public WeatherStation(UpdateStrategy strategy, TextView logView) {
        this.strategy = strategy;
        this.logView = logView;
    }

    public void setStrategy(UpdateStrategy s) {
        strategy = s;
        Logger.log(logView, "Strategy set to " + s.name());
    }

    public void addObserver(WeatherObserver obs) {
        observers.add(obs);
        Logger.log(logView, "Observer added: " + obs.id());
    }

    public void removeObserver(WeatherObserver obs) {
        observers.remove(obs);
    }

    public void updateCity(String city) {
        WeatherData data = strategy.fetchWeatherFor(city);
        dataMap.put(city, data);
        Logger.log(logView, "Updated " + city + ": " + data.getTemperature());
        notifyObservers(data);
    }

    public WeatherData getData(String city) {
        return dataMap.get(city);
    }

    public void updateAll(List<String> cities) {
        for (String c : cities) updateCity(c);
    }

    private void notifyObservers(WeatherData data) {
        for (WeatherObserver obs : new ArrayList<>(observers)) {
            obs.onWeatherUpdated(data);
        }
    }
}

