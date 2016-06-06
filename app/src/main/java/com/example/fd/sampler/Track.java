package com.example.fd.sampler;
import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by FD on 28.04.2016.
 */
//CLASS track, keeper of HITS, and performer of hits running
class Track implements Serializable{
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
        hasConnectedInstrument = true;
    }

    public boolean getHitState(int num){
        return hitsArray.get(num).getState();
    }

    public void setTrackVolume(float level){
        if(connectedInstrument != null){
        connectedInstrument.setVolume(level);}
    }

    public float getTrackVolume(){
        return mTrackVolume;
    }

    public String getPathToInstrument() {
        return mPathToInstrument;
    }

    public boolean getHasConnectedInstrument(){return hasConnectedInstrument;}

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

    public void makeHitsActiveFromDescription(String desc){
        String[] hitsString = desc.split(" ");
        int activeHitsArray[] = new int[hitsString.length];
        for (int i = 0; i < hitsString.length; i++) {
            if(!hitsString[i].equals("")) {
                activeHitsArray[i] = Integer.parseInt(hitsString[i]);
            }
            // System.out.println(numArr[i]);
        }
        makeHitActive(activeHitsArray);
    }



    public void performSound(){

                    this.connectedInstrument.playSound();

        }

    public String getHitsDescroption(){
        String desc = "";
        for (Track.Hit hit : hitsArray){
            if(hit.getState()){
                desc = desc + (hitsArray.indexOf(hit) + 1) + " ";
            }
        }
        if (desc.length() > 2) {
            desc = desc.substring(0, desc.length() - 1);
        }
        return desc;
    }

    //PROPERTIES
    private String mTrackName;
    private Pattern parentPatt;
    protected ArrayList<Hit> hitsArray = new ArrayList<>();
    protected Instrument connectedInstrument;
    public String mPathToInstrument;
    private float mTrackVolume = 0.7F;
    private boolean hasConnectedInstrument = false;
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