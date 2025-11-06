package com.example.kmtn;

import android.widget.TextView;

public class UIDisplayObserver implements WeatherObserver {
    private final String name;
    private final TextView tv;

    public UIDisplayObserver(String name, TextView tv) {
        this.name = name;
        this.tv = tv;
    }

    @Override
    public void update(WeatherData data) {
        // Append to UI log
        String line = String.format("[%s] UI Display %s received: %s\n", Utils.now(), name, data.toString());
        Utils.appendToTextView(tv, line);
    }

    @Override
    public String getName() {
        return "UIDisplay:" + name;
    }
}
