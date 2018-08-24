package com.zaot.fullscreengesture;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FullScreenService extends Service {

    public static final String ACTION = "action";
    public static final String SHOW = "show";
    public static final String HIDE = "hide";
    private ArrayFloatingView mFloatingView;

    @Override
    public void onCreate() {
        super.onCreate();
        mFloatingView = new ArrayFloatingView(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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

        if (intent != null) {
            String action = intent.getStringExtra(ACTION);
            if (SHOW.equals(action)) {
                mFloatingView.show();
            } else if (HIDE.equals(action)) {
                mFloatingView.hide();
            }
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) {
            mFloatingView.hide();
        }
    }
}
