package com.example.educationapp;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by kevin on 14-3-16.
 */
public class BaseConfig {
    public static int width;
    public static int height;
    public static float density;
    public static int densityDpi;

    public static void init(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;
        density = metrics.density;
        densityDpi = metrics.densityDpi;
    }

    public static int dp2px(int dp) {
        return (int) (dp * density);
    }
}
