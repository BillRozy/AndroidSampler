package com.example.fd.sampler;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.ToggleButton;

/**
 * Created by FD on 05.06.2016.
 */
public class MixerTrackLayout extends LinearLayout implements TrackInterface {
    private SeekBar mVolumeBar;
    private ToggleButton mMuteBtn;
    private Button mNameBtn;

    public MixerTrackLayout(Context context ){
        super(context);
        initComponent();
    }

    public MixerTrackLayout(Context context, AttributeSet attrs){
        super(context,attrs);
        initComponent();

    }

    public Button getTrackName() {
        return mNameBtn;
    }

    public SeekBar getVolumeSlider() {
        return mVolumeBar;
    }

    public ToggleButton getMuteBtn() {
        return mMuteBtn;
    }

    public void setNameBtn(Button mNameBtn) {
        this.mNameBtn = mNameBtn;
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.mixer_track_layout, this);
        mVolumeBar = (SeekBar) findViewById(R.id.volumeBar);
        mMuteBtn = (ToggleButton) findViewById(R.id.muteBtn);
        mNameBtn = (Button) findViewById(R.id.trackName);
    }
}
