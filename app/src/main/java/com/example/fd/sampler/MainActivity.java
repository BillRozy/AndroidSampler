package com.example.fd.sampler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Activity implements Observer {

   // private PatternLayout mPatternLayout;
    private Sampler myApp;
    private Pattern firstPattern;
    private Button addTrack;
    private Button stop;
    private Button play;
    private Button pause;
    static final private int CHOOSE_SAMPLE = 0;
    private String chosenPath;
    private int chosenNumber;
    private PatternFragment mPatternFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mPatternFragment = new PatternFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment,mPatternFragment).commit();
        addTrack = (Button) this.findViewById(R.id.addTrackButton);
        stop = (Button) this.findViewById(R.id.stopButton);
        pause = (Button) this.findViewById(R.id.pauseButton);
        play = (Button) this.findViewById(R.id.playButton);
        myApp = Sampler.getSampler();
        firstPattern = new Pattern(getApplicationContext());
        firstPattern.addObserver(this);
        myApp.addPattern(firstPattern);
        myApp.setPatternActive(firstPattern);

       addTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPatternFragment.addTrackLayout(new TrackLayout(getApplicationContext()));
                Track curTr = myApp.getActivePattern().addTrack("Track: " + myApp.getActivePattern().getTrackCounter());
                Toast.makeText(getApplicationContext(), "Добавлен трек " + (myApp.getActivePattern().getTrackCounter() - 1), Toast.LENGTH_SHORT).show();
                mPatternFragment.remakeTracks();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Sampler.getSampler().play();
                    for (TrackLayout tl : mPatternFragment.getTracksLayoutsArray()) {
                        tl.getDeleteBtn().setEnabled(false);
                    }
                play.setBackgroundResource(R.drawable.play_white);
                }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Sampler.getSampler().stop();
                for(TrackLayout tl : mPatternFragment.getTracksLayoutsArray()){
                    tl.getDeleteBtn().setEnabled(true);
                }
                play.setBackgroundResource(R.drawable.play);
            }
        });


        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sampler.getSampler().pause();
                for(TrackLayout tl : mPatternFragment.getTracksLayoutsArray()){
                    tl.getDeleteBtn().setEnabled(true);
                }
                play.setBackgroundResource(R.drawable.play);
            }
        });


        NumberPicker bpmPicker = (NumberPicker) this.findViewById(R.id.numberPicker);
        bpmPicker.setMaxValue(400);
        bpmPicker.setMinValue(60);
        bpmPicker.setValue(Sampler.getSampler().getBPM());
        bpmPicker.setOnValueChangedListener(new bpmPickerHandler());

        Sampler.getSampler().stepsBar = (ProgressBar) this.findViewById(R.id.progressBar);
        Sampler.getSampler().stepsBar.setMax(16);
        Sampler.getSampler().stepsBar.setProgress(0);


    }


    @Override
    public void update(Observable observable, Object data) {

    }


    class bpmPickerHandler implements NumberPicker.OnValueChangeListener {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            Sampler.getSampler().setBPM(newVal);
        }
    }
}