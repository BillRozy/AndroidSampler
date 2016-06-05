package com.example.fd.sampler;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class MixerFragment extends Fragment {
    private ArrayList<MixerTrackLayout> mixerTracksArray = null;
    private LinearLayout selfFrame;
    private LinearLayout containerForTracks;
    private TextView mPatternTitle;

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
        selfFrame = (LinearLayout) inflater.inflate(R.layout.fragment_mixer, container, false);
        containerForTracks = (LinearLayout) selfFrame.findViewById(R.id.containerForMixerTracks);
        for(Track track : Sampler.getSampler().getActivePattern().getTracksArray()){
            if(Sampler.getSampler().getActivePattern().getTracksArray().indexOf(track) != 0) {
                MixerTrackLayout mtl = new MixerTrackLayout(getActivity());
                mtl.getTrackName().setText(track.getTrackName());
                remakeTracks(mtl);
                mixerTracksArray.add(mtl);
                containerForTracks.addView(mtl);
            }
        }
        mPatternTitle = (TextView) selfFrame.findViewById(R.id.patternTitleView);
        mPatternTitle.setText( Sampler.getSampler().getActivePattern().getPatternName());
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

    public void remakeTracks(MixerTrackLayout mtl) {
            mtl.getVolumeSlider().setOnSeekBarChangeListener(new VolumeControllerListener(mtl,  mixerTracksArray));

            mtl.getMuteBtn().setOnCheckedChangeListener(new MuteControllerListener(mtl,  mixerTracksArray));

            mtl.getTrackName().setOnClickListener(new TrackNameControlListener(mtl,  mixerTracksArray, getActivity()));
    }


}
