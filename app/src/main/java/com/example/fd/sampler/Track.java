package com.example.fd.sampler;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by FD on 28.04.2016.
 */
//CLASS track, keeper of HITS, and performer of hits running
class Track{
    //METHODS
    //test
    public Instrument getConnectedInstrument(){
        return connectedInstrument;
    }
    //end test
    public Track(String n, Pattern parent){
        mTrackName = n;
        System.out.println("Новый поток: " + mTrackName) ;
        this.makeHits();
        this.parentPatt = parent;
        //t.start();
    }
    public String getTrackName(){
        return this.mTrackName;
    }
    public void setTrackName(String s){mTrackName = s;}
    public ArrayList<Hit> getHits(){
        return hitsArray;
    }
    public void connectInstrument(Context mCont, String URL){
        this.connectedInstrument = new Instrument(mCont,parentPatt.getSoundPool(),URL);
        mPathToInstrument = URL;
    }

    public boolean getHitState(int num){
        return hitsArray.get(num).getState();
    }

    public void setTrackVolume(float level){
        if(connectedInstrument != null){
        connectedInstrument.setVolume(level);}
    }

    public float getTrackVolume(){
        return connectedInstrument.getVolume();
    }

    public String getPathToInstrument() {
        return mPathToInstrument;
    }

    public void makeHits(){
        for(int i=0;i<Sampler.getSampler().getSteps();i++)
        {
            hitsArray.add(new Hit(false));
        }
    }
    public void makeHitActive(int... n){
        for(int i : n)
            hitsArray.get(i-1).toggleActive();
    }

    public void makeAllHitsActive() {
        for (Hit hit : hitsArray) {
            hit.toggleActive();
        }
    }
    public void performSound(){

                    this.connectedInstrument.playSound();
                    // System.out.println("Active hit, step number " + hitsArray.indexOf(hit));
        }

    //PROPERTIES
    private String mTrackName;
    private Pattern parentPatt;
    protected ArrayList<Hit> hitsArray = new ArrayList<>();
    protected Instrument connectedInstrument;
    public String mPathToInstrument;
    //INNER CLASS
    public class Hit
    {
        //METHODS
        public Hit(boolean act){
            this.isActive = act;
        }
        public void toggleActive(){
            isActive = !isActive;
        }
        public boolean getState(){
            return isActive;
        }
        //PROPERTIES
        private boolean isActive = false;
    }
}

class Metronome extends Track{
    public Metronome(Context mCont,Pattern parent){
        super("Metronome",parent);
        this.makeAllHitsActive();
        this.connectInstrument(mCont,"Metronome.wav");
        this.connectedInstrument.setVolume(0F);
    }

}