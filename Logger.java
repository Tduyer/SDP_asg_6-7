package com.example.kmtn;

import android.widget.TextView;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static Logger instance;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Logger() { }

    public static synchronized Logger getInstance() {
        if (instance == null) instance = new Logger();
        return instance;
    }

    public void log(TextView tv, String msg) {
        String line = String.format("[%s] %s\n", LocalDateTime.now().format(fmt), msg);
        Utils.appendToTextView(tv, line);
    }

    public static String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
