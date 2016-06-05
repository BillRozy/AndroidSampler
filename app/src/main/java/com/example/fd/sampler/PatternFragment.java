package com.example.fd.sampler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
    private Pattern mConnectedPattern;
    private Activity connectedActivity;
    private ViewGroup scrollViewFrame;


    public PatternFragment() {

        tracksArray = new ArrayList<>(8);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate Fragment", "WORKED");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("onStart Fragment", "WORKED");
    }

    public void makePatternForFragment(Context mCont) {
        mConnectedPattern = new Pattern(mCont);
        mConnectedPattern.addTrack("Track-1");
        mConnectedPattern.addTrack("Track-2");
        mConnectedPattern.addTrack("Track-3");
        Sampler.getSampler().addPattern(mConnectedPattern);
        Sampler.getSampler().setPatternActive(mConnectedPattern);
    }

    public void connectPattern(Pattern patt) {
        mConnectedPattern = patt;
    }

    public ArrayList<TrackLayout> getTracksLayoutsArray() {
        return tracksArray;
    }

    public TrackLayout getTrackLayout(int the_index) {
        return tracksArray.get(the_index);
    }

    public void addTrackLayout(TrackLayout tl) {
        tracksArray.add(tl);
    }

    public void makeTracks(Context context) {
        while (Sampler.getSampler().getActivePattern().getTracksArray().size() - 1 > tracksArray.size()) {
            TrackLayout tl = new TrackLayout(context);
            tl.getTrackName().setText(Sampler.getSampler().getActivePattern().getTrack(tracksArray.size() + 1).getTrackName());
            addTrackLayout(tl);
        }
    }

    public void addViewToFragment(TrackLayout tl) {
        verticalLayer.addView(tl);
    }

    public void remakeTracks() {
        for (final TrackLayout tl : tracksArray) {
            tl.getConnectInstrumentBtn().setOnClickListener(new InstrumentConnectionListener(tl));

            tl.getVolumeSlider().setOnSeekBarChangeListener(new VolumeControllerListener(tl, tracksArray) );

            tl.getMuteBtn().setOnCheckedChangeListener(new MuteControllerListener(tl,tracksArray));

            tl.getTrackName().setOnClickListener(new TrackNameControlListener(tl, tracksArray,connectedActivity));

            tl.getDeleteBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Sampler.getSampler().getActivePattern().removeTrack(Sampler.getSampler().getActivePattern().getTrack(getTracksLayoutsArray().indexOf(tl) + 1));
                    deleteTrackLayout(tl);

                }
            });

            final ArrayList<HitView> hitsArrayOfCurrentTrack = tl.getHitsArray();
            for (final HitView hv : hitsArrayOfCurrentTrack) {
                if (Sampler.getSampler().getActivePattern().getTrack(getTracksLayoutsArray().indexOf(tl) + 1).getHitState(hitsArrayOfCurrentTrack.indexOf(hv))) {
                    hv.setActive();
                    hv.setImageResource(R.drawable.btn_media_player_selected);
                }
                hv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hv.toggleState();
                        Log.d("CLICKED FROM MAIN LIST", "BUTTON " + (hitsArrayOfCurrentTrack.indexOf(hv) + 1) + " of track " + getTracksLayoutsArray().indexOf(tl) + " Pressed");
                        hitsStateMaker(tl, hv, hitsArrayOfCurrentTrack);
                    }
                });
            }
        }
    }

    public void deleteTrackLayout(TrackLayout trackLayout) {
        tracksArray.remove(trackLayout);
        verticalLayer.removeView(trackLayout);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (scrollViewFrame == null) {
            scrollViewFrame = (ViewGroup) inflater.inflate(R.layout.fragment_pattern, container, false);
        }
        // verticalLayer = (LinearLayout) inflater.inflate(R.layout.fragment_pattern, container, false);}
        verticalLayer = (LinearLayout) scrollViewFrame.findViewById(R.id.verticalLayer);
        if (verticalLayer == null) {
            verticalLayer = new LinearLayout(connectedActivity);
            verticalLayer.setOrientation(LinearLayout.VERTICAL);
            verticalLayer.setPadding(8, 8, 8, 8);
        }
        Log.d("OnCreateView", "WORKED");
        if (tracksArray.size() == 0) {
            makeTracks(connectedActivity);
            remakeTracks();
            for (TrackLayout tl : tracksArray) {
                verticalLayer.addView(tl);
            }
        }
        return scrollViewFrame;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // makeTracks(connectedActivity);
        remakeTracks();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_SAMPLE) {
            if (resultCode == -1) {
                chosenPath = data.getStringExtra(SampleListActivity.mSelectedSamplePath);
                String chosenName = data.getStringExtra(SampleListActivity.mSelectedSampleName);
                Sampler.getSampler().getActivePattern().getTrack(mChosenTrack).connectInstrument(getActivity().getApplicationContext(), chosenPath);
                Sampler.getSampler().getActivePattern().getTrack(mChosenTrack).setTrackName(chosenName);
                tracksArray.get(mChosenTrack - 1).getTrackName().setText(chosenName);
                tracksArray.get(mChosenTrack - 1).getConnectInstrumentBtn().setBackgroundResource(R.drawable.galka);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        connectedActivity = activity;
        Log.d("onAttach Fragment", "WORKED");
    }

    public void hitsStateMaker(TrackLayout tl, HitView hv, ArrayList<HitView> hitsAr) {
        if (hv.getState()) {
            Sampler.getSampler().getActivePattern().getTrack(getTracksLayoutsArray().indexOf(tl) + 1).makeHitActive(hitsAr.indexOf(hv) + 1);
            hv.setImageResource(R.drawable.btn_media_player_selected);
        } else {
            Sampler.getSampler().getActivePattern().getTrack(getTracksLayoutsArray().indexOf(tl) + 1).makeHitActive(hitsAr.indexOf(hv) + 1);
            hv.setImageResource(R.drawable.btn_media_player_disabled_selected);
        }
    }


    class InstrumentConnectionListener implements View.OnClickListener {

        private TrackLayout tl;

        public InstrumentConnectionListener(TrackLayout tl) {
            this.tl = tl;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PatternFragment.this.getActivity(), FileBrowserActivity.class);
            mChosenTrack = tracksArray.indexOf(tl) + 1;
            startActivityForResult(intent, CHOOSE_SAMPLE);
        }

    }
}

class VolumeControllerListener implements SeekBar.OnSeekBarChangeListener{

    private TrackInterface tl;
    private ArrayList tracksArray;

    public VolumeControllerListener(TrackInterface tl, ArrayList array){
        this.tl =  tl;
        tracksArray = array;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Sampler.getSampler().getActivePattern().getTrack(tracksArray.indexOf(tl) + 1).setTrackVolume((float) seekBar.getProgress() / 100);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }
}

class MuteControllerListener implements CompoundButton.OnCheckedChangeListener{

    private TrackInterface tl;
    private ArrayList tracksArray;

    public MuteControllerListener(TrackInterface tl, ArrayList array){
        this.tl =  tl;
        tracksArray = array;
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            Sampler.getSampler().getActivePattern().getTrack(tracksArray.indexOf(tl) + 1).setTrackVolume(0F);
        } else {
            Sampler.getSampler().getActivePattern().getTrack(tracksArray.indexOf(tl) + 1).setTrackVolume((float) tl.getVolumeSlider().getProgress() / 100);
        }

    }
}

class TrackNameControlListener implements View.OnClickListener{
    private TrackInterface tl;
    private ArrayList tracksArray;
    private Activity act;

    public TrackNameControlListener(TrackInterface tl, ArrayList array,Activity activity){
        this.tl =  tl;
        tracksArray = array;
        act = activity;
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(act);
        alertDialog.setTitle("Track Name");
        alertDialog.setMessage("Enter New Name");

        final EditText input = new EditText(act);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.galka);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String title = input.getText().toString();
                        if (title.compareTo("") != 0) {
                            tl.getTrackName().setText(title);
                            Sampler.getSampler().getActivePattern().getTrack(tracksArray.indexOf(tl) + 1).setTrackName(title);

                        }
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

}