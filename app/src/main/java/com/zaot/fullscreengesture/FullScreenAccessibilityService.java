package com.zaot.fullscreengesture;

import android.accessibilityservice.AccessibilityService;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class FullScreenAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event.getEventType() == AccessibilityEvent.TYPE_GESTURE_DETECTION_END && event.getText().size() > 0) {
            String action = event.getText().get(0).toString();
            DLog.I("AccessibilityEvent: " + action);
            switch (action) {
                case Constants.BACK_BUTTON_ACTION:
                    performGlobalAction(GLOBAL_ACTION_BACK);
                    break;
                case Constants.BACK_HOME_ACTION:
                    performGlobalAction(GLOBAL_ACTION_HOME);
                    break;
                case Constants.RECENT_APPLICATION_ACTION:
                    performGlobalAction(GLOBAL_ACTION_RECENTS);
                    break;

            }
        }
    }

    @Override
    public void onInterrupt() {
        DLog.I("FullScreenAccessibilityService onInterrupt");
    }
}
