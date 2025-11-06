package com.example.wthapp;

import android.content.Context;
import android.widget.Toast;

public class Utils {
    public static void toast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
}
