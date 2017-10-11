package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class PullToRefreshScrollView extends PullToRefreshBase<ScrollView> {

	public PullToRefreshScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected ScrollView createRefreshableView(Context context, AttributeSet attrs) {
		ScrollView scrollView = new ScrollView(context, attrs);
		scrollView.setId(R.id.scrollview);
		return scrollView;
	}

	@Override
	protected boolean isReadyForPullDown() {
		return mRefreshableView.getScrollY() == 0;
	}

	@Override
	protected boolean isReadyForPullUp() {
		View scrollViewChild = mRefreshableView.getChildAt(0);
		if (null != scrollViewChild) {
			return mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
		}
		return false;
	}

	public void setPullImageVisibility(int visibility) {
		getHeaderLayout().setPullImageVisibility(visibility);
	}

	public void setPullImageDrawable(Drawable drawable) {
		getHeaderLayout().setPullImageDrawable(drawable);
		refreshLoadingViewsHeight();
	}
	public void setFrameImageBackground(Drawable frameDrawable) {
		getHeaderLayout().setFrameImageBackground(frameDrawable);
		refreshLoadingViewsHeight();
	}

	public void setLoadingDrawable(Drawable imageDrawable) {
		getHeaderLayout().setLoadingDrawable(imageDrawable);
		refreshLoadingViewsHeight();
	}

	public void setTextColor(int color) {
		getHeaderLayout().setTextColor(color);
	}

	public void setFrameImageVisibility(int visibility) {
		getHeaderLayout().setFrameImageVisibility(visibility);
	}

	public void setHeaderTextVisibility(int visibility) {
		getHeaderLayout().setHeaderTextVisibility(visibility);
	}

	public void setLoadingVisibility(int visibility) {
		getHeaderLayout().setLoadingVisibility(visibility);
	}

}
