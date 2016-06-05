package com.example.fd.sampler;

import android.widget.Button;
import android.widget.SeekBar;
import android.widget.ToggleButton;

/**
 * Created by FD on 05.06.2016.
 */
public interface TrackInterface {
    Button getTrackName();
     SeekBar getVolumeSlider();
    ToggleButton getMuteBtn();

}
