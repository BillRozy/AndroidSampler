package com.example.fd.sampler;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by FD on 28.04.2016.
 */
//CLASS Pattern, keeper of tracks
class Pattern {
    //METHODS
    //test
    public ArrayList<Track> getTracksArray(){
        return tracksArray;
    }
    public SoundPool getSoundPool(){return this.sPool;}
    //end test

    //CONSTUCTOR
    public Pattern(Context mCont){
        this.addMetronome(mCont);
    }
    public Track getTrack(int number) {
        return tracksArray.get(number);
    }
    public void addTrack(String name) {
        Track track = new Track(name,this);
        tracksArray.add(track);
        Thread thread = track.getTrackThread();
        thread.setName("Track-" + trackCounter + track.getTrackThreadName());
        // track.getTrackThread().start();
        trackCounter++;
    }

    public void addMetronome(Context mCont){
        Track metronome = new Metronome(mCont,this);
        tracksArray.add(metronome);
        Thread thread = metronome.getTrackThread();
        thread.setName("Metronome");
        trackCounter++;
    }

    public void playPattern() {
        for (Track track : tracksArray) {
            if (track.getTrackThread().getState() == Thread.State.NEW ) {
                track.getTrackThread().start();}
            else track.requestResume(track);
        }

    }


    public void stopPattern() {
        for (Track track : tracksArray) {
            if(track.getTrackThread().getState() == Thread.State.TIMED_WAITING || track.getTrackThread().getState() == Thread.State.RUNNABLE)
            {
                track.pause();
                System.out.println("This track: stopped " + track.getTrackThread().getName() + " " + track);

            }
        }
    }
    //PROPERTIES
    private ArrayList<Track> tracksArray = new ArrayList<>(10);
    private int trackCounter = 0;
    private SoundPool sPool = new SoundPool(9, AudioManager.STREAM_MUSIC,0);
}
