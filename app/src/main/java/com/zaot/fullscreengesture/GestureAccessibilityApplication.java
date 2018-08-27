package com.zaot.fullscreengesture;

import android.app.Application;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

public class GestureAccessibilityApplication extends Application{

    private static String umengKey = "5b839592a40fa31ed8000011";
    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.init(getApplicationContext(), umengKey, null, UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
    }
}
