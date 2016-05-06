package com.example.fd.sampler;

/**
 * Created by FD on 03.05.2016.
 */
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;


import java.io.IOException;

// CLASS Instrument keeper of wav sound, and have method to play it
class Instrument{

    public Instrument(Context mCont,SoundPool pool, String Path) {
        assets = mCont.getAssets();
        this.pool = pool;
        this.id = loadSound(Path);
    }


    public void playSound()
    {
        try{
            long begin = System.currentTimeMillis();
            this.pool.play(id,1,1,1,0,1); //Поехали!!!
            Thread.sleep(Sampler.getSampler().getDelay());
            //this.pool.stop(id);
            long end = System.currentTimeMillis();
            Log.d(Thread.currentThread().getName() + " Time delayed: ","" + (end-begin));
        }  catch (InterruptedException exc) {Thread.currentThread().interrupt();}
    }


    private int loadSound(String fileName) {
        AssetFileDescriptor afd = null;
        try {
            afd = assets.openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FILE","Cant open file");
            return -1;
        }
        return pool.load(afd, 1);
    }


    //PROPERTIES
    private int id;
    AssetManager assets;
    private SoundPool pool;
}