package com.example.kmtn;

import android.content.Context;
import android.widget.Toast;

public class ToastObserver implements WeatherObserver {
    private final String name;
    private final Context ctx;

    public ToastObserver(String name, Context ctx) {
        this.name = name;
        this.ctx = ctx;
    }

    @Override
    public void update(WeatherData data) {
        String msg = String.format("%s -> %s", name, data.toString());
        Toast.makeText(ctx, "ToastObserver: " + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getName() {
        return "Toast:" + name;
    }
}
