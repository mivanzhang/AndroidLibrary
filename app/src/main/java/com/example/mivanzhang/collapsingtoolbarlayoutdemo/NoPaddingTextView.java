package com.example.mivanzhang.collapsingtoolbarlayoutdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by zhangmeng on 2017/8/31.
 */

public class NoPaddingTextView extends TextView {
    private int mAdditionalPadding;

    public NoPaddingTextView(Context context) {
        super(context);
        init();
    }

    public NoPaddingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setIncludeFontPadding(false);
    }

    Paint.FontMetricsInt fontMetricsInt;

    @Override
    protected void onDraw(Canvas canvas) {
//        Debug.waitForDebugger();
//        int yOff = -mAdditionalPadding/3;
//        canvas.translate(0, yOff);
        //        super.onDraw(canvas);


        if (true){
            if (fontMetricsInt == null){
                fontMetricsInt = new Paint.FontMetricsInt();
                getPaint().getFontMetricsInt(fontMetricsInt);
            }
            canvas.translate(0, fontMetricsInt.top - fontMetricsInt.ascent);
        }
        super.onDraw(canvas);

//        // Figure out the drawing coordinates
//        mStrokePaint.setAntiAlias(true);
//        mStrokePaint.setStyle(Paint.Style.STROKE);
//        mStrokePaint.setTextAlign(Paint.Align.CENTER);
//
//        // Get the text to print
//        final float textSize = super.getTextSize();
//        final String text = super.getText().toString();
//
//        // setup stroke
//        mStrokePaint.setColor(mOutlineColor);
//        mStrokePaint.setStrokeWidth(textSize * OUTLINE_PROPORTION);
//        mStrokePaint.setTextSize(textSize);
//        mStrokePaint.setFlags(super.getPaintFlags());
//        mStrokePaint.setTypeface(super.getTypeface());
//
//        // Figure out the drawing coordinates
//        super.getPaint().getTextBounds(text, 0, text.length(), mTextBounds);
//        // draw everything
//        canvas.drawText(text,
//                super.getWidth() * 0.5f, (super.getHeight() + mTextBounds.height()) * 0.5f,
//                mStrokePaint);
//        super.onDraw(canvas);


    }
    private static final float OUTLINE_PROPORTION = 0.1f;
    private final Paint mStrokePaint = new Paint();
    private final Rect mTextBounds = new Rect();
    private int mOutlineColor = Color.TRANSPARENT;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        getAdditionalPadding();

        int mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode != MeasureSpec.EXACTLY) {
            int measureHeight = measureHeight(getText().toString(), widthMeasureSpec);

            int height = measureHeight - mAdditionalPadding;
            height += getPaddingTop() + getPaddingBottom();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureHeight(String text, int widthMeasureSpec) {
        float textSize = getTextSize();

        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setText(text);
        textView.measure(widthMeasureSpec, 0);
        return textView.getMeasuredHeight();
    }

    private int getAdditionalPadding() {
        float textSize = getTextSize();

        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setLines(1);
        textView.measure(0, 0);
        int measuredHeight = textView.getMeasuredHeight();
        if (measuredHeight - textSize > 0) {
            mAdditionalPadding = (int) (measuredHeight - textSize);
            Log.v("NoPaddingTextView", "onMeasure: height=" + measuredHeight + " textSize=" + textSize + " mAdditionalPadding=" + mAdditionalPadding);
        }
        return mAdditionalPadding;
    }
}
