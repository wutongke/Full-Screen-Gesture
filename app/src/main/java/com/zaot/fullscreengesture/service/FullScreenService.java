package com.zaot.fullscreengesture.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.zaot.fullscreengesture.ArrayFloatingView;
import com.zaot.fullscreengesture.MainActivity;
import com.zaot.fullscreengesture.R;

public class FullScreenService extends Service {

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

        setupForgroundNotification();
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

    private void setupForgroundNotification() {
        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplication(), 0, activityIntent, 0);
        Notification notification = new Notification.Builder(getApplication()).setAutoCancel(true)
                                                                              .setSmallIcon(R.drawable.ic_launcher_background)
                                                                              .setTicker("FullScreen")
                                                                              .setContentTitle("FullScreen Gesture")
                                                                              .setContentText("FullScreen Gesture")
                                                                              .setWhen(System.currentTimeMillis())
                                                                              .setContentIntent(pendingIntent)
                                                                              .build();
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
