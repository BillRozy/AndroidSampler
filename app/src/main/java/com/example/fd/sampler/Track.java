package com.example.fd.sampler;
import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by FD on 28.04.2016.
 */
//CLASS track, keeper of HITS, and performer of hits running
class Track implements Runnable {
    //METHODS
    //test
    public Instrument getConnectedInstrument(){
        return connectedInstrument;
    }
    //end test
    public Track(String n, Pattern parent){
        t = new Thread(this);
        name = n;
        System.out.println("Новый поток: " + t) ;
        this.makeHits();
        this.parentPatt = parent;
        //t.start();
    }
    public Thread getTrackThread(){
        return this.t;
    }
    public String getTrackThreadName(){
        return this.name;
    }
    public void stop(){keepRunning = false;}
    public void pause() {
        isPaused = true;
    }
    public synchronized void requestResume(Track track){
        track.notify();
    }
    public ArrayList<Hit> getHits(){
        return hitsArray;
    }
    public void connectInstrument(Context mCont, String URL){
        this.connectedInstrument = new Instrument(mCont,parentPatt.getSoundPool(),URL);
    }
    public void makeHits(){
        for(int i=0;i<Sampler.getSampler().getSteps();i++)
        {
            hitsArray.add(new Hit(false));
        }
    }
    public void makeHitActive(int... n){
        for(int i : n)
            hitsArray.get(i-1).setActive();
    }

    public void makeAllHitsActive() {
        for (Hit hit : hitsArray) {
            hit.setActive();
        }
    }
    public void performSound(){

                    this.connectedInstrument.playSound();
                    // System.out.println("Active hit, step number " + hitsArray.indexOf(hit));
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
                else performSound();
            }
        } catch (Exception e) {
            System.out.println(name + " прерван.");}
    }


    //PROPERTIES
    private String name;
    private Thread t;
    private Pattern parentPatt;
    protected boolean isPaused = false;
    protected boolean keepRunning = false;
    protected ArrayList<Hit> hitsArray = new ArrayList<>();
    protected Instrument connectedInstrument;// = new Instrument("H2Sv4 - THHL - HiHat(0009).wav");
    //INNER CLASS
    public class Hit
    {
        //METHODS
        public Hit(boolean act){
            this.isActive = act;
        }
        public void setActive(){
            this.isActive = true;
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
    }
    public void performSound(int step){
        System.out.println("Вызван performSound!");
        for (int i=(step-1);i<hitsArray.size();i++) {
            //System.out.println("Проверяю играет ли сэмплер");
            if(!isPaused) {
                    System.out.println("Current step is: " + (i+1));
                    this.connectedInstrument.playSound();

                if (i == (Sampler.getSampler().getSteps()-1)) {
                    System.out.println("Метроном сбросил шаг на 1");
                    Sampler.getSampler().setCurrentStep(1);}
                else {
                    Sampler.getSampler().setCurrentStep(i+2);
                    System.out.println("Метроном поставил шаг на " + Sampler.getSampler().getCurrentStep());
                }
            }
        }
    }


}