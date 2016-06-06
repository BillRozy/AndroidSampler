package com.example.fd.sampler;

import java.io.Serializable;

/**
 * Created by FD on 06.06.2016.
 */
public class Preset implements Serializable {
    private String title;
    private int steps;
    private int BPM;
    private int tracksQ;
    private String[] activeHits;
    private String[] trackTitles;

    public void setActiveHits(String[] activeHits) {
        this.activeHits = activeHits;
    }

    public void setBPM(int BPM) {
        this.BPM = BPM;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTracksQ(int tracksQ) {
        this.tracksQ = tracksQ;
    }

    public void setTrackTitles(String[] trackTitles) {
        this.trackTitles = trackTitles;
    }

    public int getBPM() {
        return BPM;
    }

    public int getSteps() {
        return steps;
    }

    public int getTracksQ() {
        return tracksQ;
    }

    public String getTitle() {
        return title;
    }

    public String[] getActiveHits() {
        return activeHits;
    }

    public String[] getTrackTitles() {
        return trackTitles;
    }
}
