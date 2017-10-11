package com.example.mivanzhang.collapsingtoolbarlayoutdemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by qinzhe on 2017/8/16.
 */

public class ExpandableBoundView extends LinearLayout {

    Drawable d;

    public ExpandableBoundView(Context context) {
        super(context);
        initDrawable(context);
    }

    public ExpandableBoundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initDrawable(context);
    }

    public ExpandableBoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDrawable(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExpandableBoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initDrawable(context);
    }

    private void initDrawable(Context context) {
        d = context.getResources().getDrawable(R.drawable.actionbar_index_fragment_search_bg);
    }

    public void setDrawable(Drawable d) {
        this.d = d;
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        d.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        d.draw(canvas);
    }

    public void setXBounds(int x) {
        d.setBounds(x, 0, getWidth(), getHeight());
        invalidate();
    }

}
