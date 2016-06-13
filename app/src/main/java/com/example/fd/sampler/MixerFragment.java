package com.example.fd.sampler;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.util.ArrayList;


public class MixerFragment extends Fragment {
    private ArrayList<MixerTrackLayout> mixerTracksArray = null;

    public MixerFragment() {
        super();
        mixerTracksArray = new ArrayList<>(8);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup selfFrame = (HorizontalScrollView) inflater.inflate(R.layout.fragment_mixer, container, false);
        LinearLayout containerForTracks = (LinearLayout) selfFrame.findViewById(R.id.containerForMixerTracks);
        final ArrayList<Track> trackList = Sampler.getSampler().getActivePattern().getTracksArray();
        for (final Track track : trackList) {
            if (trackList.indexOf(track) != 0) {
                MixerTrackLayout mtl = new MixerTrackLayout(getActivity());
                mtl.getTrackName().setText(track.getTrackName());
                mtl.getVolumeSlider().setProgress((int) (track.getTrackVolume() * 100));
                remakeTracks(mtl);
                mixerTracksArray.add(mtl);
                containerForTracks.addView(mtl);
            }
        }
        return selfFrame;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onPause() {
        super.onPause();
        for (MixerTrackLayout mtl : mixerTracksArray) {
            mtl.getVolumeSlider().setOnSeekBarChangeListener(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        for (final MixerTrackLayout mtl : mixerTracksArray) {
            mtl.getVolumeSlider().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Sampler.getSampler().getActivePattern().getTrack(mixerTracksArray.indexOf(mtl) + 1).setTrackVolume((float) progress / 100);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }

    public void remakeTracks(MixerTrackLayout mtl) {

        mtl.getMuteBtn().setOnCheckedChangeListener(new MuteControllerListener(mtl, mixerTracksArray));

        mtl.getTrackName().setOnClickListener(new TrackNameControlListener(mtl, mixerTracksArray, getActivity()));
    }

}
