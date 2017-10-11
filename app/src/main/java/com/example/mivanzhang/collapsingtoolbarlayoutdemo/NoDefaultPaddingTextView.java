package com.example.mivanzhang.collapsingtoolbarlayoutdemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Debug;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhangmeng on 2017/9/4.
 */

public class NoDefaultPaddingTextView extends View {
    private static final int DEFAULT_TEXT_SIZE = 28;//unit sp

    public NoDefaultPaddingTextView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public NoDefaultPaddingTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public NoDefaultPaddingTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NoDefaultPaddingTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private TextPaint paint;
    private String text = "";
    private Rect rc = new Rect();
    //unit px
    private float textSize;
    //text color
    private int textColor;

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        Debug.waitForDebugger();
        initAttribute(context, attrs);
        setWillNotDraw(false);
        paint = new TextPaint();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setAntiAlias(true);

    }

    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.NoDefaultPaddingTextView);
        textColor = typeArray.getColor(R.styleable.NoDefaultPaddingTextView_nopadding_text_color, getResources().getColor(R.color.black2));
        textSize = typeArray.getDimensionPixelSize(R.styleable.NoDefaultPaddingTextView_nopadding_text_size, sp2px(context, DEFAULT_TEXT_SIZE));
        text = typeArray.getString(R.styleable.NoDefaultPaddingTextView_nopadding_text);
        typeArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(text, 0, getHeight(), paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int measuredWidth = 0;
        int measuredHeight = 0;


        boolean needMeasure = false;
        if (!TextUtils.isEmpty(this.text)) {
            if (widthMode == MeasureSpec.EXACTLY) {
                measuredWidth = widthSize;
            } else {
                needMeasure = true;
            }
            if (heightMode == MeasureSpec.EXACTLY) {
                measuredHeight = heightSize;
            } else {
                needMeasure = true;
            }
        }

        if (needMeasure) {
            int width = (int) paint.measureText(text);
            paint.getTextBounds(text, 0, text.length(), rc);
            if (widthMode == MeasureSpec.AT_MOST) {
                measuredWidth = Math.min(widthSize, width);
            } else {
                measuredWidth = rc.width();
            }

            if (heightMode == MeasureSpec.AT_MOST) {
                measuredHeight = Math.min(heightSize, rc.height());
            } else {
                measuredHeight = rc.height();
            }
        }

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (null == text) {
            this.text = "";
        } else {
            this.text = text;
        }
        requestLayout();
//        invalidate();
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(@ColorInt int color) {
        this.textColor = color;
    }

    public int length() {
        if (TextUtils.isEmpty(text)) {
            return 0;
        }
        return text.length();
    }
}