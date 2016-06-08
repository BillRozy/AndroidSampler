package com.example.fd.sampler;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

/**
 * Created by FD on 28.04.2016.
 */
//CLASS Pattern, keeper of tracks
class Pattern implements Runnable{
    //METHODS
    //test
    public ArrayList<Track> getTracksArray(){
        return tracksArray;
    }
    public SoundPool getSoundPool(){return this.sPool;}
    //end test

    //CONSTRUCTOR
    public Pattern(Context mCont){
        tracksArray = new ArrayList<>(6);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < Build.VERSION_CODES.LOLLIPOP) {
            sPool = new SoundPool(12, AudioManager.STREAM_MUSIC, 1);
        }
        else {
            attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            sPool = new SoundPool.Builder().setAudioAttributes(attributes).setMaxStreams(12).build();
        }
        this.addMetronome(mCont);
        this.musicThread = new Thread(this);
        mPatternCounter++;
        mPatternName = "Pattern " + mPatternCounter;



    }
    public void setPatternName(String name){mPatternName = name;}
    public String getPatternName(){return  mPatternName;}
    public Track getTrack(int number) {
        return tracksArray.get(number);
    }
    public void stopPlaying(){keepRunning = false;}
    public void pause() {
        isPaused = true;
    }
    public synchronized void requestResume(){
        this.notify();
    }
    public Track addTrack(String name) {
        Track track = new Track(name,this);
        track.setTrackVolume(1F);
        tracksArray.add(track);
        trackCounter++;
        return track;
    }

    public void removeTrack(Track track){
        tracksArray.remove(track);
        trackCounter--;
    }

    public Thread getMusicThread(){
        return musicThread;
    }

    public int getTrackCounter(){
        return trackCounter;
    }

    public int getPatternBPM(){return mPatternBPM;}

    public void setPatternBPM(int mPatternBPM) {
        this.mPatternBPM = mPatternBPM;
    }

    public int getPatternSteps() {
        return mPatternSteps;
    }

    public void setPatternSteps(int mPatternSteps) {
        this.mPatternSteps = mPatternSteps;
    }

    public void addMetronome(Context mCont){
        Track metronome = new Metronome(mCont,this);
        tracksArray.add(metronome);
        trackCounter++;
    }

    public void playPattern(int step) {
            for (int i = step-1; i < Sampler.getSampler().getSteps(); i++) {
                if(!isPaused) {
                    Sampler.getSampler().setCurrentStep(i+1);
                    Sampler.getSampler().stepsBar.setProgress(i+1);
                    Log.d("Current step is ",Sampler.getSampler().getCurrentStep()+"");
                    for (Track track : tracksArray) {
                        if (track.hitsArray.get(i).getState()) {
                            track.performSound();
                        }
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(Sampler.getSampler().getDelay());
                    } catch (InterruptedException exc) {
                    }
                    if(i==Sampler.getSampler().getSteps()-1){
                        Sampler.getSampler().setCurrentStep(1);
                    }
                }
            }
    }


    public void run() {
        keepRunning = true;
        try {
            while (keepRunning) {
                if (isPaused) {
                    synchronized (this){
                        // System.out.println("Поступил запрос на стоп");
                        wait();
                        isPaused = false;
                    }
                }
                else playPattern(Sampler.getSampler().getCurrentStep());
            }
        } catch (Exception e) {
            System.out.println(this + " прерван." + e);}
    }


    //PROPERTIES
    private ArrayList<Track> tracksArray;
    private int trackCounter = 0;
    private Thread musicThread;
    private String mPatternName;
    protected boolean isPaused = false;
    protected boolean keepRunning = false;
    private int mPatternBPM = 120;
    private int mPatternSteps = 16;
   AudioAttributes attributes;
    SoundPool sPool;
    static public int mPatternCounter = 0;
}
