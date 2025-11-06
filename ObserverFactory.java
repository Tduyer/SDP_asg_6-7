package com.example.kmtn;

import android.content.Context;
import android.widget.TextView;

public class ObserverFactory {
    public static WeatherObserver create(String type, String id, Context ctx, TextView tvForUI) {
        switch (type.toLowerCase()) {
            case "ui":
            case "display":
                return new UIDisplayObserver(id, tvForUI);
            case "toast":
                return new ToastObserver(id, ctx);
            default:
                throw new IllegalArgumentException("Unknown observer type: " + type);
        }
    }
}
