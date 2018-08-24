package com.zaot.fullscreengesture;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
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

    private Context applicationContext;
    private ViewGroup layoutView;
    private ImageView arrayView;
    private int touchStartX, touchStartY;
    private WindowManager.LayoutParams parentLayoutParams;
    private FloatingManager windowManager;
    private RelativeLayout.LayoutParams layoutParams;

    public static final int LEFT_TYPE = 1;
    public static final int RIGHT_TYPE = 2;
    public static final int BOTTOM_TYPE = 3;

    private static int DEFAULT_TYPE = LEFT_TYPE;
    private int type = DEFAULT_TYPE;
    private int thresholdMaxValue = 100;
    private int thresholdMiniValue = 50;
    private int bottomStart;
    private int rightStart;
    private int viewWidth = 60;
    private int viewHeight = 60;

    public ArrayFloatingView(Context context) {
        this(context, DEFAULT_TYPE);
    }

    public ArrayFloatingView(Context context, int type) {
        super(context);
        this.type = type;
        applicationContext = context.getApplicationContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutView = (ViewGroup) layoutInflater.inflate(R.layout.floating_view, null);
        arrayView = layoutView.findViewById(R.id.arrow);
        layoutView.setOnTouchListener(mOnTouchListener);
        windowManager = FloatingManager.getInstance(applicationContext);

        WindowManager windowManager = (WindowManager) applicationContext.getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        bottomStart = displayMetrics.heightPixels - viewHeight;
        rightStart = displayMetrics.widthPixels - viewWidth;
        parentLayoutParams = new WindowManager.LayoutParams();
        layoutParams = (RelativeLayout.LayoutParams) arrayView.getLayoutParams();

        arrayView.setImageResource(type == LEFT_TYPE
                                   ? R.drawable.right_arrow
                                   : type == RIGHT_TYPE ? R.drawable.arrow_left : R.drawable.arrow_on);
    }

    public void show() {
        initParentLayoutParams();
        windowManager.addView(layoutView, parentLayoutParams);
    }

    public void hide() {
        windowManager.removeView(layoutView);
    }

    private void initParentLayoutParams() {
        resetParentLayoutParams();
        parentLayoutParams.gravity = Gravity.TOP | Gravity.START;
        parentLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        parentLayoutParams.format = PixelFormat.RGBA_8888;
        parentLayoutParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
    }

    private void resetParentLayoutParams() {
        switch (type) {
            case LEFT_TYPE:
                parentLayoutParams.x = 0;
                parentLayoutParams.y = 0;
                parentLayoutParams.width = LayoutParams.WRAP_CONTENT;
                parentLayoutParams.height = LayoutParams.MATCH_PARENT;
                break;
            case RIGHT_TYPE:
                parentLayoutParams.x = rightStart;
                parentLayoutParams.y = 0;
                parentLayoutParams.width = LayoutParams.WRAP_CONTENT;
                parentLayoutParams.height = LayoutParams.MATCH_PARENT;
                break;
            case BOTTOM_TYPE:
                parentLayoutParams.x = 0;
                parentLayoutParams.y = bottomStart;
                parentLayoutParams.width = LayoutParams.MATCH_PARENT;
                parentLayoutParams.height = LayoutParams.WRAP_CONTENT;
                break;
        }
    }

    private OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchStartX = (int) event.getRawX();
                    touchStartY = (int) event.getRawY();
                    switch (type) {
                        case LEFT_TYPE:
                            layoutParams.topMargin = touchStartY;
                            layoutParams.leftMargin = 0;
                            layoutParams.bottomMargin = 0;
                            layoutParams.rightMargin = 0;
                            break;
                        case RIGHT_TYPE:
                            layoutParams.topMargin = touchStartY;
                            layoutParams.leftMargin = 0;
                            layoutParams.bottomMargin = 0;
                            layoutParams.rightMargin = 0;
                            break;
                        case BOTTOM_TYPE:
                            layoutParams.leftMargin = touchStartX;
                            layoutParams.topMargin = 0;
                            layoutParams.bottomMargin = 0;
                            layoutParams.rightMargin = 0;
                            break;
                    }
                    arrayView.setLayoutParams(layoutParams);
                    break;
                case MotionEvent.ACTION_MOVE:
                    switch (type) {
                        case LEFT_TYPE:
                            parentLayoutParams.x += (int) event.getRawX() - touchStartX;
                            parentLayoutParams.x = Math.min(parentLayoutParams.x, thresholdMaxValue);
                            if (parentLayoutParams.x >= thresholdMiniValue) {
                                arrayView.setVisibility(VISIBLE);
                            }
                            break;
                        case RIGHT_TYPE:
                            parentLayoutParams.x += (int) event.getRawX() - touchStartX;
                            parentLayoutParams.x = Math.max(parentLayoutParams.x, rightStart - thresholdMaxValue);
                            if (parentLayoutParams.x <= rightStart - thresholdMiniValue) {
                                arrayView.setVisibility(VISIBLE);
                            }
                            break;
                        case BOTTOM_TYPE:
                            parentLayoutParams.y += (int) event.getRawY() - touchStartY;
                            parentLayoutParams.y = Math.max(parentLayoutParams.y, bottomStart - thresholdMaxValue);
                            if (parentLayoutParams.y <= bottomStart - thresholdMiniValue) {
                                arrayView.setVisibility(VISIBLE);
                            }
                            break;
                    }
                    arrayView.setLayoutParams(layoutParams);
                    windowManager.updateView(layoutView, parentLayoutParams);
                    touchStartX = (int) event.getRawX();
                    touchStartY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    switch (type) {
                        case LEFT_TYPE:
                            if (parentLayoutParams.x >= thresholdMaxValue) {
                                AccessibilityEventHelper.trySendAccessibilityEvent(layoutView,
                                                                                   Constants.BACK_BUTTON_ACTION);
                            }
                            break;
                        case RIGHT_TYPE:
                            if (parentLayoutParams.x <= rightStart - thresholdMaxValue) {
                                AccessibilityEventHelper.trySendAccessibilityEvent(layoutView,
                                                                                   Constants.BACK_BUTTON_ACTION);
                            }
                            break;
                        case BOTTOM_TYPE:
                            if (parentLayoutParams.y <= bottomStart - thresholdMaxValue) {
                                AccessibilityEventHelper.trySendAccessibilityEvent(layoutView,
                                                                                   Constants.BACK_HOME_ACTION);
                            }
                            break;
                    }
                    arrayView.setVisibility(INVISIBLE);
                    resetParentLayoutParams();
                    windowManager.updateView(layoutView, parentLayoutParams);
                    break;
            }
            return true;
        }
    };

}
