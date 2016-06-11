package com.example.fd.sampler;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends Activity implements PatternFragment.PatternFragmentInterface {
    final private int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL = 1;
    final private int MY_PERMISSIONS_REQUEST_READ_EXTERNAL = 2;
    static final String FILES_DIRECTORY_PRESETS = android.os.Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/DrumSampler/Presets/";
    private Sampler myApp;
    private DataBaseHelper mDatabaseHelper;
    public Button play;
    private ToggleButton mixerButton;
    private TextView patternNumber;
    private NumberPicker bpmPicker;
    private NumberPicker stepPicker;
    private  TextView presetName;
    private ArrayList<PatternFragment> mPatternFragmentsArray = null;
    private SharedPreferences sp;
    public String pathChosen = "";
    public String nameChosen = "";
    public String presetChosen = "";
    public int trackChosen = 0;
    private int mChosenPatternFragmentNumber = 0;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_COPIED = "file_copied";
    static final private int CHOOSE_SAMPLE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(!sp.contains(APP_PREFERENCES_COPIED) || sp.getInt(APP_PREFERENCES_COPIED,0)==0){
            if(android.os.Build.VERSION.SDK_INT >= 23) {
                int permissionCheck = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL);
                }
            }
            else{
                SharedPreferences.Editor spEditor = sp.edit();
                File dir = new File(FileBrowserActivity.FILES_DIRECTORY);
                if (dir.mkdir()) {
                    Log.d("Created", dir.getAbsolutePath());
                    myCopy();
                }
                spEditor.putInt(APP_PREFERENCES_COPIED,1);
                spEditor.commit();
            }
        }
        myApp = Sampler.getSampler();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
       if ( mPatternFragmentsArray == null ) {
           mPatternFragmentsArray = new ArrayList<>();
       }
        bpmPicker = (NumberPicker) this.findViewById(R.id.numberPicker);
        bpmPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        bpmPicker.setMaxValue(400);
        bpmPicker.setMinValue(60);
        setNumberPickerTextColor(bpmPicker, Color.BLACK);
        stepPicker = (NumberPicker) findViewById(R.id.stepPicker);
        stepPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        stepPicker.setMaxValue(16);
        stepPicker.setMinValue(4);
        setNumberPickerTextColor(stepPicker, Color.BLACK);
        initOnCreate();
        Button addTrack = (Button) this.findViewById(R.id.addTrackButton);
        Button stop = (Button) this.findViewById(R.id.stopButton);
        Button pause = (Button) this.findViewById(R.id.pauseButton);
           play = (Button) this.findViewById(R.id.playButton);
        Button nextPattern = (Button) this.findViewById(R.id.nextPatt);
        Button prevPattern = (Button) this.findViewById(R.id.prevPattern);
        Button savePresetBtn = (Button) findViewById(R.id.saveBtn);
        Button loadPresetBtn = (Button) findViewById(R.id.loadBtn);
            patternNumber = (TextView) this.findViewById(R.id.numPattern);
            patternNumber.setText((mChosenPatternFragmentNumber+1)+"");
        mixerButton = (ToggleButton) this.findViewById(R.id.mixerButton);
        Button parseSiteBtn = (Button) findViewById(R.id.siteParseBtn);

        presetName = (TextView) findViewById(R.id.presetName);
        presetName.setText(myApp.getActivePattern().getPatternName());

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
                   play.setBackgroundResource(R.drawable.play_red);
               }
           });


           stop.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   Sampler.getSampler().stop();
                   for (TrackLayout tl : mPatternFragmentsArray.get(mChosenPatternFragmentNumber).getTracksLayoutsArray()) {
                       tl.getDeleteBtn().setEnabled(true);
                   }
                   play.setBackgroundResource(R.drawable.play_white);
               }
           });


           pause.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Sampler.getSampler().pause();
                   for (TrackLayout tl : mPatternFragmentsArray.get(mChosenPatternFragmentNumber).getTracksLayoutsArray()) {
                       tl.getDeleteBtn().setEnabled(true);
                   }
                   play.setBackgroundResource(R.drawable.play_white);
               }
           });

           nextPattern.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   boolean wasPlaying = false;
                   if(myApp.isPlaying()) {
                       wasPlaying = true;
                       myApp.stop();
                       play.setBackgroundResource(R.drawable.play_white);
                   }
                   saveStateInDatabase();
                   if (mChosenPatternFragmentNumber == (myApp.getSizeOfProgramm() - 1)) {
                       initOnCreate();
                       addPattern();
                   } else {
                       initOnCreate();
                       mChosenPatternFragmentNumber++;
                       myApp.setPatternActive(myApp.getPatternsList().get(mChosenPatternFragmentNumber));
                      getFragmentManager().beginTransaction().replace(R.id.fragment, mPatternFragmentsArray.get(mChosenPatternFragmentNumber)).commit();
                   }
                   patternNumber.setText((mChosenPatternFragmentNumber+1)+"");
                   presetName.setText(myApp.getActivePattern().getPatternName());
                   if(wasPlaying){
                       myApp.play();
                       play.setBackgroundResource(R.drawable.play_white);
                   }
                   mixerButton.setChecked(false);
               }
           });

           prevPattern.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   boolean wasPlaying = false;
                   if(myApp.isPlaying()) {
                       wasPlaying = true;
                       myApp.stop();
                       play.setBackgroundResource(R.drawable.play_white);
                   }
                   saveStateInDatabase();
                   if (mChosenPatternFragmentNumber != 0) {
                       initOnCreate();
                       mChosenPatternFragmentNumber--;
                       myApp.setPatternActive(myApp.getPatternsList().get(mChosenPatternFragmentNumber));
                       getFragmentManager().beginTransaction().replace(R.id.fragment, mPatternFragmentsArray.get(mChosenPatternFragmentNumber)).commit();
                   }
                   patternNumber.setText((mChosenPatternFragmentNumber+1)+"");
                   presetName.setText(myApp.getActivePattern().getPatternName());
                   if(wasPlaying){
                       myApp.play();
                       play.setBackgroundResource(R.drawable.play_white);
                   }
                   mixerButton.setChecked(false);
               }
           });

        parseSiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SiteParserActivity.class);
                startActivity(intent);
            }
        });

        mixerButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    MixerFragment mf = new MixerFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment, mf).commit();
                }
                else{
                    getFragmentManager().beginTransaction().replace(R.id.fragment, mPatternFragmentsArray.get(mChosenPatternFragmentNumber)).commit();
                }
            }
        });

        savePresetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Write name of preset");
                alertDialog.setMessage("Enter Preset Name");

                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.file_icon);

                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String title = input.getText().toString();
                                if (title.compareTo("") != 0) {
                                    savePreset(title);
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
        });

        loadPresetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FileBrowserActivity.class);
                startActivityForResult(intent, CHOOSE_SAMPLE);
            }
        });



        bpmPicker.setOnValueChangedListener(new bpmPickerHandler());

        stepPicker.setOnValueChangedListener(new stepsPickerHandler());

           Sampler.getSampler().stepsBar = (ProgressBar) this.findViewById(R.id.progressBar);
           Sampler.getSampler().stepsBar.setMax(16);
           Sampler.getSampler().stepsBar.setProgress(0);

        bpmPicker.setValue(myApp.getActivePattern().getPatternBPM());
        stepPicker.setValue(myApp.getActivePattern().getPatternSteps());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_SAMPLE) {
            if (resultCode == -1) {
                initOnCreate();
                loadPreset(data.getStringExtra(SampleListActivity.mSelectedSamplePath));
            }
        }
    }

    private void savePreset(String name){
        try {
            myApp.getActivePattern().setPatternName(name);
            Preset preset = new Preset();
            preset.setBPM(myApp.getActivePattern().getPatternSteps());
            preset.setTitle(name);
            preset.setSteps(myApp.getActivePattern().getPatternSteps());
            preset.setTrackTitles(myApp.getAllTracksNames());
            preset.setActiveHits(myApp.getAllTracksHitsDescription());
            preset.setPathsToSamples(myApp.getAllTracksPaths());
            preset.setHasSample(myApp.getExistenceOfSample());
            preset.setVolumesArray(myApp.getAllTracksVolumes());
            FileOutputStream fos = new FileOutputStream(FILES_DIRECTORY_PRESETS + name + ".dmp");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(preset);
            oos.flush();
            oos.close();
            presetName.setText(name);
        }catch (IOException exc){Log.d("EXCEPTION", exc.toString());}
    }

    public void loadPreset(String Path){
        try {
            FileInputStream fis = new FileInputStream(Path);
            ObjectInputStream oin = new ObjectInputStream(fis);
            Preset preset = (Preset) oin.readObject();
            String[] titles = preset.getTrackTitles();
            String[] description = preset.getActiveHits();
            String[] paths = preset.getPathsToSamples();
            Boolean[] mark = preset.getHasSample();
            float[] volumes = preset.getVolumesArray();
            Pattern patt = new Pattern(this);
            patt.setPatternName(preset.getTitle());
            patt.setPatternBPM(preset.getBPM());
            patt.setPatternSteps(preset.getSteps());
            for(int i=1;i<titles.length;i++){
                Track track = patt.addTrack(titles[i]);
                track.makeHitsActiveFromDescription(description[i]);
                track.setTrackVolume(volumes[i]);
                if(paths[i] != null) {
                    track.connectInstrument(this, paths[i]);
                }
                track.setHasConnectedInstrument(mark[i]);
                Log.d("Active ", description[i]);
            }
            myApp.getPatternsList().set(myApp.getPatternsList().indexOf(myApp.getActivePattern()),patt);
            myApp.setPatternActive(patt);
            PatternFragment pf = new PatternFragment();
            pf.connectPattern(patt);
            mPatternFragmentsArray.set(mChosenPatternFragmentNumber,pf);
            getFragmentManager().beginTransaction().replace(R.id.fragment, mPatternFragmentsArray.get(mChosenPatternFragmentNumber)).commit();
            presetName.setText(patt.getPatternName());
            stepPicker.setValue(patt.getPatternSteps());
            bpmPicker.setValue(patt.getPatternBPM());
        }catch(IOException | ClassNotFoundException exc){Log.d("EXCEPTION", exc.toString());}

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
        bpmPicker.setValue(myApp.getActivePattern().getPatternBPM());
        stepPicker.setValue(myApp.getActivePattern().getPatternSteps());
        getFragmentManager().beginTransaction().replace(R.id.fragment, mPatternFragmentsArray.get(mChosenPatternFragmentNumber)).commit();
        Log.d("NEW FRAGMENT", pf.isAdded()+"");
    }

    private void initOnCreate(){
        mChosenPatternFragmentNumber = myApp.getLastActivePatternIndex();
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
            myApp.addPattern(patt);
            myApp.setPatternActive(patt);
        }
        patternCursor.close();

        Cursor trackCursor = sdb.query(mDatabaseHelper.DATABASE_TABLE_TRACKS, new String[]{
                        DataBaseHelper._ID, DataBaseHelper.TRACK_TITLE_COLUMN,
                        DataBaseHelper.TRACK_HITS_ARRAY_COLUMN, DataBaseHelper.TRACK_VOLUME_COLUMN,
                        DataBaseHelper.TRACK_MUTE_COLUMN,
                        DataBaseHelper.TRACK_PATH_TO_SAMPLE_COLUMN,
                        DataBaseHelper.TRACK_PATTERN_ID_COLUMN,
                        DataBaseHelper.TRACK_HAS_CONNECTED_SAMPLE}, null, null, null, null, null);

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
                }
                int hasConnectedSample = trackCursor.getInt(trackCursor.getColumnIndex(DataBaseHelper.TRACK_HAS_CONNECTED_SAMPLE));
                float volume = ((float)trackCursor.getInt(trackCursor.getColumnIndex(DataBaseHelper.TRACK_VOLUME_COLUMN))/100);
                int mute = trackCursor.getInt(trackCursor.getColumnIndex(DataBaseHelper.TRACK_MUTE_COLUMN));
                String path = trackCursor.getString(trackCursor.getColumnIndex(DataBaseHelper.TRACK_PATH_TO_SAMPLE_COLUMN));
                Pattern patt = myApp.getPattern(pattID);
                    Log.d("FOUNDPATTERN FOR TRACK!", (pattID-1) + "");
                    Track track = patt.addTrack(trackName);
                     track.setTrackVolume(volume);
                if(activeHitsArray[0] != 0) {
                    track.makeHitActive(activeHitsArray);
                }
            if (hasConnectedSample == 1) {
                track.connectInstrument(this, path);
            }
        }
        trackCursor.close();

        Pattern temp = myApp.getPattern(mChosenPatternFragmentNumber+1);
        myApp.setPatternActive(temp);
        if(mChosenPatternFragmentNumber == -1){
            myApp.setPatternActive(myApp.getPattern(1));
        }
        myApp.setBPM(temp.getPatternBPM());
        myApp.setSteps(temp.getPatternSteps());
        for(Pattern patt : myApp.getPatternsList()){
            PatternFragment pf = new PatternFragment();
            pf.connectPattern(patt);
            mPatternFragmentsArray.add(pf);
            if(mPatternFragmentsArray.indexOf(pf) == mChosenPatternFragmentNumber){
                getFragmentManager().beginTransaction().replace(R.id.fragment, mPatternFragmentsArray.get(mChosenPatternFragmentNumber)).commit();
            }

        }

        sdb.close();
    }


    class bpmPickerHandler implements NumberPicker.OnValueChangeListener {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            myApp.setBPM(newVal);
            myApp.getActivePattern().setPatternBPM(newVal);
        }
    }

    class stepsPickerHandler implements NumberPicker.OnValueChangeListener{
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            myApp.setSteps(newVal);
            myApp.getActivePattern().setPatternBPM(newVal);
        }
    }

    @Override
    protected void onPause() {
       super.onPause();
        saveStateInDatabase();
        Log.d("OnPause", "WORKED");

    }

    private void saveStateInDatabase(){
        if(myApp.getActivePattern()!=null) {
            myApp.stop();
            myApp.setLastPatternActiveIndex();
            myApp.setSizeOfProgramm();
            myApp.setMuseNull();
            SQLiteDatabase mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
            int deleted = mSqLiteDatabase.delete(mDatabaseHelper.DATABASE_TABLE_PATTERNS, "1", null);
            Log.d("REMOVED FROM PATTERNS", deleted + "");
            mSqLiteDatabase.delete(mDatabaseHelper.DATABASE_TABLE_TRACKS, "1", null);
            for (int i = 0; i < myApp.getPatternsList().size(); i++) {
                Pattern patt = myApp.getPattern(i + 1);
                ContentValues values = new ContentValues();

                values.put(DataBaseHelper.PATTERN_NAME_COLUMN, patt.getPatternName());
                values.put(DataBaseHelper.PATTERN_BPM_COLUMN, patt.getPatternBPM());
                values.put(DataBaseHelper.PATTERN_STEP_COLUMN, patt.getPatternSteps());

                mSqLiteDatabase.insert(mDatabaseHelper.DATABASE_TABLE_PATTERNS, null, values);
                for (int j = 1; j < patt.getTracksArray().size(); j++) {
                    Track track = patt.getTrack(j);
                    ArrayList<Track.Hit> hitsArray = track.getHits();
                    String activeHits = "";
                    for (Track.Hit hit : hitsArray) {
                        if (hit.getState()) {
                            activeHits = activeHits + (hitsArray.indexOf(hit) + 1) + " ";
                        }
                    }
                    if (activeHits.length() > 2) {
                        activeHits = activeHits.substring(0, activeHits.length() - 1);
                    }
                    Log.d("active hits: ", activeHits);
                    ContentValues trackData = new ContentValues();
                    trackData.put(DataBaseHelper.TRACK_TITLE_COLUMN, track.getTrackName());
                    trackData.put(DataBaseHelper.TRACK_HITS_ARRAY_COLUMN, activeHits);
                    int volume = (int) (track.getTrackVolume()*100);
                    trackData.put(DataBaseHelper.TRACK_VOLUME_COLUMN, volume);
                    trackData.put(DataBaseHelper.TRACK_MUTE_COLUMN, false);
                    trackData.put(DataBaseHelper.TRACK_PATH_TO_SAMPLE_COLUMN, track.getPathToInstrument());
                    trackData.put(DataBaseHelper.TRACK_PATTERN_ID_COLUMN, i + 1);
                    if (track.getHasConnectedInstrument()) {
                        trackData.put(DataBaseHelper.TRACK_HAS_CONNECTED_SAMPLE, 1);
                    } else {
                        trackData.put(DataBaseHelper.TRACK_HAS_CONNECTED_SAMPLE, 0);
                    }
                    mSqLiteDatabase.insert(mDatabaseHelper.DATABASE_TABLE_TRACKS, null, trackData);
                }
                Log.d("PATTERN DDED TO BD ", i + "");
            }
            mSqLiteDatabase.close();
            myApp.clearPools();
            // myApp.interruptAllThreads();
            myApp.clearPatternsList();
            myApp.clearActivePattern();
            //myApp.getPatternsList().clear();
            mPatternFragmentsArray.clear();
        }
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                }
                catch(NoSuchFieldException | IllegalAccessException e){
                    Log.w("setNumberPickerTexColor", e);
                }
            }
        }
        return false;
    }

    @Override
    public void putDataAboutTrack(int num, String path, String name) {

        initOnCreate();
        trackChosen = num;
        pathChosen = path;
        nameChosen = name;
        myApp.getActivePattern().getTrack(num).connectInstrument(this,path);
        myApp.getActivePattern().getTrack(num).setTrackName(name);
    }

    private void myCopy(){
        AssetManager assetManager = this.getAssets();
        String assets[] = null;
        try {
            InputStream indexFile = assetManager.open("assets.index");
            String test = convertStreamToString(indexFile);
            assets = test.split("\\n");
        }catch (IOException exc){Log.d("ERROR",exc.toString());}
            for(String url : assets){
                File file = new File(url);
                if(file.getName().contains(".")) {
                    copyFile(url);
                }
                else
                {
                    File dir = new File(FileBrowserActivity.FILES_DIRECTORY + url);
                    boolean success = dir.mkdirs();
                }
            }
        }

    private void copyFile(String filename) {
        AssetManager assetManager = this.getAssets();
        InputStream in;
        OutputStream out;
        String newFileName = null;
        try {
            Log.i("tag", "copyFile() "+filename);
            in = assetManager.open(filename);
            if (filename.endsWith(".jpg")) // extension was added to avoid compression on APK file
                newFileName = FileBrowserActivity.FILES_DIRECTORY + filename.substring(0, filename.length()-4);
            else
                newFileName = FileBrowserActivity.FILES_DIRECTORY + filename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.e("tag", "Exception in copyFile() of "+newFileName);
            Log.e("tag", "Exception in copyFile() "+e.toString());
        }

    }

    static String convertStreamToString(java.io.InputStream is) {
        Scanner scanner = new Scanner(is);
        String text = scanner.useDelimiter("\\A").next();
        scanner.close();
        return text;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    SharedPreferences.Editor spEditor = sp.edit();
                    File dir = new File(FileBrowserActivity.FILES_DIRECTORY);
                    if (dir.mkdir()) {
                        Log.d("Created", dir.getAbsolutePath());
                        myCopy();
                    }
                    spEditor.putInt(APP_PREFERENCES_COPIED,1);
                    spEditor.commit();
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}