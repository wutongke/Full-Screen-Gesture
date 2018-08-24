package com.zaot.fullscreengesture;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ArrayFloatingView extends FrameLayout {

    private Context mContext;
    private ViewGroup mView;
    private ImageView mImageView;
    private int mTouchStartX, mTouchStartY;//手指按下时坐标
    private WindowManager.LayoutParams mLayoutParams;
    private FloatingManager mWindowManager;
    private RelativeLayout.LayoutParams layoutParams;

    private int thresholdMaxValue = 100;
    private int thresholdMiniValue = 50;

    public ArrayFloatingView(Context context) {
        super(context);
        mContext = context.getApplicationContext();
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        mView = (ViewGroup) mLayoutInflater.inflate(R.layout.floating_view, null);
        mImageView = mView.findViewById(R.id.imageview);
        mView.setOnTouchListener(mOnTouchListener);
        mWindowManager = FloatingManager.getInstance(mContext);
    }

    public void show() {
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;
        //总是出现在应用程序窗口之上
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置图片格式，效果为背景透明
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mLayoutParams.width = LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = LayoutParams.MATCH_PARENT;
        mWindowManager.addView(mView, mLayoutParams);
        layoutParams = (RelativeLayout.LayoutParams) mImageView.getLayoutParams();
    }

    public void hide() {
        mWindowManager.removeView(mView);
    }

    private OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchStartX = (int) event.getRawX();
                    mTouchStartY = (int) event.getRawY();
                    layoutParams.topMargin = mTouchStartY;
                    break;
                case MotionEvent.ACTION_MOVE:
                    mLayoutParams.x += (int) event.getRawX() - mTouchStartX;
                    mLayoutParams.x = Math.min(mLayoutParams.x, thresholdMaxValue);
                    if (mLayoutParams.x >= thresholdMiniValue) {
                        mImageView.setVisibility(VISIBLE);
                    }
                    mImageView.setLayoutParams(layoutParams);
                    mWindowManager.updateView(mView, mLayoutParams);
                    mTouchStartX = (int) event.getRawX();
                    break;
                case MotionEvent.ACTION_UP:
                    if (mLayoutParams.x >= thresholdMaxValue) {
                        AccessibilityEventHelper.trySendAccessibilityEvent(mView, Constants.BACK_BUTTON_ACTION);
                    }
                    mImageView.setVisibility(INVISIBLE);
                    mLayoutParams.x = 0;
                    mWindowManager.updateView(mView, mLayoutParams);
                    break;
            }
            return true;
        }
    };

}
