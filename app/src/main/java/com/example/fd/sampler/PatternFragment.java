package com.example.fd.sampler;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;


import java.util.ArrayList;

/**
 * Created by FD on 26.05.2016.
 */
public class PatternFragment extends Fragment {

    private ArrayList<TrackLayout> tracksArray = null;
    private LinearLayout verticalLayer;
    private int mChosenTrack;
    private String chosenPath;
    static final private int CHOOSE_SAMPLE = 0;

    public PatternFragment(){
        tracksArray = new ArrayList<>(8);
    }

    public ArrayList<TrackLayout> getTracksLayoutsArray(){
        return tracksArray;
    }

    public TrackLayout getTrackLayout(int the_index){
        return tracksArray.get(the_index);
    }

    public void addTrackLayout(TrackLayout tl){
        tracksArray.add(tl);
        verticalLayer.addView(tl);
    }

    public void makeTracks(){
        while(Sampler.getSampler().getActivePattern().getTracksArray().size()-1>tracksArray.size())
        {
            addTrackLayout(new TrackLayout(getActivity().getApplicationContext()));
        }
    }

    public void remakeTracks() {
        for (final TrackLayout tl : tracksArray) {
            tl.getConnectInstrumentBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PatternFragment.this.getActivity(), SampleListActivity.class);
                    mChosenTrack = tracksArray.indexOf(tl) + 1;
                    startActivityForResult(intent, CHOOSE_SAMPLE);

                }
            });

            tl.getVolumeSlider().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Sampler.getSampler().getActivePattern().getTrack(getTracksLayoutsArray().indexOf(tl) + 1).setTrackVolume((float)seekBar.getProgress()/100);
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }
            });

            tl.getMuteBtn().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        Sampler.getSampler().getActivePattern().getTrack(getTracksLayoutsArray().indexOf(tl) + 1).setTrackVolume(0F);}
                    else {
                        Sampler.getSampler().getActivePattern().getTrack(getTracksLayoutsArray().indexOf(tl) + 1).setTrackVolume((float) tl.getVolumeSlider().getProgress()/100);}

                }
            });

            tl.getDeleteBtn().setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    Sampler.getSampler().getActivePattern().removeTrack(Sampler.getSampler().getActivePattern().getTrack(getTracksLayoutsArray().indexOf(tl)+1));
                    Log.d("DELETED:",Sampler.getSampler().getActivePattern().getTrack(getTracksLayoutsArray().indexOf(tl)+1).getTrackName()+"");
                    deleteTrackLayout(tl);

                }
            });

            final ArrayList<HitView> hitsArrayOfCurrentTrack = tl.getHitsArray();
            for (final HitView hv : hitsArrayOfCurrentTrack) {
                hv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hv.toggleState();
                        Log.d("CLICKED FROM MAIN LIST", "BUTTON " + (hitsArrayOfCurrentTrack.indexOf(hv) + 1) + " of track " + getTracksLayoutsArray().indexOf(tl) + " Pressed");
                        if (hv.getState()) {
                            Sampler.getSampler().getActivePattern().getTrack(getTracksLayoutsArray().indexOf(tl) + 1).makeHitActive(hitsArrayOfCurrentTrack.indexOf(hv) + 1);
                            hv.setImageResource(R.drawable.btn_media_player_selected);
                        } else {
                            Sampler.getSampler().getActivePattern().getTrack(getTracksLayoutsArray().indexOf(tl) + 1).makeHitActive(hitsArrayOfCurrentTrack.indexOf(hv) + 1);
                            hv.setImageResource(R.drawable.btn_media_player_disabled_selected);
                        }
                    }
                });
            }
        }
    }

    public void deleteTrackLayout(TrackLayout trackLayout){
        tracksArray.remove(trackLayout);
        verticalLayer.removeView(trackLayout);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        verticalLayer = (LinearLayout) inflater.inflate(R.layout.fragment_pattern, container, false);
        return verticalLayer;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_SAMPLE) {
            if (resultCode == -1) {
                chosenPath = data.getStringExtra(SampleListActivity.mSelectedSamplePath);
                Sampler.getSampler().getActivePattern().getTrack(mChosenTrack).connectInstrument(getActivity().getApplicationContext(), chosenPath);
                getTracksLayoutsArray().get(mChosenTrack-1).getConnectInstrumentBtn().setImageResource(R.drawable.mixer);
            }
        }
    }
}