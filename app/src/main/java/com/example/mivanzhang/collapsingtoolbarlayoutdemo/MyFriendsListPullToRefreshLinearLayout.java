package com.example.mivanzhang.collapsingtoolbarlayoutdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by tianbei on 2017/7/25.
 */

public class MyFriendsListPullToRefreshLinearLayout extends PullToRefreshBase<LinearLayout> {

    private OnReadyForPullDownListener listener;


    public void setPullDownListener(OnReadyForPullDownListener listener) {
        this.listener = listener;
    }

    public MyFriendsListPullToRefreshLinearLayout(Context context) {
        super(context);
    }

    public MyFriendsListPullToRefreshLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected LinearLayout createRefreshableView(Context context, AttributeSet attrs) {
        LinearLayout linearLayout = new LinearLayout(context, attrs);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
    }

    @Override
    protected boolean isReadyForPullDown() {
        if (listener != null) {
            return listener.isReadyForPullDown();
        }
        return false;
//        return true;
    }

    @Override
    protected boolean isReadyForPullUp() {
        return false;
    }


    public interface OnReadyForPullDownListener {
        boolean isReadyForPullDown();
    }

}
