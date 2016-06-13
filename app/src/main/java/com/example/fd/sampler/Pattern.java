package com.example.fd.sampler;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


//CLASS Pattern, keeper of tracks
class Pattern implements Runnable {
    static public int mPatternCounter = 0;
    protected boolean isPaused = false;
    protected boolean keepRunning = false;
    //end test
    AudioAttributes attributes;
    SoundPool sPool;
    //PROPERTIES
    private ArrayList<Track> tracksArray;
    private int trackCounter = 0;
    private String mPatternName;
    private int mPatternBPM = 120;
    private int mPatternSteps = 16;

    //CONSTRUCTOR
    public Pattern(Context mCont) {
        tracksArray = new ArrayList<>(6);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < Build.VERSION_CODES.LOLLIPOP) {
            sPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 1);
        } else {
            attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            sPool = new SoundPool.Builder().setAudioAttributes(attributes).setMaxStreams(12).build();
        }
        this.addMetronome(mCont);
        mPatternCounter++;
        mPatternName = "Pattern " + mPatternCounter;

    }

    //METHODS
    //test
    public ArrayList<Track> getTracksArray() {
        return tracksArray;
    }

    public SoundPool getSoundPool() {
        return this.sPool;
    }

    public void setSoundPoolNull() {
        this.sPool = null;
    }

    public String getPatternName() {
        return mPatternName;
    }

    public void setPatternName(String name) {
        mPatternName = name;
    }

    public Track getTrack(int number) {
        return tracksArray.get(number);
    }

    public void pause() {
        isPaused = true;
    }

    public synchronized void requestResume() {
        this.notify();
    }

    public Track addTrack(String name) {
        Track track = new Track(name, this);
        track.setTrackVolume(1F);
        tracksArray.add(track);
        trackCounter++;
        return track;
    }

    public void removeTrack(Track track) {
        tracksArray.remove(track);
        trackCounter--;
    }

    public int getTrackCounter() {
        return trackCounter;
    }

    public int getPatternBPM() {
        return mPatternBPM;
    }

    public void setPatternBPM(int mPatternBPM) {
        this.mPatternBPM = mPatternBPM;
    }

    public int getPatternSteps() {
        return mPatternSteps;
    }

    public void setPatternSteps(int mPatternSteps) {
        this.mPatternSteps = mPatternSteps;
    }

    public void addMetronome(Context mCont) {
        Track metronome = new Metronome(mCont, this);
        tracksArray.add(metronome);
        trackCounter++;
    }

    public void playPattern(int step) {
        for (int i = step - 1; i < Sampler.getSampler().getSteps(); i++) {
            if (!isPaused) {
                Sampler.getSampler().setCurrentStep(i + 1);
                Sampler.getSampler().stepsBar.setProgress(i + 1);
                Log.d("Current step is ", Sampler.getSampler().getCurrentStep() + "");
                for (Track track : tracksArray) {
                    if (track.hitsArray.get(i).getState()) {
                        track.performSound();
                    }
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(Sampler.getSampler().getDelay());
                } catch (InterruptedException exc) {
                }
                if (i == Sampler.getSampler().getSteps() - 1) {
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
                    keepRunning = false;
                } else {
                    playPattern(Sampler.getSampler().getCurrentStep());
                }
            }
        } catch (Exception e) {
            System.out.println(this + " прерван." + e);
        }
        Log.d("Muse Thread", "finished");
        keepRunning = true;
        isPaused = false;
    }
}
