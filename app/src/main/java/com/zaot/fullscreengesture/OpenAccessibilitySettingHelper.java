package com.zaot.fullscreengesture;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import java.util.List;

/**
 * 开启无障碍服务帮助类
 */
public class OpenAccessibilitySettingHelper {

    /**
     * 跳转到无障碍服务设置页面
     * @param context 设备上下文
     */
    public static void jumpToSettingPage(Context context){
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 判断是否有辅助功能权限
     * @return true 已开启
     *          false 未开启
     */
    public static boolean isAccessibilitySettingsOn(Context context, String className){
        if (context == null){
            return false;
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices =
            activityManager.getRunningServices(100);// 获取正在运行的服务列表
        if (runningServices.size()<0){
            return false;
        }
        for (int i=0;i<runningServices.size();i++){
            ComponentName service = runningServices.get(i).service;
            if (service.getClassName().equals(className)){
                return true;
            }
        }
        return false;
    }
}
