package com.example.fd.sampler;
import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.util.Log;

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
    public Track(String n){
        name = n;
        System.out.println("Новый трек: " + name) ;
        this.makeHits();
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
    public void connectInstrument(String URL){
        this.connectedInstrument = new Instrument(1, AudioManager.STREAM_MUSIC,0,URL);
        Log.d("Wav","Was connected");
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
    public void performSound(int step){
        for (int i=(step-1);i<hitsArray.size();i++) {
            //System.out.println("Проверяю играет ли сэмплер");
            if(!isPaused) {
                if (hitsArray.get(i).getActive()) {
                    this.connectedInstrument.playSound();
                    // System.out.println("Active hit, step number " + hitsArray.indexOf(hit));
                } else {
                    try {
                        Thread.sleep(Sampler.getSampler().getDelay());
                    } catch (InterruptedException exc) {}
                }
            }
        }
    }
    public void run() {
        keepRunning = true;
        try {
            while (keepRunning) {
                if (isPaused) {
                    synchronized (connectedInstrument){
                        // System.out.println("Поступил запрос на стоп");
                        connectedInstrument.wait();
                        isPaused = false;
                    }
                }

                else { long begin = System.currentTimeMillis();
                    performSound(Sampler.getSampler().getCurrentStep());
                    long after = System.currentTimeMillis();
                System.out.println(Thread.currentThread().getName() + " played " + (after-begin));}
            }
        } catch (Exception e) {
            System.out.println(name + " прерван.");}
    }


    //PROPERTIES
    private String name;
    private Thread t;
    protected boolean isPaused = false;
    protected boolean keepRunning = false;
    protected ArrayList<Hit> hitsArray = new ArrayList<>();
    protected Instrument connectedInstrument;
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
    public Metronome(Context myContext){
        super("Metronome");
        this.makeAllHitsActive();
        this.connectInstrument("Metronome.wav");
    }
    public void performSound(int step){
        System.out.println("Вызван performSound!");
        for (int i=(step-1);i<hitsArray.size();i++) {
            //System.out.println("Проверяю играет ли сэмплер");
            if(!isPaused) {
                if (i == (Sampler.getSampler().getSteps()-1)) {
                    System.out.println("Метроном сбросил шаг на 1");
                    Sampler.getSampler().setCurrentStep(1);}
                else {
                    Sampler.getSampler().setCurrentStep(i+1);
                    System.out.println("Метроном поставил шаг на " + Sampler.getSampler().getCurrentStep());
                }
                if (hitsArray.get(i).getActive()) {
                    System.out.println("Current step is: " + Sampler.getSampler().getCurrentStep());
                    this.connectedInstrument.playSound();
                }
            }
        }
    }


}