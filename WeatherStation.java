package com.example.kmtn;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WeatherStation {
    private final List<WeatherObserver> observers = new ArrayList<>();
    private UpdateStrategy strategy;
    private WeatherData currentData;
    private final TextView logView;

    public WeatherStation(UpdateStrategy initialStrategy, TextView logView) {
        this.strategy = initialStrategy;
        this.logView = logView;
        Logger.getInstance().log(logView, "WeatherStation created. Strategy: " + (strategy != null ? strategy.name() : "none"));
    }

    public void setStrategy(UpdateStrategy strategy) {
        Logger.getInstance().log(logView, "Strategy changed to: " + strategy.name());
        this.strategy = strategy;
    }

    public UpdateStrategy getStrategy() { return strategy; }

    public void addObserver(WeatherObserver o) {
        observers.add(o);
        Logger.getInstance().log(logView, "Observer added: " + o.getName());
    }

    public void removeObserver(WeatherObserver o) {
        observers.remove(o);
        Logger.getInstance().log(logView, "Observer removed: " + o.getName());
    }

    public List<WeatherObserver> getObserversSnapshot() {
        return new ArrayList<>(observers);
    }

    public void updateWeather() {
        if (strategy == null) {
            Logger.getInstance().log(logView, "No strategy set — skipping update.");
            return;
        }
        WeatherData newData = strategy.fetchWeatherData();
        boolean changed = hasChanged(newData);
        currentData = newData;
        Logger.getInstance().log(logView, "Weather updated via " + strategy.name() + ": " + currentData.toString());
        if (changed) notifyObservers();
        else Logger.getInstance().log(logView, "No significant change — observers not notified.");
    }

    private boolean hasChanged(WeatherData newData) {
        if (currentData == null) return true;
        return Math.abs(currentData.getTemperatureC() - newData.getTemperatureC()) > 0.01
                || Math.abs(currentData.getHumidity() - newData.getHumidity()) > 0.01
                || Math.abs(currentData.getPressure() - newData.getPressure()) > 0.01;
    }

    private void notifyObservers() {
        Logger.getInstance().log(logView, "Notifying " + observers.size() + " observers.");
        for (WeatherObserver o : new ArrayList<>(observers)) {
            try { o.update(currentData); }
            catch (Exception e) { Logger.getInstance().log(logView, "Error notifying " + o.getName() + ": " + e.getMessage()); }
        }
    }
}
