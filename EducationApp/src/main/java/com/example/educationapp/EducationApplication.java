package com.example.educationapp;

import android.app.Application;

/**
 * Created by kevin on 14-3-16.
 */
public class EducationApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BaseConfig.init(this);
    }
}
