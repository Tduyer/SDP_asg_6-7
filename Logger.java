package com.example.wthapp;

import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger {
    private static final SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    public static void log(TextView tv, String msg) {
        String line = "[" + fmt.format(new Date()) + "] " + msg + "\n";
        tv.post(() -> tv.append(line));
    }
}
