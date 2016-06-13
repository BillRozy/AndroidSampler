package com.example.fd.sampler;

import java.io.Serializable;

public class Preset implements Serializable {
    private String title;
    private int steps;
    private int BPM;
    private String[] activeHits;
    private String[] trackTitles;
    private String[] pathsToSamples;
    private float[] volumesArray;
    private Boolean[] hasSample;

    public float[] getVolumesArray() {
        return volumesArray;
    }

    public void setVolumesArray(float[] volumesArray) {
        this.volumesArray = volumesArray;
    }

    public Boolean[] getHasSample() {
        return hasSample;
    }

    public void setHasSample(Boolean[] hasSample) {
        this.hasSample = hasSample;
    }

    public String[] getPathsToSamples() {
        return pathsToSamples;
    }

    public void setPathsToSamples(String[] pathsToSamples) {
        this.pathsToSamples = pathsToSamples;
    }

    public int getBPM() {
        return BPM;
    }

    public void setBPM(int BPM) {
        this.BPM = BPM;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getActiveHits() {
        return activeHits;
    }

    public void setActiveHits(String[] activeHits) {
        this.activeHits = activeHits;
    }

    public String[] getTrackTitles() {
        return trackTitles;
    }

    public void setTrackTitles(String[] trackTitles) {
        this.trackTitles = trackTitles;
    }
}
