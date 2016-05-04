package com.example.fd.sampler;

import java.util.ArrayList;

/**
 * Created by FD on 28.04.2016.
 */
// Class-controller of app, singletone

class Sampler {
    // Singletone Realization
    private static volatile Sampler instance;

    public static Sampler getSampler() {
        Sampler localInstance = instance;
        if (localInstance == null) {
            synchronized (Sampler.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Sampler();
                }
            }
        }
        return localInstance;
    }

    // Methods

    public void offSampler(){
        isPlaying = false;
    }

    public void play(){
        System.out.println("Начинаю воспроизведение...");
        activePattern.playPattern();}

    public void stop(){
        System.out.println("Trying to stop sampler!");
        activePattern.stopPattern();
        currentStep=1;
        System.out.println("After stop step is: " + currentStep);
    }
    public void pause(){
        System.out.println("Paused sampler!");
        activePattern.stopPattern();
    }
    public void addPattern(Pattern patt){
        patterns.add(patt);
    }

    public void setPatternActive(Pattern pattern){
        this.activePattern = pattern;
    }

    public Pattern getActivePattern(){
        return this.activePattern;
    }

    public int getDelay(){
        double dblBPM = (double) BPM;
        double delay =  120.0/dblBPM * 500.0;
        return (int) delay;

    }

    public void setReplays(int q){
        replays = q;
    }
    public int getReplays(){
        return replays;
    }
    public void setBPM(int B){
        BPM = B;
    }
    public int getBPM(){
        return BPM;
    }
    public void setSteps(int s){
        steps = s;
    }
    public int getSteps(){
        return steps;
    }
    public boolean isPlaying(){return isPlaying;}
    public void setPlaying(boolean t){isPlaying = t;}
    public Pattern getPattern(int number){
        return patterns.get(number-1);
    }
    public int getCurrentStep(){
        return currentStep;
    }
    public void setCurrentStep(int step){
        currentStep = step;
    }

    //PROPERTIES
    private ArrayList<Pattern> patterns = new ArrayList<>();
    private Pattern activePattern;
    private int currentStep = 1;
    private int BPM = 200;
    private int steps = 16;
    private int replays = 1;
    private boolean isPlaying = false;

}
