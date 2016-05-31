package com.example.fd.sampler;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Activity implements PatternFragment.PatternInterface{

    private static final String BILL_TOTAL = "sum";
    private Sampler myApp;
    private Button addTrack;
    private Button stop;
    private Button play;
    private Button pause;
    private Button nextPattern;
    private Button prevPattern;
    private TextView patternNumber;
    private String chosenPath;
    private ArrayList<PatternFragment> mPatternFragmentsArray = null;
    private PatternFragment mPatternFragment;
    private SharedPreferences sp;
    private int mChosenPatternFragmentNumber = 0;
    public static final String APP_PREFERENCES = "mysettings";
    private double currentBillTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
       if ( mPatternFragmentsArray == null ) {
           mPatternFragmentsArray = new ArrayList<>();
           mPatternFragmentsArray.add(new PatternFragment());
           mPatternFragmentsArray.get(mChosenPatternFragmentNumber).makePatternForFragment(this.getApplicationContext());
           getFragmentManager().beginTransaction().replace(R.id.fragment, mPatternFragmentsArray.get(mChosenPatternFragmentNumber)).commit();
       }
       else
       {
           currentBillTotal = savedInstanceState.getDouble(BILL_TOTAL);
       }
           addTrack = (Button) this.findViewById(R.id.addTrackButton);
           stop = (Button) this.findViewById(R.id.stopButton);
           pause = (Button) this.findViewById(R.id.pauseButton);
           play = (Button) this.findViewById(R.id.playButton);
           nextPattern = (Button) this.findViewById(R.id.nextPatt);
           prevPattern = (Button) this.findViewById(R.id.prevPattern);
            patternNumber = (TextView) this.findViewById(R.id.numPattern);
            patternNumber.setText((mChosenPatternFragmentNumber+1)+"");
           myApp = Sampler.getSampler();
           Log.d("OnCreate", "WOrked");

           addTrack.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   TrackLayout tl = new TrackLayout(getApplicationContext());
                   mPatternFragmentsArray.get(mChosenPatternFragmentNumber).addTrackLayout(tl);
                   mPatternFragmentsArray.get(mChosenPatternFragmentNumber).addViewToFragment(tl);
                   Track curTr = myApp.getActivePattern().addTrack("Track: " + myApp.getActivePattern().getTrackCounter());
                   Toast.makeText(getApplicationContext(), "Добавлен трек " + (myApp.getActivePattern().getTrackCounter() - 1), Toast.LENGTH_SHORT).show();
                   mPatternFragmentsArray.get(mChosenPatternFragmentNumber).remakeTracks();
               }
           });

           play.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Sampler.getSampler().play();
                   for (TrackLayout tl : mPatternFragmentsArray.get(mChosenPatternFragmentNumber).getTracksLayoutsArray()) {
                       tl.getDeleteBtn().setEnabled(false);
                   }
                   play.setBackgroundResource(R.drawable.play_white);
               }
           });


           stop.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   Sampler.getSampler().stop();
                   for (TrackLayout tl : mPatternFragmentsArray.get(mChosenPatternFragmentNumber).getTracksLayoutsArray()) {
                       tl.getDeleteBtn().setEnabled(true);
                   }
                   play.setBackgroundResource(R.drawable.play);
               }
           });


           pause.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Sampler.getSampler().pause();
                   for (TrackLayout tl : mPatternFragmentsArray.get(mChosenPatternFragmentNumber).getTracksLayoutsArray()) {
                       tl.getDeleteBtn().setEnabled(true);
                   }
                   play.setBackgroundResource(R.drawable.play);
               }
           });

           nextPattern.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   myApp.stop();
                   if (mChosenPatternFragmentNumber == (mPatternFragmentsArray.size() - 1)) {
                       addPattern();
                   } else {
                       mChosenPatternFragmentNumber++;
                       getFragmentManager().beginTransaction().replace(R.id.fragment, mPatternFragmentsArray.get(mChosenPatternFragmentNumber)).commit();

                   }
  //                 mPatternFragmentsArray.get(mChosenPatternFragmentNumber).makeTracks(getApplicationContext());
//                   mPatternFragmentsArray.get(mChosenPatternFragmentNumber).makeFragmentLayout();
    //               mPatternFragmentsArray.get(mChosenPatternFragmentNumber).makeTracksViews();
                   patternNumber.setText((mChosenPatternFragmentNumber+1)+"");
               }
           });

           prevPattern.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (mChosenPatternFragmentNumber != 0) {
                       mChosenPatternFragmentNumber--;
                       getFragmentManager().beginTransaction().replace(R.id.fragment, mPatternFragmentsArray.get(mChosenPatternFragmentNumber)).commit();
                   }
                   patternNumber.setText((mChosenPatternFragmentNumber+1)+"");
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(BILL_TOTAL, currentBillTotal);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("OnStart","Worked");
    }


    private void addPattern(){
        mChosenPatternFragmentNumber++;
        PatternFragment pf = new PatternFragment();
        mPatternFragmentsArray.add(pf);
        mPatternFragmentsArray.get(mChosenPatternFragmentNumber).makePatternForFragment(this.getApplicationContext());
        getFragmentManager().beginTransaction().replace(R.id.fragment, mPatternFragmentsArray.get(mChosenPatternFragmentNumber)).commit();
        Log.d("NEW FRAGMENT", pf.isAdded()+"");
    }


    class bpmPickerHandler implements NumberPicker.OnValueChangeListener {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            Sampler.getSampler().setBPM(newVal);
        }
    }
}