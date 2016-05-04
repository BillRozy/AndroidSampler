package com.example.fd.sampler;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context mCont = this.getApplicationContext();
        Sampler myApp = Sampler.getSampler();
        Pattern firstPattern = new Pattern(mCont);
        Pattern secondPattern = new Pattern(mCont);
        myApp.addPattern(firstPattern);
        myApp.addPattern(secondPattern);
        myApp.setPatternActive(firstPattern);
        myApp.getActivePattern().addTrack("kick");
        myApp.getActivePattern().addTrack("snare");
        myApp.getActivePattern().addTrack("hh");
        myApp.getPattern(1).getTrack(1).makeHitActive(1,2,5,6,9,10,13);
        myApp.getPattern(1).getTrack(2).makeHitActive(3,7,11,15,16);
        myApp.getPattern(1).getTrack(3).makeHitActive(1,3,5,7,9,10,11,13,15);
        myApp.getActivePattern().getTrack(1).connectInstrument("H2Sv2 - THKL - Kick(0004).wav");
        myApp.getActivePattern().getTrack(2).connectInstrument("snare.wav");
        myApp.getActivePattern().getTrack(3).connectInstrument("H2Sv4 - THHL - HiHat(0006).wav");
        myApp.setPatternActive(secondPattern);
        myApp.getActivePattern().addTrack("kick");
        myApp.getActivePattern().addTrack("snare");
        myApp.getActivePattern().getTrack(1).connectInstrument("H2Sv2 - THKL - Kick(0004).wav");
        myApp.getActivePattern().getTrack(2).connectInstrument("H2Sv3 - THSL - Snare(0003).wav");
        myApp.getPattern(2).getTrack(1).makeHitActive(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16);
        myApp.getPattern(2).getTrack(2).makeHitActive(3,7,11,15);
        myApp.setPatternActive(firstPattern);

        Button play = (Button) this.findViewById(R.id.playButton);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sampler.getSampler().play();
            }
        });

        Button stop = (Button) this.findViewById(R.id.stopButton);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sampler.getSampler().stop();
            }
        });

        Button pause = (Button) this.findViewById(R.id.pauseButton);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sampler.getSampler().pause();
            }
        });

        RadioButton setFirstPatt = (RadioButton) this.findViewById(R.id.firstPattRadio);
        setFirstPatt.setOnClickListener(new setPatternActiveHandler(firstPattern));

        RadioButton setSecondPatt = (RadioButton) this.findViewById(R.id.secondPattRadioButton);
        setSecondPatt.setOnClickListener(new setPatternActiveHandler(secondPattern));

       EditText bpmEdit = (EditText) this.findViewById(R.id.bpmEditText);

        TextView bpmText = (TextView) this.findViewById(R.id.statusTextView);
        bpmText.setText(String.valueOf(Sampler.getSampler().getBPM()));

       Button setBPMButton = (Button) this.findViewById(R.id.setBPMBbutton);
        setBPMButton.setOnClickListener(new setBPMHandler(bpmEdit));

/*
        myApp.play();
        try{Thread.sleep(8000);
        }catch(InterruptedException exc){}
        myApp.pause();
        try{Thread.sleep(4000);
        }catch(InterruptedException exc){}
        myApp.play();
        try{Thread.sleep(4000);
        }catch(InterruptedException exc){}
        myApp.stop();
        myApp.setPatternActive(secondPattern);
        try{Thread.sleep(4000);
        }catch(InterruptedException exc){}
        myApp.setBPM(140);
        myApp.play();
        try{Thread.sleep(4000);
        }catch(InterruptedException exc){}
        myApp.stop();
        myApp.setPatternActive(firstPattern);
        myApp.play();
        try{Thread.sleep(4000);
        }catch(InterruptedException exc){}
        myApp.stop();*/
    }
}

class setPatternActiveHandler implements View.OnClickListener{
    private Pattern pattern;
    public setPatternActiveHandler(Pattern pattern){this.pattern = pattern;}
    @Override
    public void onClick(View v) {
        Sampler.getSampler().setPatternActive(pattern);
    }
}

class setBPMHandler implements View.OnClickListener{
    private EditText bpmView;
    public void setBPM(EditText bpmText){
        Sampler.getSampler().setBPM(Integer.parseInt(bpmText.getText().toString()));
    }
    public setBPMHandler(EditText text){bpmView = text;}
    public void onClick(View v){

            setBPM(bpmView);
    }
}