package com.example.mivanzhang.collapsingtoolbarlayoutdemo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by zhangmeng on 2017/8/15.
 * <p>
 * 整个view动画的计算都是以一个view中心坐标计算的
 * <p>
 * 除了天气的气温摄氏度动画的计算，是以左边距+view宽度的一半
 */

public class HeaderView extends AppBarLayout {
    private Context context;
    private TextView updatedView;
    //这边不能直接给Textview设置drawable，因为这样做就无法居中，达不到设计稿的要求
    private ImageView updatedViewLeftDrawble;
    private View updatedViewContainer;
    private View rootView;
    private TextView cityButton;
    private View cityButtonRightDrawble;
    private View weatherTemperatureContainer;
    private TextView weatherTemperature;
    private View weatherTemperatureDuIcon;
    private ImageView weatherIcon;
    private View searchIcon;
    private View searchTextView;
    private ExpandableBoundView searchLayout;
    private ViewGroup contentView;
    private View containerView;
    private TextView weatherDescribe;
    private TextView ariQuality;
    private View weatherDescribeContainer;
    private View weatherInfoContainer;
    private View weatherStub;
    private TextView weatherAqiNumber;
    private TextView weatherUnknown;
    private View bottomView;
    private int previousVerticalOffset = Integer.MIN_VALUE;
    private int currentVerticalOffset = Integer.MIN_VALUE;
    private int totalOffset;
    private boolean isWeatherShow = true;
    private boolean isWeatherUnknown = false;

    private WeatherAnimationCallBack weatherAnimationCallBack;

    //动画常量,从设计稿上测量
    public static final float WEATHER_ICON_SCALE_RATE = 0.2f; //
    public static final float WEATHER_TEXTVIEW_SCALE_RATE = 0.61f; //
    public static final float WEATHER_DU_ICON_SCALE_RATE = 0.4f; //   2/5
    public static final int DEFAULT_WEATHER_ANIMATION_TIME = 1000; //   3秒
    public static final int DOUBLE_DEFAULT_WEATHER_ANIMATION_TIME = 3000; //   3秒
    public static final int HEADER_MAX_HEIGHT = 89; //   最大高度
    public static final int HEADER_MIN_HEIGHT = 48; //   最小高度

    public HeaderView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public void initView() {
        rootView = LayoutInflater.from(context).inflate(R.layout.actionbar_header, this, true);
        updatedView = (TextView) rootView.findViewById(R.id.weather_update);
        updatedViewContainer = rootView.findViewById(R.id.weather_update_container);
        updatedViewLeftDrawble = (ImageView) rootView.findViewById(R.id.weather_update_left_drawable);
        contentView = (ViewGroup) findViewById(R.id.content);
//        containerView = findViewById(R.id.container);
        weatherTemperatureContainer = rootView.findViewById(R.id.weather_temperature_container);
        weatherTemperature = (TextView) weatherTemperatureContainer.findViewById(R.id.weather_temperature);
        weatherTemperatureDuIcon = weatherTemperatureContainer.findViewById(R.id.weather_temperature_du_icon);
        weatherIcon = (ImageView) rootView.findViewById(R.id.weather_icon);
        cityButton = (TextView) rootView.findViewById(R.id.city_button);
        cityButtonRightDrawble = rootView.findViewById(R.id.city_button_right_drawable);
        searchLayout = (ExpandableBoundView) rootView.findViewById(R.id.search_layout);
        searchIcon = searchLayout.findViewById(R.id.search_icon);
        searchTextView = searchLayout.findViewById(R.id.search_edit);
        weatherDescribe = (TextView) rootView.findViewById(R.id.weather_describe);
        ariQuality = (TextView) rootView.findViewById(R.id.air_quality);
        weatherDescribeContainer = rootView.findViewById(R.id.weather_describe_container);
        weatherInfoContainer = rootView.findViewById(R.id.weather_info_container);
        weatherUnknown = (TextView) rootView.findViewById(R.id.weather_unknown);
        weatherAqiNumber = (TextView) rootView.findViewById(R.id.weather_aqi_number);
        weatherStub = rootView.findViewById(R.id.weather_stub);
        bottomView = rootView.findViewById(R.id.weather_bottom_line);
        startAnimation();
    }


    private void startAnimation() {
        totalOffset = getTotalScrollRange();
        addOnOffsetChangedListener(new OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset != 0) {
                    bottomView.setVisibility(VISIBLE);
                } else {
                    bottomView.setVisibility(GONE);
                }
                if (!isWeatherShow) {
                    return;
                }
                currentVerticalOffset = verticalOffset;
                if (currentVerticalOffset == previousVerticalOffset) {
                    return;
                }


                totalOffset = contentView.getHeight() - ViewCompat.getMinimumHeight(contentView);
                if (totalOffset == 0) {
                    return;
                }
                float translate = -verticalOffset;
//                translate *= 1.5;
                if (translate > totalOffset) {
                    translate = totalOffset;
                }
                float rate = translate / totalOffset;
                notifyCallback(1 - rate);
                previousVerticalOffset = currentVerticalOffset;
                updateViewLocation(1 - rate);

            }
        });
    }

    private boolean isAnimationCycleStart = true;

    private void notifyCallback(float rate) {
        if (weatherAnimationCallBack == null || previousVerticalOffset == Integer.MIN_VALUE) {
            return;
        }
        if (rate < 0.5f && isAnimationCycleStart) {
            weatherAnimationCallBack.animationStart();
            isAnimationCycleStart = false;
        }
        weatherAnimationCallBack.animating(rate);
        if (rate == 1) {
            weatherAnimationCallBack.weatherAnimationFullyExpand();
            //下一个动画循环的开始

        }
        if (rate == 0 && !isAnimationCycleStart) {
            isAnimationCycleStart = true;
            weatherAnimationCallBack.animationEnd();
        }
    }

    private void updateViewLocation(float rate) {
        if (Math.abs(rate - 1) < 0.1) {
            weatherStub.setVisibility(GONE);
        } else {
            weatherStub.setVisibility(VISIBLE);
        }
        startWeatherAnimation(rate);
        startActionBarAnimation(rate);
        startWeatherDescribeAnimation(rate);
        startWeatherUpdateAnimation(rate);
    }


    private void startWeatherUpdateAnimation(float rate) {
        if (!isWeatherUnknown) {
            updatedViewContainer.setVisibility(VISIBLE);
            updatedViewContainer.setAlpha(rate);
        }
        //整个下拉框的高度89-最终显示的高度36
        updateViewLoacation(updatedViewContainer, (int) dp2Px(-53), 0, rate);
    }

    private void startWeatherDescribeAnimation(float rate) {
        weatherInfoContainer.setAlpha(rate);
        weatherInfoContainer.setTranslationY(-currentVerticalOffset);
        weatherInfoContainer.setTranslationX(((int) dp2Px(25) * rate));
    }

    private void startActionBarAnimation(float rate) {
        rate = 1 - rate;
        //其实移动是34
        cityButtonRightDrawble.bringToFront();
        cityButton.setTranslationX(dp2Px(34) * rate);
        cityButtonRightDrawble.setTranslationX(dp2Px(34) * rate);
        searchLayout.setXBounds((int) (rate * ((int) dp2Px(34))));
        updateViewLoacation(searchIcon, 0, ((int) dp2Px(34)), rate);
        updateViewLoacation(searchTextView, 0, ((int) dp2Px(34)), rate);

    }

    private void startWeatherAnimation(float rate) {
        rate = 1 - rate;
        if (isWeatherUnknown) {
            //天气未知的情况下，原始天气的位置是垂直居中的，y移动整个滑动的距离41dp+ 两个画布差距的一半（48-41）/2,而且没有缩放，，同时右移2dp
            updateViewLoacation(weatherIcon, (int) dp2Px(43), 0, rate);

        } else {
            //原始坐标是(27,42)-->(26,34)  其中画布下移41,为了好看稍微加了点下移2dp
            updateViewLoacation(weatherIcon, (int) dp2Px(35), (int) dp2Px(-1), rate);

            //原始坐标是(65,42)--->(26,51.5)+ 画布41,设计嫌弃天气和摄氏度距离太近，因此这边偷偷加上2dp
            //x轴的计算为：47+wide/2-14-24/2(天气图标的中心为终点),

            updateViewLoacation(weatherTemperature, (int) dp2Px(50.5f + 2), (int) (dp2Px(-21) - weatherTemperature.getWidth() * rate / 2), rate);
//            updateViewLoacation(weatherTemperature, (int) dp2Px(50.5f), (int) (dp2Px(-18) - weatherTemperature.getWidth() * rate / 2), rate);
            weatherTemperature.setScaleX(1 - WEATHER_TEXTVIEW_SCALE_RATE * rate);
            weatherTemperature.setScaleY(1 - WEATHER_TEXTVIEW_SCALE_RATE * rate);

            //°的动画设置   原始位置（85.5,33.5）->(34.5,47.5)  +画布下移41
            weatherTemperatureDuIcon.setScaleX(1 - WEATHER_DU_ICON_SCALE_RATE * rate);
            weatherTemperatureDuIcon.setScaleY(1 - WEATHER_DU_ICON_SCALE_RATE * rate);
            //0.69=1-WEATHER_TEXTVIEW_SCALE_RATE/2
            int offset = 2 * (2 - weatherTemperature.length());//这边是特殊处理，主要的原因是weatherTemperature的单位是sp，这个在不同的手机转换为dp存在着差异，导致我采用了这个蹩脚的办法
            updateViewLoacation(weatherTemperatureDuIcon, (int) dp2Px(55 + 4), (int) (dp2Px(-25 + offset) - weatherTemperature.getWidth() * 0.69 * rate), rate);
//            updateViewLoacation(weatherTemperatureDuIcon, (int) dp2Px(55), (int) (dp2Px(-22) - weatherTemperature.getWidth() * 0.69 * rate), rate);

        }
        weatherIcon.setScaleX(1 - WEATHER_ICON_SCALE_RATE * rate);
        weatherIcon.setScaleY(1 - WEATHER_ICON_SCALE_RATE * rate);

    }

    /**
     * @param view        view 保存view的原始坐标信息
     * @param downOffset  下移的位移，负数表示上移 单位是px
     * @param rightOffset 右移的位移，负数表示左移 单位是px
     * @param rate        动画的进行的比率
     */

    private void updateViewLoacation(View view, int downOffset, int rightOffset, float rate) {
        updateViewLoacation(view, downOffset, rightOffset, rate, 0);
    }

    /**
     * @param view        view 保存view的原始坐标信息
     * @param downOffset  下移的位移，负数表示上移 单位是px
     * @param rightOffset 右移的位移，负数表示左移 单位是px
     * @param rate        动画的进行的比率
     * @param yOffset     额外下移的位移，不受制于rate的影响
     */

    private void updateViewLoacation(View view, int downOffset, int rightOffset, float rate, int yOffset) {
        view.setTranslationX(rightOffset * rate);
        view.setTranslationY(downOffset * rate + yOffset);
    }

    public void setCityText(@Nullable String cityName) {
        if (TextUtils.isEmpty(cityName)) {
            return;
        }
        cityButton.setText(cityName);
    }

    public void setWeatherIcon(int resourceid) {
        weatherIcon.setImageResource(resourceid);
    }

    public void setWeatherIcon(Drawable drawable) {
        weatherIcon.setImageDrawable(drawable);
    }

    public void setWeatherTemperature(int temperature) {
        //一位数是1s动画，1X是两秒，2X是三秒。一直延续下去，比如说15摄氏度就是2秒动画完毕
        setWeatherTemperature(temperature, DEFAULT_WEATHER_ANIMATION_TIME * (Math.abs(temperature / 10) + 1));
    }

    public void setWeatherTemperature(int temperature, int animationTime) {
        weatherTemperature.setText(String.valueOf(temperature));
//        if (isWeatherUnknown) {
//            reset();
//        }
//        final boolean isTwoDigit;
//        if (temperature / 10 != 0) {
//            isTwoDigit = true;
//        } else {
//            isTwoDigit = false;
//        }
//        //从零开始
//        ValueAnimator valueAnimator = ValueAnimator.ofInt(-1, temperature);
//        valueAnimator.setInterpolator(new DecelerateInterpolator());
//        valueAnimator.setDuration(animationTime);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                StringBuilder stringBuilder = new StringBuilder();
//                if (temperature < 0) {
//                    stringBuilder.append("-");
//                }
//
//                int currentValue = Math.abs((int) valueAnimator.getAnimatedValue());
//                if (isTwoDigit && (currentValue < 10)) {
//                    stringBuilder.append("0" + currentValue);
//                } else {
//                    stringBuilder.append(currentValue);
//                }
//
//                weatherTemperature.setText(stringBuilder.toString());
//            }
//        });
//        valueAnimator.start();
    }

    public void setAirQualityText(String airQualityText) {
        if (isWeatherUnknown) {
            reset();
        }
        ariQuality.setText(airQualityText);
    }

    public void setWeatherDescribe(String airQualityText) {
        if (isWeatherUnknown) {
            reset();
        }
        weatherDescribe.setText(airQualityText);
    }

    public void setUpdatedViewText(String text) {
        setUpdatedViewText(text, null);
    }

    public void setUpdatedViewText(String text, Drawable leftDrawable) {
        if (updatedViewContainer.getVisibility() != VISIBLE) {
            updatedViewContainer.setVisibility(VISIBLE);
        }
        if (leftDrawable != null) {
            updatedViewLeftDrawble.setImageDrawable(leftDrawable);
        }
        updatedView.setText(text);
    }

    /**
     * 如果天气未知，记得调用setWeatherIcon 设置天气的图标
     * 这个函数有副作用，如果后续天气已知，在设置天气相关信息之前 ，请调用 方法 reset()
     */
    public void setWeatherUnkown() {
        isWeatherUnknown = true;
        weatherDescribe.setVisibility(GONE);
        ariQuality.setVisibility(GONE);
        weatherUnknown.setVisibility(VISIBLE);
        weatherTemperatureContainer.setVisibility(GONE);
        weatherAqiNumber.setVisibility(GONE);
        updatedViewContainer.setVisibility(GONE);
    }

    public void reset() {
        isWeatherUnknown = false;
        weatherDescribe.setVisibility(VISIBLE);
        ariQuality.setVisibility(VISIBLE);
        weatherUnknown.setVisibility(GONE);
        weatherTemperatureContainer.setVisibility(VISIBLE);
        updatedViewContainer.setVisibility(GONE);
        weatherAqiNumber.setVisibility(VISIBLE);
    }

    /**
     * 天气view是否全部展开
     */
    public boolean isFullExpanded() {
        if (!isWeatherShow) {
            return true;
        }
        return currentVerticalOffset == 0;
    }

    public void setAQINumber(String number) {
        weatherAqiNumber.setText(number);

    }

    /**
     * @param isShow 是否显示天气相关信息,该函数有副作用，设置为false之后，
     *               <p>
     *               天气相关信息不再展示，想要展示，请再次调用，并传递参数为true
     */
    public void setIfNeedShowWeatherInfo(boolean isShow) {
        if (isWeatherUnknown) {
            reset();
        }
        if (isShow) {
            weatherDescribeContainer.setVisibility(VISIBLE);
        } else {
            updateViewLocation(1);
            weatherDescribeContainer.setVisibility(GONE);
        }

        int height = isShow ? HEADER_MAX_HEIGHT : HEADER_MIN_HEIGHT;
        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) contentView.getLayoutParams();
        layoutParams.height = (int) dp2Px(height);
        contentView.setLayoutParams(layoutParams);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        containerView.setBackgroundColor(color);
    }

    public void setContentViewBackgroundDrawable(Drawable drawable) {
        if (contentView != null) {
            contentView.setBackground(drawable);
        }
    }

    private float dp2Px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    public interface WeatherAnimationCallBack {
        //动画刚刚开始,此时的rate=0
        void animationStart();

        //动画结束,此时的rate=0
        void animationEnd();

        //rate =1 代码动画全部展开，
        void weatherAnimationFullyExpand();

        //rate =1 代码动画全部展开，rate==0，代表动画结束
        //在一次完整的动画展示过程，rate的值的变化范围是0~1，然后动画收起之后，从1~0
        void animating(float rate);
    }

    public void setWeatherAnimationCallBack(WeatherAnimationCallBack weatherAnimationCallBack) {
        this.weatherAnimationCallBack = weatherAnimationCallBack;
    }

    @Override
    public void setElevation(float f) {
        super.setElevation(0f);
    }


}
