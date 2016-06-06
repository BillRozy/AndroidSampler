package com.example.fd.sampler;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by FD on 06.06.2016.
 */
public class StepsLayout extends LinearLayout {
    private ArrayList<ToggleButton> mStepsArray = null;



    public StepsLayout(Context context) {
        super(context);
        initComponent();
    }
    public StepsLayout(Context context, AttributeSet attrs){
        super(context,attrs);
        initComponent();
    }

    public ArrayList<ToggleButton> getStepsArray() {
        return mStepsArray;
    }

    public void initComponent(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.steps_layout, this);
        mStepsArray = new ArrayList<>();
        mStepsArray.add((ToggleButton) findViewById(R.id.step1));
        mStepsArray.add((ToggleButton) findViewById(R.id.step2));
        mStepsArray.add((ToggleButton) findViewById(R.id.step3));
        mStepsArray.add((ToggleButton) findViewById(R.id.step4));
        mStepsArray.add((ToggleButton) findViewById(R.id.step5));
        mStepsArray.add((ToggleButton) findViewById(R.id.step6));
        mStepsArray.add((ToggleButton) findViewById(R.id.step7));
        mStepsArray.add((ToggleButton) findViewById(R.id.step8));
        mStepsArray.add((ToggleButton) findViewById(R.id.step9));
        mStepsArray.add((ToggleButton) findViewById(R.id.step10));
        mStepsArray.add((ToggleButton) findViewById(R.id.step11));
        mStepsArray.add((ToggleButton) findViewById(R.id.step12));
        mStepsArray.add((ToggleButton) findViewById(R.id.step13));
        mStepsArray.add((ToggleButton) findViewById(R.id.step14));
        mStepsArray.add((ToggleButton) findViewById(R.id.step15));
        mStepsArray.add((ToggleButton) findViewById(R.id.step16));

    }
}
