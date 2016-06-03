package com.example.fd.sampler;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
    private DataBaseHelper mDatabaseHelper;
    private Button addTrack;
    private Button stop;
    private Button play;
    private Button pause;
    private Button nextPattern;
    private Button prevPattern;
    private Button fileBrowseBtn;
    private TextView patternNumber;
    private ArrayList<PatternFragment> mPatternFragmentsArray = null;
    private SharedPreferences sp;
    private int mChosenPatternFragmentNumber = 0;
    public static final String APP_PREFERENCES = "mysettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        myApp = Sampler.getSampler();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
       if ( mPatternFragmentsArray == null ) {
           mPatternFragmentsArray = new ArrayList<>();
       }
        initOnCreate();
           addTrack = (Button) this.findViewById(R.id.addTrackButton);
           stop = (Button) this.findViewById(R.id.stopButton);
           pause = (Button) this.findViewById(R.id.pauseButton);
           play = (Button) this.findViewById(R.id.playButton);
           nextPattern = (Button) this.findViewById(R.id.nextPatt);
           prevPattern = (Button) this.findViewById(R.id.prevPattern);
            patternNumber = (TextView) this.findViewById(R.id.numPattern);
            patternNumber.setText((mChosenPatternFragmentNumber+1)+"");
        fileBrowseBtn = (Button) this.findViewById(R.id.fileBrowseBtn);

           Log.d("OnCreate", "WOrked");

           addTrack.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Track curTr = myApp.getActivePattern().addTrack("Track: " + myApp.getActivePattern().getTrackCounter());
                   TrackLayout tl = new TrackLayout(getApplicationContext());
                   tl.getTrackName().setText(curTr.getTrackName());
                   mPatternFragmentsArray.get(mChosenPatternFragmentNumber).addTrackLayout(tl);
                   mPatternFragmentsArray.get(mChosenPatternFragmentNumber).addViewToFragment(tl);
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
                       myApp.setPatternActive(myApp.getPattern(mChosenPatternFragmentNumber+1));
                       getFragmentManager().beginTransaction().replace(R.id.fragment, mPatternFragmentsArray.get(mChosenPatternFragmentNumber)).commit();

                   }
                   patternNumber.setText((mChosenPatternFragmentNumber+1)+"");
               }
           });

           prevPattern.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (mChosenPatternFragmentNumber != 0) {
                       mChosenPatternFragmentNumber--;
                       myApp.setPatternActive(myApp.getPattern(mChosenPatternFragmentNumber+1));
                       getFragmentManager().beginTransaction().replace(R.id.fragment, mPatternFragmentsArray.get(mChosenPatternFragmentNumber)).commit();
                   }
                   patternNumber.setText((mChosenPatternFragmentNumber+1)+"");
               }
           });

        fileBrowseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FileBrowserActivity.class);
                startActivity(intent);
            }
        });


           NumberPicker bpmPicker = (NumberPicker) this.findViewById(R.id.numberPicker);
        bpmPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
           bpmPicker.setMaxValue(400);
           bpmPicker.setMinValue(60);
           bpmPicker.setValue(Sampler.getSampler().getBPM());
           bpmPicker.setOnValueChangedListener(new bpmPickerHandler());

           Sampler.getSampler().stepsBar = (ProgressBar) this.findViewById(R.id.progressBar);
           Sampler.getSampler().stepsBar.setMax(16);
           Sampler.getSampler().stepsBar.setProgress(0);



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
        mPatternFragmentsArray.get(mChosenPatternFragmentNumber).makePatternForFragment(this);
        getFragmentManager().beginTransaction().replace(R.id.fragment, mPatternFragmentsArray.get(mChosenPatternFragmentNumber)).commit();
        Log.d("NEW FRAGMENT", pf.isAdded()+"");
    }

    private void initOnCreate(){
        myApp.clearPatternsList();
        mDatabaseHelper = new DataBaseHelper(this, "mainbase.db", null, 1);
        SQLiteDatabase sdb;
        sdb = mDatabaseHelper.getReadableDatabase();
        Cursor patternCursor = sdb.query(mDatabaseHelper.DATABASE_TABLE_PATTERNS, new String[]{
                        DataBaseHelper._ID, DataBaseHelper.PATTERN_NAME_COLUMN, DataBaseHelper.PATTERN_BPM_COLUMN
        , DataBaseHelper.PATTERN_STEP_COLUMN}, null,
                null,
                null,
                null,
                null
        );

        int pos = 1;
        while (patternCursor.moveToNext()) {
            int id = patternCursor.getInt(patternCursor.getColumnIndex(DataBaseHelper._ID));
            String name = patternCursor.getString(patternCursor
                    .getColumnIndex(DataBaseHelper.PATTERN_NAME_COLUMN));
            int bpm = patternCursor.getInt(patternCursor.getColumnIndex(DataBaseHelper.PATTERN_BPM_COLUMN));
            int steps = patternCursor.getInt(patternCursor.getColumnIndex(DataBaseHelper.PATTERN_STEP_COLUMN));

            Log.i("LOG_TAG", "ROW " + id + " HAS NAME " + name);
            Pattern patt = new Pattern(this);
            patt.setPatternName(name);
            patt.setPatternBPM(bpm);
            patt.setPatternSteps(steps);
            Sampler.getSampler().addPattern(patt);
            Sampler.getSampler().setPatternActive(patt);
        }
        patternCursor.close();

        Cursor trackCursor = sdb.query(mDatabaseHelper.DATABASE_TABLE_TRACKS, new String[]{
                        DataBaseHelper._ID, DataBaseHelper.TRACK_TITLE_COLUMN,
                        DataBaseHelper.TRACK_HITS_ARRAY_COLUMN, DataBaseHelper.TRACK_VOLUME_COLUMN,
                        DataBaseHelper.TRACK_MUTE_COLUMN,
                        DataBaseHelper.TRACK_PATH_TO_SAMPLE_COLUMN,
                        DataBaseHelper.TRACK_PATTERN_ID_COLUMN}, null, null, null, null, null);

        while(trackCursor.moveToNext()){
                int trackID = trackCursor.getInt(trackCursor.getColumnIndex(DataBaseHelper._ID));
                int pattID = trackCursor.getInt(trackCursor.getColumnIndex(DataBaseHelper.TRACK_PATTERN_ID_COLUMN));
                String trackName = trackCursor.getString(trackCursor.getColumnIndex(DataBaseHelper.TRACK_TITLE_COLUMN));
                Log.i("LOG_TAG", "TRACK " + trackID + " HAS NAME " + trackName);
                String[] hitsString = trackCursor.getString(trackCursor.getColumnIndex(DataBaseHelper.TRACK_HITS_ARRAY_COLUMN)).split(" ");
                int activeHitsArray[] = new int[hitsString.length];
                for (int i = 0; i < hitsString.length; i++) {
                        if(!hitsString[i].equals("")) {
                            activeHitsArray[i] = Integer.parseInt(hitsString[i]);
                        }
                    // System.out.println(numArr[i]);
                }
                int volume = trackCursor.getInt(trackCursor.getColumnIndex(DataBaseHelper.TRACK_VOLUME_COLUMN));
                int mute = trackCursor.getInt(trackCursor.getColumnIndex(DataBaseHelper.TRACK_MUTE_COLUMN));
                String path = trackCursor.getString(trackCursor.getColumnIndex(DataBaseHelper.TRACK_PATH_TO_SAMPLE_COLUMN));
                Pattern patt = myApp.getPattern(pattID);
                    Log.d("FOUNDPATTERN FOR TRACK!", (pattID-1) + "");
                    Track track = patt.addTrack(trackName);
                if(activeHitsArray[0] != 0) {
                    track.makeHitActive(activeHitsArray);
                }
                    track.setTrackVolume((float) volume);
            if (path != null) {
                track.connectInstrument(this, path);
            }
        }
        trackCursor.close();

        mChosenPatternFragmentNumber = myApp.getPatternsList().indexOf(myApp.getActivePattern());
        for(Pattern patt : myApp.getPatternsList()){
            PatternFragment pf = new PatternFragment();
            pf.connectPattern(patt);
            mPatternFragmentsArray.add(pf);
            if(mPatternFragmentsArray.indexOf(pf) == mChosenPatternFragmentNumber){
                getFragmentManager().beginTransaction().replace(R.id.fragment, mPatternFragmentsArray.get(mChosenPatternFragmentNumber)).commit();
            }

        }
        sdb.close();
      //  myApp.clearPatternsList();
    }


    class bpmPickerHandler implements NumberPicker.OnValueChangeListener {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            myApp.setBPM(newVal);
        }
    }

    @Override
    protected void onPause() {
       super.onPause();
        Log.d("OnPause", "WORKED");
        mDatabaseHelper = new DataBaseHelper(this, "mainbase.db", null, 1);

        SQLiteDatabase mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        int deleted = mSqLiteDatabase.delete(mDatabaseHelper.DATABASE_TABLE_PATTERNS, "1", null);
        Log.d("REMOVED FROM PATTERNS" , deleted+"");
        mSqLiteDatabase.delete(mDatabaseHelper.DATABASE_TABLE_TRACKS, "1", null);
        for( int i = 0;i < myApp.getPatternsList().size(); i++ ) {
            Pattern patt = myApp.getPattern(i+1);
            ContentValues values = new ContentValues();

            values.put(DataBaseHelper.PATTERN_NAME_COLUMN, patt.getPatternName());
            values.put(DataBaseHelper.PATTERN_BPM_COLUMN, patt.getPatternBPM());
            values.put(DataBaseHelper.PATTERN_STEP_COLUMN, patt.getPatternSteps());
            // Вставляем данные в таблицу
            //int res = (int) mSqLiteDatabase.insertWithOnConflict(mDatabaseHelper.DATABASE_TABLE_PATTERNS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            //if (res == -1) {
              //  mSqLiteDatabase.update(mDatabaseHelper.DATABASE_TABLE_PATTERNS, values, "_ID=?", new String[] {Integer.toString(i+1)});  // number 1 is the _id here, update to variable for your code
            //}
            mSqLiteDatabase.insert(mDatabaseHelper.DATABASE_TABLE_PATTERNS, null, values);
            for(int j = 1; j < patt.getTracksArray().size();j++){
                Track track = patt.getTrack(j);
                ArrayList<Track.Hit> hitsArray = track.getHits();
                String activeHits = "";
                for (Track.Hit hit : hitsArray){
                    if(hit.getState()){
                        activeHits = activeHits + (hitsArray.indexOf(hit) + 1) + " ";
                    }
                }
                if (activeHits.length() > 2) {
                    activeHits = activeHits.substring(0, activeHits.length() - 1);
                }
                Log.d("active hits: ", activeHits);
                ContentValues trackData = new ContentValues();
                trackData.put(DataBaseHelper.TRACK_TITLE_COLUMN,track.getTrackName());
                trackData.put(DataBaseHelper.TRACK_HITS_ARRAY_COLUMN, activeHits);
                trackData.put(DataBaseHelper.TRACK_VOLUME_COLUMN,70);
                trackData.put(DataBaseHelper.TRACK_MUTE_COLUMN, false);
                trackData.put(DataBaseHelper.TRACK_PATH_TO_SAMPLE_COLUMN, track.getPathToInstrument());
                trackData.put(DataBaseHelper.TRACK_PATTERN_ID_COLUMN, i+1);
                mSqLiteDatabase.insert(mDatabaseHelper.DATABASE_TABLE_TRACKS, null, trackData);
            }
            Log.d("PATTERN DDED TO BD ", i+"");
        }
        mSqLiteDatabase.close();
    }
}