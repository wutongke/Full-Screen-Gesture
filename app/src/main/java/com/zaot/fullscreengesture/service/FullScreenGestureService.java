package com.zaot.fullscreengesture.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import com.zaot.fullscreengesture.MainActivity;
import com.zaot.fullscreengesture.R;
import com.zaot.fullscreengesture.utils.NotificationBundle;
import com.zaot.fullscreengesture.view.ArrayFloatingView;

public class FullScreenGestureService extends Service {

    public static final String ACTION = "action";
    public static final String SHOW = "show";
    public static final String HIDE = "hide";
    private ArrayFloatingView leftFloatingView;
    private ArrayFloatingView rightFloatingView;
    private ArrayFloatingView bottomFloatingView;

    @Override
    public void onCreate() {
        super.onCreate();
        leftFloatingView = new ArrayFloatingView(this, ArrayFloatingView.LEFT_TYPE);
        rightFloatingView = new ArrayFloatingView(this, ArrayFloatingView.RIGHT_TYPE);
        bottomFloatingView = new ArrayFloatingView(this, ArrayFloatingView.BOTTOM_TYPE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        setupForegroundNotification();
        if (intent != null) {
            String action = intent.getStringExtra(ACTION);
            if (SHOW.equals(action)) {
                leftFloatingView.show();
                rightFloatingView.show();
                bottomFloatingView.show();
            } else if (HIDE.equals(action)) {
                leftFloatingView.hide();
                rightFloatingView.hide();
                bottomFloatingView.hide();
            }
        }

        return START_STICKY;
    }

    private void setupForegroundNotification() {
        Intent activityIntent = new Intent(this, MainActivity.class);
        activityIntent.putExtra(NotificationBundle.TAB_TO_OPEN, NotificationBundle.instrument_tab);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplication(), 0, activityIntent, 0);
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelId = getResources().getString(R.string.app_name);
            String channelName = getResources().getString(R.string.app_name);
            String channelDescription = getResources().getString(R.string.gesture_control_introduce);

            NotificationChannel notificationChannel =
                new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(channelDescription);
            ((NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(
                notificationChannel);

            notification = new Notification.Builder(getApplication(), channelId)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setTicker(getResources().getString(R.string.app_name))
                .setContentTitle(getResources().getString(R.string.keep_fullscreen_gesture))
                .setContentText(getResources().getString(R.string.fullscreen_gesture_notification_tab))
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .build();
        } else {
            notification = new Notification.Builder(getApplication())
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setTicker(getResources().getString(R.string.app_name))
                .setContentTitle(getResources().getString(R.string.keep_fullscreen_gesture))
                .setContentText(getResources().getString(R.string.fullscreen_gesture_notification_tab))
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .build();
        }
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (leftFloatingView != null) {
            leftFloatingView.hide();
            rightFloatingView.hide();
            bottomFloatingView.hide();
        }
    }
}
