package com.example.fd.sampler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by FD on 11.05.2016.
 */
public class TrackView extends View {
    public TrackView(Context context, AttributeSet attrs){
        super(context,attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TrackView,
                0,0);
        try{
            mCountSteps = a.getInt(R.styleable.TrackView_countsSteps,8);
            mTextPos = a.getBoolean(R.styleable.TrackView_labelPosition, false);
        }finally {
            a.recycle();
        }
        this.init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawRect(rectF,mPaintBorder);
        float lsum = 0.0f;
      //  for(int i = 0; i < mCountSteps;i++){
            canvas.drawRect(100.0f+lsum,10f,50.0f,100.0f,mPaintHit);
            lsum += 50.0f;
       // }
    }

    private void init(){
        mPaintBorder = new Paint();
        mPaintBorder.setStrokeWidth(3);
        mPaintBorder.setStyle(Paint.Style.STROKE);
        mPaintHit = new Paint();
        mPaintHit.setStrokeWidth(4);
        mPaintHit.setColor(Color.MAGENTA);
        rectF = new RectF(50,50,800,300);
        setCountSteps(8);
    }

    public void setCountSteps(int num){
        mCountSteps = num;
        invalidate();
        requestLayout();
    }

    public int getCountSteps(){
        return mCountSteps;
    }



    private int mCountSteps;
    private boolean mTextPos;
    private RectF rectF;
    private Paint mPaintBorder;
    private Paint mPaintHit;
}
