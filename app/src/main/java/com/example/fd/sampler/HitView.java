package com.example.fd.sampler;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class HitView extends ImageButton {
    private boolean isActive;

    public HitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.HitView);
        isActive = attributes.getBoolean(R.styleable.HitView_isActive, false);
        int row_height = getResources().getDimensionPixelOffset(R.dimen.app_list_row_size);
        this.setMaxHeight(row_height * 2);
        this.setMaxWidth(row_height);
        this.setPadding(0, 0, 5, 5);
        this.setImageResource(R.drawable.selector);
        this.setScaleType(ScaleType.CENTER_CROP);
        this.setBackgroundColor(Color.TRANSPARENT);
        attributes.recycle();
    }

    public void toggleState() {
        isActive = !isActive;
    }

    public void setActive() {
        isActive = true;
    }

    public boolean getState() {
        return isActive;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        super.setBackgroundColor(Color.TRANSPARENT);

    }

}
