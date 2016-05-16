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


import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by BDV on 14.05.2016.
 */
public class PatternLayout extends LinearLayout {
    private ArrayList<TrackLayout> tracksArray = null;

    public PatternLayout(Context context) {
        super(context);
        this.setOrientation(LinearLayout.VERTICAL);
        tracksArray = new ArrayList<>();
    }
    public PatternLayout(Context context, AttributeSet attrs){
        super(context,attrs);
        this.setOrientation(LinearLayout.VERTICAL);
        tracksArray = new ArrayList<>();

    }

    public void addTrackLayout(TrackLayout tl){
        tracksArray.add(tl);
        this.addView(tl);
    }

    public void deleteTrackLayout(int num){
        tracksArray.remove(num);
    }
}
