package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by liangcan on 15/6/5.
 */
public abstract class LoadingView extends FrameLayout {

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setPullLabel(String pullLabel) {}

    public void setRefreshingLabel(String refreshingLabel) {}

    public void setReleaseLabel(String releaseLabel) {}

    public void setTextColor(ColorStateList color) {}

    public void setSubTextColor(ColorStateList color) {}

    public void setTextColor(int color) {}

    public void setSubTextColor(int color) {}

    public void setSubHeaderText(CharSequence label) {}

    public void setHeaderTextVisibility(int visibility) {}

    public void setPullImageDrawable(Drawable imageDrawable){}

    public void setPullImageDrawable(Drawable imageDrawable, boolean isFixedPullImage) {}

    public void setPullImageVisibility(int visibility) {}

    public void setLoadingDrawable(Drawable imageDrawable) {}

    public void setLoadingVisibility(int visibility) {}

    public abstract void reset();

    public abstract void releaseToRefresh();

    public abstract void refreshing();

    public abstract void pullToRefresh();

    public abstract void onPullY(float scaleOfHeight);

    public abstract void setFrameImageBackground(Drawable drawable);

    public abstract void setFrameImageVisibility(int visibility);
}
