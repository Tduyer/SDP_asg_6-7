package com.example.kmtn;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

public class Utils {
    private static final Handler MAIN = new Handler(Looper.getMainLooper());

    public static void appendToTextView(TextView tv, String text) {
        MAIN.post(() -> {
            tv.append(text);
            // auto-scroll: set selection if it's EditText; for TextView, we can request layout
        });
    }

    public static String now() {
        return Logger.now();
    }
}
