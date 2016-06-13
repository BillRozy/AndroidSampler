package com.example.fd.sampler;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;


public class TrackLayout extends LinearLayout implements TrackInterface {
    private ArrayList<HitView> mHitsArray = null;
    private ImageButton connectInstrumentBtn;
    private ImageButton mDeleteBtn;
    private Button mTrackName;


    public TrackLayout(Context context) {
        super(context);
        initComponent();
    }

    public TrackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    public ArrayList<HitView> getHitsArray() {
        return mHitsArray;
    }

    public ImageButton getConnectInstrumentBtn() {
        return connectInstrumentBtn;
    }

    public ImageButton getDeleteBtn() {
        return mDeleteBtn;
    }

    public Button getTrackName() {
        return mTrackName;
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.track_layout, this);
        mHitsArray = new ArrayList<>();
        mHitsArray.add((HitView) findViewById(R.id.hit1));
        mHitsArray.add((HitView) findViewById(R.id.hit2));
        mHitsArray.add((HitView) findViewById(R.id.hit3));
        mHitsArray.add((HitView) findViewById(R.id.hit4));
        mHitsArray.add((HitView) findViewById(R.id.hit5));
        mHitsArray.add((HitView) findViewById(R.id.hit6));
        mHitsArray.add((HitView) findViewById(R.id.hit7));
        mHitsArray.add((HitView) findViewById(R.id.hit8));
        mHitsArray.add((HitView) findViewById(R.id.hit9));
        mHitsArray.add((HitView) findViewById(R.id.hit10));
        mHitsArray.add((HitView) findViewById(R.id.hit11));
        mHitsArray.add((HitView) findViewById(R.id.hit12));
        mHitsArray.add((HitView) findViewById(R.id.hit13));
        mHitsArray.add((HitView) findViewById(R.id.hit14));
        mHitsArray.add((HitView) findViewById(R.id.hit15));
        mHitsArray.add((HitView) findViewById(R.id.hit16));
        connectInstrumentBtn = (ImageButton) findViewById(R.id.connectInstrumentButton);
        mDeleteBtn = (ImageButton) findViewById(R.id.delete);
        mTrackName = (Button) findViewById(R.id.trackNameView);
    }

}
