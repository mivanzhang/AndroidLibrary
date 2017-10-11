package com.example.mivanzhang.collapsingtoolbarlayoutdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<String> datas;
    private CoordinatorLayout coordinatorLayout;

    private int barHeight;


    private static final String TAG = "AIDLActivity";

    //由AIDL文件生成的Java类
    private IMyAidlInterface mIShop = null;

    //标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
    private boolean mIsConnected = false;

    //包含Book对象的list
    private Book mBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Debug.waitForDebugger();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, Main4Activity.class));
                addBook(v);
            }
        });
//        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.root);
//        frameLayout.addView(getVoucherCenterView(), getVoucherCenterViewLayoutParam());
//        showToastWithImg(this, this.getString(R.string.app_name), R.drawable.ic_arrow_white_down, true);

//        setContentView(R.layout.activity_main2);
////        CheckBox checkBox=(CheckBox) findViewById(R.id.checkbox);
////        checkBox.setText("我是你好，\n第二行");
//
////        NoDefaultPaddingTextView noDefaultPaddingTextView = (NoDefaultPaddingTextView) findViewById(R.id.textview);
////        noDefaultPaddingTextView.setText(null);
//
//
//        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
//        initData();
//        initView();
//
//        final HeaderView appBarLayout = (HeaderView) findViewById(R.id.header);
//        MyFriendsListPullToRefreshLinearLayout refreshLinearLayout = (MyFriendsListPullToRefreshLinearLayout) findViewById(R.id.pull);
//        refreshLinearLayout.setPullDownListener(new MyFriendsListPullToRefreshLinearLayout.OnReadyForPullDownListener() {
//            @Override
//            public boolean isReadyForPullDown() {
////                appBarLayout.showWeather();
////                return appBarLayout.getTop() == 0 && ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition() == 0 && appBarLayout.isExpnaded();
//                return appBarLayout.getTop() == 0 && ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition() == 0 && appBarLayout.isFullExpanded();
//            }
//        });
//
////        animate();
////        HeaderView headerView = (HeaderView) findViewById(R.id.header);
//        appBarLayout.setWeatherAnimationCallBack(new HeaderView.WeatherAnimationCallBack() {
//            @Override
//            public void animationStart() {
//                Log.w("headerView", "animationStart");
//            }
//
//            @Override
//            public void animationEnd() {
//                Log.w("headerView", "animationEnd");
//            }
//
//            @Override
//            public void weatherAnimationFullyExpand() {
//                Log.w("headerView", "weatherAnimationFullyExpand");
//
//            }
//
//            @Override
//            public void animating(float rate) {
//                Log.w("headerView", "animating rate is " + rate);
//
//            }
//        });
    }

    public void addBook(View view) {
        //如果与服务端的连接处于未连接状态，则尝试连接
        if (!mIsConnected) {
            attemptToBindService();
            Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mIShop == null) return;

        Book book = new Book();
        book.setName("APP研发录In");
        book.setPrice(30);
        try {
            Book bookReturn = mIShop.addBook(book);
            Log.d(TAG, "method addBook ");
            Log.d(TAG, "client parameter addBook: " + book.toString() + " return book " + bookReturn.toString()+"\n\n");

            bookReturn = mIShop.addBookOut(book);
            Log.d(TAG, "method addBookOut ");
            Log.d(TAG, "client parameter addBookOut: " + book.toString() + " return book " + bookReturn.toString()+"\n\n");

            bookReturn = mIShop.addBookInOut(book);
            Log.d(TAG, "method addBookInOut ");
            Log.d(TAG, "client parameter addBookInOut: " + book.toString() + " return book " + bookReturn.toString()+"\n\n");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 尝试与服务端建立连接
     */
    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setAction("action.hfs");
        intent.setPackage("com.example.tianbei.collapsingtoolbarlayoutdemo");
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mIsConnected) {
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mIsConnected) {
            unbindService(mServiceConnection);
            mIsConnected = false;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(getLocalClassName(), "service connected");
            mIShop = IMyAidlInterface.Stub.asInterface(service);
            mIsConnected = true;

            if (mIShop != null) {
                try {
                    mBooks = mIShop.getBook();
                    Log.d(TAG, "client onServiceConnected: " + mBooks.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(getLocalClassName(), "service disconnected");
            mIsConnected = false;
        }
    };

    public static void showToastWithImg(Context context, String message, int imgRes, boolean shortShow) {
        Toast toast = Toast.makeText(context, message, shortShow ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) toast.getView();
        ImageView imageCodeProject = new ImageView(context);
        imageCodeProject.setImageResource(imgRes);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((40), (40));
        layoutParams.setMargins(0, 32, 0, 0);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        toastView.addView(imageCodeProject, 0, layoutParams);
        toast.show();
    }


    private View getFramViewView() {
        FrameLayout frameLayout = new FrameLayout(this);
        return frameLayout;
    }

    private LinearLayout.LayoutParams getFramViewViewLayout() {
        LinearLayout.LayoutParams contentViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        contentViewLayoutParams.setLayoutDirection(LinearLayout.VERTICAL);
        contentViewLayoutParams.weight = 1;
        return contentViewLayoutParams;
    }

    private View getVoucherCenterView() {
        TextView voucherCenter = new TextView(this);
        voucherCenter.setText("text");
        voucherCenter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_black, 0, 0, 0);
        voucherCenter.setGravity(Gravity.CENTER);
        return voucherCenter;
    }

    private FrameLayout.LayoutParams getVoucherCenterViewLayoutParam() {
        FrameLayout.LayoutParams voucherCenterLayoutParam = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                60);
        voucherCenterLayoutParam.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        voucherCenterLayoutParam.setMargins(0, 0, 0, 30);
        return voucherCenterLayoutParam;
    }

    private void initData() {
        datas = new ArrayList<>();
        for (int i = 0; i < 70; i++) {
            datas.add("hunao " + i);
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private MyAdapter adapter;

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MyAdapter adapter = new MyAdapter(datas);
        recyclerView.setAdapter(adapter);

    }


}
