package com.example.fd.sampler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Activity implements Observer {

    private PatternLayout mPatternLayout;
    private ArrayList<SoundSample> tempSongList;
    private Sampler myApp;
    private Pattern firstPattern;
    private Button addTrack;
    private Button stop;
    private Button play;
    private Button pause;
    static final private int CHOOSE_SAMPLE = 0;
    private String chosenPath;
    private int chosenNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout mainFrame = (LinearLayout) findViewById(R.id.main_frame);
        mPatternLayout = (PatternLayout) this.findViewById(R.id.patternView);
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

                mPatternLayout.addTrackLayout(new TrackLayout(getApplicationContext()));
                Track curTr = myApp.getActivePattern().addTrack("Track: " + myApp.getActivePattern().getTrackCounter());
                curTr.connectInstrument(getApplicationContext(), "H2Sv2 - THKL - Kick(0004).wav");
                Toast.makeText(getApplicationContext(), "Добавлен трек " + (myApp.getActivePattern().getTrackCounter() - 1), Toast.LENGTH_SHORT).show();
                remakeTracks();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Нажата кнопка Play", Toast.LENGTH_SHORT).show();
                Sampler.getSampler().play();
            }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sampler.getSampler().stop();
            }
        });


        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sampler.getSampler().pause();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_SAMPLE) {
            if (resultCode == RESULT_OK) {
                chosenPath = data.getStringExtra(SampleListActivity.mSelectedSamplePath);
                myApp.getActivePattern().getTrack(chosenNumber).connectInstrument(getApplicationContext(), chosenPath);
            }
        }
    }

    @Override
    public void update(Observable observable, Object data) {

    }

    public void remakeTracks() {
        for (final TrackLayout tl : mPatternLayout.getTracksLayoutsArray()) {
            tl.getConnectInstrumentBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SampleListActivity.class);
                    chosenNumber = mPatternLayout.getTracksLayoutsArray().indexOf(tl) + 1;
                    startActivityForResult(intent, CHOOSE_SAMPLE);
                   /* final String[] mSamplesName = {"Kick", "Snare", "Hat","From File System"};
                    AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                    ad.setTitle("Connecting Sample")  // заголовок
                   .setItems(mSamplesName, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            switch (item) {
                            case 0:
                                myApp.getActivePattern().getTrack(mPatternLayout.getTracksLayoutsArray().indexOf(tl) + 1).connectInstrument(getApplicationContext(), "H2Sv2 - THKL - Kick(0004).wav");
                                break;
                            case 1:
                                myApp.getActivePattern().getTrack(mPatternLayout.getTracksLayoutsArray().indexOf(tl) + 1).connectInstrument(getApplicationContext(), "snare.wav");
                                break;
                            case 2:
                                myApp.getActivePattern().getTrack(mPatternLayout.getTracksLayoutsArray().indexOf(tl) + 1).connectInstrument(getApplicationContext(), "H2Sv4 - THHL - HiHat(0006).wav");
                                break;
                                case 3:
                                  break;

                        }
                            tl.getConnectInstrumentBtn().setImageResource(R.drawable.ic_delete);
                            Toast.makeText(getApplicationContext(), "Connected",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setCancelable(true)
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            Toast.makeText(getApplicationContext(), "Вы ничего не выбрали",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    alertDialog = ad.show();*/
                }
            });

            final ArrayList<HitView> hitsArrayOfCurrentTrack = tl.getHitsArray();
            for (final HitView hv : hitsArrayOfCurrentTrack) {
                hv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hv.toggleState();
                        Log.d("CLICKED FROM MAIN LIST", "BUTTON " + (hitsArrayOfCurrentTrack.indexOf(hv) + 1) + " of track " + mPatternLayout.getTracksLayoutsArray().indexOf(tl) + " Pressed");
                        if (hv.getState()) {
                            myApp.getActivePattern().getTrack(mPatternLayout.getTracksLayoutsArray().indexOf(tl) + 1).makeHitActive(hitsArrayOfCurrentTrack.indexOf(hv) + 1);
                            hv.setImageResource(R.drawable.btn_media_player_selected);
                        } else {
                            myApp.getActivePattern().getTrack(mPatternLayout.getTracksLayoutsArray().indexOf(tl) + 1).makeHitActive(hitsArrayOfCurrentTrack.indexOf(hv) + 1);
                            hv.setImageResource(R.drawable.btn_media_player_disabled_selected);
                        }
                    }
                });
            }
        }
    }


    class bpmPickerHandler implements NumberPicker.OnValueChangeListener {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            Sampler.getSampler().setBPM(newVal);
        }
    }
}