package com.example.fd.sampler;

import android.app.Application;

/**
 * Created by FD on 31.05.2016.
 */
public class DruMMaxApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
       Sampler myApp = Sampler.getSampler();

    }
}
