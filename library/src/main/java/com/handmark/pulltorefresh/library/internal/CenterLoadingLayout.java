package com.handmark.pulltorefresh.library.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.R;

/**
 * Created by liangcan on 15/6/5.
 */
public class CenterLoadingLayout extends LoadingView {
    private static int PULL_IMAGE_STATE_PULLING = 1;
    private static int PULL_IMAGE_STATE_PULL_HAS_END = 2;

    private int mPullImageState;
    private ImageView mHeaderImage;
    private ImageView mRefreshingImage;
    private Drawable mPullEndDrawable;
    private int mPullImageHeight;
    private int mPullEndImageHeight;
    private boolean mShowPullImage = true;
    private boolean mShowRefreshingImage = true;
    private boolean mShowFixedPullImage = false;

    @SuppressLint("NewApi")
    public CenterLoadingLayout(Context context, final PullToRefreshBase.Mode mode, TypedArray attrs) {
        super(context);
        ViewGroup header = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_center_header, this);
        mHeaderImage = (ImageView) header.findViewById(R.id.pull_to_refresh_image);
        mHeaderImage.setImageResource(R.drawable.pull_image);
        ViewGroup.LayoutParams params = mHeaderImage.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int widthSpec = MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.EXACTLY);
        int lpHeight = params.height;
        int heightSpec;
        heightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.UNSPECIFIED);
        mHeaderImage.measure(widthSpec, heightSpec);
        mPullImageHeight = mHeaderImage.getMeasuredHeight();

        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderBackground)) {
            Drawable background = attrs.getDrawable(R.styleable.PullToRefresh_ptrHeaderBackground);
            if (null != background) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    setBackgroundDrawable(background); // Deprecated from API level 16
                } else {
                    setBackground(background); // Added in API level 16
                }
            }
        }

        mPullEndDrawable = getResources().getDrawable(R.drawable.pull_end_animation);
        mPullEndImageHeight = mPullEndDrawable.getIntrinsicHeight();

        mRefreshingImage = (ImageView) header.findViewById(R.id.refreshing_image);
        mRefreshingImage.setImageResource(R.drawable.refreshing_center_animation);

        reset();
    }

    @Override
    public void reset() {
        mPullImageState = PULL_IMAGE_STATE_PULLING;
        operateImageAnimation(mHeaderImage, false);
        if (mShowPullImage) {
            resetPullImage();
            mHeaderImage.setVisibility(View.VISIBLE);
        } else {
            mHeaderImage.setVisibility(View.INVISIBLE);
        }

        operateImageAnimation(mRefreshingImage, false);
        mRefreshingImage.setVisibility(View.INVISIBLE);
    }

    @Override
    public void releaseToRefresh() {

    }

    @Override
    public void refreshing() {
        if (mShowPullImage) {
            mHeaderImage.setVisibility(View.INVISIBLE);
            operateImageAnimation(mHeaderImage, false);
        }

        if (mShowRefreshingImage) {
            mRefreshingImage.setVisibility(View.VISIBLE);
            operateImageAnimation(mRefreshingImage, true);
        }
    }

    @Override
    public void pullToRefresh() {

    }

    @Override
    public void onPullY(float scaleOfHeight) {
        if (mShowPullImage && !mShowFixedPullImage) {
            float height = scaleOfHeight * mPullImageHeight;
            if (PULL_IMAGE_STATE_PULLING == mPullImageState) {
                if (height < mPullImageHeight) {
                    setPullImageHeight((int) height);
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT, mPullEndImageHeight);
                    mHeaderImage.setLayoutParams(params);
                    mHeaderImage.setImageDrawable(mPullEndDrawable);
                    operateImageAnimation(mHeaderImage, true);
                    mPullImageState = PULL_IMAGE_STATE_PULL_HAS_END;
                }
            } else if (PULL_IMAGE_STATE_PULL_HAS_END == mPullImageState) {
                if (height <= mPullImageHeight) {
                    resetPullImage();
                    mPullImageState = PULL_IMAGE_STATE_PULLING;
                }
            }
        }
    }

    @Override
    public void setFrameImageBackground(Drawable drawable) {
        mRefreshingImage.setImageDrawable(drawable);
    }

    @Override
    public void setFrameImageVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            mShowRefreshingImage = true;
        } else {
            mShowRefreshingImage = false;
        }
        mRefreshingImage.setVisibility(visibility);
    }

    @Override
    public  void setPullImageDrawable(Drawable drawable) {
        if (drawable != null) {
            mShowFixedPullImage = true;
            mHeaderImage.setImageDrawable(drawable);
        }
    }

    @Override
    public void setPullImageDrawable(Drawable drawable, boolean showFixedPullImage) {
        if(null != drawable) {
            mHeaderImage.setImageDrawable(drawable);
        }
        mShowFixedPullImage = showFixedPullImage;
    }

    @Override
    public void setPullImageVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            mShowPullImage = true;
        } else {
            mShowPullImage = false;
        }
        mHeaderImage.setVisibility(visibility);
    }

    private void setPullImageHeight(int height) {
        ViewGroup.LayoutParams params = mHeaderImage.getLayoutParams();
        params.height = height;
        mHeaderImage.setLayoutParams(params);
    }

    private void resetPullImage() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.refreshing_image);
        mHeaderImage.setLayoutParams(params);
        if (mShowFixedPullImage) {
            Drawable drawable = mHeaderImage.getDrawable();
            if (drawable instanceof AnimationDrawable) {
                AnimationDrawable animationDrawable = (AnimationDrawable) drawable;
                animationDrawable.stop();
                animationDrawable.selectDrawable(0);
            }
        } else {
            mHeaderImage.setImageResource(R.drawable.pull_image);
        }
    }


    private void operateImageAnimation(ImageView imageView, boolean start) {
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof AnimationDrawable) {
            AnimationDrawable animationDrawable = (AnimationDrawable) drawable;
            animationDrawable.stop();
            if (start) {
                animationDrawable.start();
            }
        }
    }
}
