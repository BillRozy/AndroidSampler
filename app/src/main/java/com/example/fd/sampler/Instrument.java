package com.example.fd.sampler;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;

// CLASS Instrument keeper of wav sound, and has method to play it
class Instrument {
    AssetManager assets;
    private float mVolume;
    //PROPERTIES
    private int id;
    private SoundPool pool;

    public Instrument(Context mCont, SoundPool pool, String Path) {
        assets = mCont.getAssets();
        this.pool = pool;
        this.id = loadSound(Path);
        mVolume = 1F;
    }

    public void setVolume(float level) {
        mVolume = level;
    }

    public void playSound() {
        try {
            long begin = System.currentTimeMillis();
            this.pool.play(id, mVolume, mVolume, 1, 0, 1);
            long end = System.currentTimeMillis();
            Log.d(Thread.currentThread().getName() + " Time delayed: ", "" + (end - begin));
        } catch (NullPointerException exc) {
            Log.d("Instrument isnt loaded!", exc.toString());
        }
    }

    private int loadSound(String fileName) {
        AssetFileDescriptor afd = null;
        try {
            afd = assets.openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FILE", "Cant open file from assets");
            try {
                return id = pool.load(fileName, 1);
            } catch (NullPointerException exc) {
                Log.d("Cant open from direct", exc.toString());
            }
        }
        return pool.load(afd, 1);
    }
}
