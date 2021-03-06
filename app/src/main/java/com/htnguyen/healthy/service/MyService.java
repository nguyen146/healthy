package com.htnguyen.healthy.service;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.htnguyen.healthy.R;
import com.htnguyen.healthy.model.Timer;
import com.htnguyen.healthy.util.Constants;
import com.htnguyen.healthy.util.DbHelper;
import com.htnguyen.healthy.util.Tools;
import com.htnguyen.healthy.view.activity.MainActivity;

import java.util.Calendar;

public class MyService extends Service{
    private Timer mTimer;
    private NotificationManager mNotificationManager;
    private static final int TIME_SNOOZE = 10 * 60 * 1000;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long id = intent.getLongExtra(Constants.KEY.TIMER_KEY, 0);
        if (id ==0){
            return START_NOT_STICKY;
        }
        mTimer = DbHelper.getTimerById(id);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        switch (intent.getAction()) {
            case Constants.ACTION.START_SERVICE:
                showNotification();
                break;
            case Constants.ACTION.DISMISS_ACTION:
                mNotificationManager.cancel(mTimer.getPendingId());
                break;
            case Constants.ACTION.SNOOZE_ACTION:
                mNotificationManager.cancel(mTimer.getPendingId());
                setSnooze(mTimer);
                break;
            case Constants.ACTION.CALL_ACTION:
                Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mTimer.getPhoneNumber()));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent2);
                }
                else {
                    Toast.makeText(this, "Can't call", Toast.LENGTH_SHORT).show();
                }
                mNotificationManager.cancel(mTimer.getPendingId());
                break;
        }
        return START_NOT_STICKY;
    }

    public void showNotification(){
        //Content Intent
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, mTimer.getPendingId(),
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Dismiss action intent
        Intent dismissIntent = new Intent(this, MyService.class);
        dismissIntent.setAction(Constants.ACTION.DISMISS_ACTION);
        dismissIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        dismissIntent.putExtra(Constants.KEY.TIMER_KEY, mTimer.getId());
        PendingIntent dismissPendingIntent = PendingIntent.getService(this, mTimer.getPendingId(),
                dismissIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        //Snooze action intent
        Intent snoozeIntent = new Intent(this, MyService.class);
        snoozeIntent.setAction(Constants.ACTION.SNOOZE_ACTION);
        snoozeIntent.putExtra(Constants.KEY.TIMER_KEY, mTimer.getId());
        PendingIntent snoozePendingIntent = PendingIntent.getService(this, mTimer.getPendingId(),
                snoozeIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blue_heart);

        if(!mTimer.getPhoneNumber().equals("")){
            Intent phoneIntent = new Intent(this, MyService.class);
            phoneIntent.setAction(Constants.ACTION.CALL_ACTION);
            phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            phoneIntent.putExtra(Constants.KEY.TIMER_KEY, mTimer.getId());
            PendingIntent phonePendingIntent = PendingIntent.getService(this, mTimer.getPendingId(),
                    phoneIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle(mTimer.getTitle())
                    .setTicker(mTimer.getTitle())
                    .setSubText(Tools.convertDateToString(mTimer.getWakeUpTime()))
                    .setContentText(mTimer.getDescription())
                    .setSmallIcon(R.drawable.blue_heart)
                    .setLargeIcon(Bitmap.createScaledBitmap(bitmap, 128, 128, false))
                    .setOngoing(true)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .addAction(R.drawable.ic_x, getString(R.string.dismiss),
                            dismissPendingIntent)
                    .addAction(R.drawable.ic_call, getString(R.string.call), phonePendingIntent)
                    .addAction(R.drawable.ic_snooze, getString(R.string.snooze), snoozePendingIntent).
                            build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS;
            mNotificationManager.notify(mTimer.getPendingId(), notification);
        }else {
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle(mTimer.getTitle())
                    .setTicker(mTimer.getTitle())
                    .setSubText(Tools.convertDateToString(mTimer.getWakeUpTime()))
                    .setContentText(mTimer.getDescription())
                    .setSmallIcon(R.drawable.blue_heart)
                    .setLargeIcon(Bitmap.createScaledBitmap(bitmap, 128, 128, false))
                    .setOngoing(true)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .addAction(R.drawable.ic_x, getString(R.string.dismiss),
                            dismissPendingIntent)
                    .addAction(R.drawable.ic_snooze, getString(R.string.snooze), snoozePendingIntent).
                            build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS;
            mNotificationManager.notify(mTimer.getPendingId(), notification);
        }
    }

    private void setSnooze(Timer mTimer) {
        Context context = this.getBaseContext();
        Intent service = new Intent(context, MyService.class);
        service.setAction(Constants.ACTION.START_SERVICE);
        service.putExtra(Constants.KEY.TIMER_KEY, mTimer.getId());
        PendingIntent sender = PendingIntent.getService(context, mTimer.getPendingId(), service, 0);
        AlarmManager alarmManager = (AlarmManager) context.
                getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() +
                TIME_SNOOZE, sender);
        Toast.makeText(context, getString(R.string.snoozeAlert), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
