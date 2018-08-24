package com.zaot.fullscreengesture;

import android.content.Context;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

public class AccessibilityEventHelper {

    public static void trySendAccessibilityEvent(View view, String action) {
        AccessibilityManager accessibilityManager =
            (AccessibilityManager) view.getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (!accessibilityManager.isEnabled()) {
            return;
        }
        AccessibilityEvent event = AccessibilityEvent.obtain(
            AccessibilityEvent.TYPE_GESTURE_DETECTION_END);
        event.setClassName(view.getClass().getName());
        event.setPackageName(view.getContext().getPackageName());
        event.getText().add(Constants.BACK_BUTTON_ACTION);
        view.dispatchPopulateAccessibilityEvent(event);
        accessibilityManager.sendAccessibilityEvent(event);
    }
}
