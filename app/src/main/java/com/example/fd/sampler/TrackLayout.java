package com.example.fd.sampler;

/**
 * Created by BDV on 14.05.2016.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


import java.util.ArrayList;


public class TrackLayout extends LinearLayout {
    private ArrayList<HitView> mHitsArray = null;
    private boolean isWannaWatch = false;


    public TrackLayout(Context context) {
        super(context);
        initComponent();
    }
    public TrackLayout(Context context, AttributeSet attrs){
        super(context,attrs);
        initComponent();
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
        updateFields();
    }

    private void updateFields() {

    }

    public void setChannelName(String name) {
    }

    public void setChannelLogo(int resourceId) {
    }

    public void setChannelLogo(Bitmap image) {
    }

    public void setProgramTime(String time) {
    }

    public void setProgramName(String name) {

    }

    public void setProgramDescription(String name) {
    }

    private final OnClickListener buttonListener = new OnClickListener() {
        public void onClick(View view) {
            isWannaWatch = !isWannaWatch;
            updateFields();
        }
    };



    private void updateFieldsByParent() {

    }
}
