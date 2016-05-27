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
        name = n;
        System.out.println("Новый поток: " + name) ;
        this.makeHits();
        this.parentPatt = parent;
        //t.start();
    }
    public String getTrackName(){
        return this.name;
    }
    public ArrayList<Hit> getHits(){
        return hitsArray;
    }
    public void connectInstrument(Context mCont, String URL){
        this.connectedInstrument = new Instrument(mCont,parentPatt.getSoundPool(),URL);
    }

    public void setTrackVolume(float level){
        if(connectedInstrument != null){
        connectedInstrument.setVolume(level);}
    }

    public float getTrackVolume(){
        return connectedInstrument.getVolume();
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
    private String name;
    private Pattern parentPatt;
    protected ArrayList<Hit> hitsArray = new ArrayList<>();
    protected Instrument connectedInstrument;// = new Instrument("H2Sv4 - THHL - HiHat(0009).wav");
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
        public boolean getActive(){
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