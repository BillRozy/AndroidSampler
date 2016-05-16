package com.example.fd.sampler;

/**
 * Created by BDV on 14.05.2016.
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

/**
 * Created by FD on 12.05.2016.
 */
public class HitView extends ImageButton implements View.OnClickListener {
    public HitView(Context context, boolean isActive){
        super(context);
        setOnClickListener(this);
        this.isActive = isActive;
        int row_height = getResources().getDimensionPixelOffset(R.dimen.app_list_row_size);
        this.setMaxHeight(row_height*2);
        this.setMaxWidth(row_height);
        //this.setMinimumHeight(70);
        //this.setMinimumWidth(40);
        this.setPadding(0,0,5,5);
        this.setImageResource(R.drawable.selector);
        this.setScaleType(ScaleType.FIT_CENTER);
        this.setBackgroundColor(Color.TRANSPARENT);
    }
    public HitView(Context context, AttributeSet attrs){
        super(context,attrs);
        setOnClickListener(this);
        TypedArray attributes = context.obtainStyledAttributes(attrs,R.styleable.HitView);
        isActive = attributes.getBoolean(R.styleable.HitView_isActive,false);
        int row_height = getResources().getDimensionPixelOffset(R.dimen.app_list_row_size);
        this.setMaxHeight(row_height*2);
        this.setMaxWidth(row_height);
        //this.setMinimumHeight(70);
        //this.setMinimumWidth(40);
        this.setPadding(0,0,5,5);
        this.setImageResource(R.drawable.selector);
        this.setScaleType(ScaleType.FIT_CENTER);
        this.setBackgroundColor(Color.TRANSPARENT);
        attributes.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        super.setBackgroundColor(Color.TRANSPARENT);

    }

    @Override
    public void onClick(View v) {
        isActive = !isActive;
        Log.d("CLICKED","BUTTON");
        if(isActive)
            this.setImageResource(R.drawable.btn_toggle_on_normal_holo_dark);
        else this.setImageResource(R.drawable.btn_toggle_off_normal_holo_dark);

    }

    private boolean isActive;

}
