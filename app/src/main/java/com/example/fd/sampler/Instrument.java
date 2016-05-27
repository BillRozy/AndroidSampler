package com.example.fd.sampler;

/**
 * Created by FD on 03.05.2016.
 */
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;
import android.util.Log;


import java.io.IOException;

// CLASS Instrument keeper of wav sound, and have method to play it
class Instrument{

    private float mVolume = 1F;
    public Instrument(Context mCont,SoundPool pool, String Path) {
        assets = mCont.getAssets();
        this.pool = pool;
        this.id = loadSound(Path);
    }

    public void setVolume(float level){
        mVolume = level;
    }

    public float getVolume(){
        return mVolume;
    }


    public void playSound()
    {
     //  try{
            long begin = System.currentTimeMillis();
            this.pool.play(id,mVolume,mVolume,1,0,1); //Поехали!!!
          // TimeUnit.MILLISECONDS.sleep(Sampler.getSampler().getDelay());
            //this.pool.pause(id);
            long end = System.currentTimeMillis();
            Log.d(Thread.currentThread().getName() + " Time delayed: ","" + (end-begin));
     //  }  catch (InterruptedException exc) {}
    }


    private int loadSound(String fileName) {
        AssetFileDescriptor afd = null;
        try {
            afd = assets.openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FILE","Cant open file from assets");
            int id = pool.load(fileName,1);
            return id;
        }
        return pool.load(afd, 1);
    }



    //PROPERTIES
    private int id;
    AssetManager assets;
    private SoundPool pool;
}