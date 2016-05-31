package com.example.fd.sampler;

import android.app.Application;

/**
 * Created by FD on 31.05.2016.
 */
public class DruMMaxApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Sampler app = Sampler.getSampler();
        Pattern initPattern = new Pattern(getApplicationContext());
        Track first = initPattern.addTrack("Track-1");
        first.makeHitActive(1,3,5,7);
        initPattern.addTrack("Track-2");
        initPattern.addTrack("Track-3");
        app.addPattern(initPattern);
        app.setPatternActive(initPattern);
    }
}
