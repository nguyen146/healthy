package com.htnguyen.healthy.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.htnguyen.healthy.model.Heart;
import com.htnguyen.healthy.model.Timer;
import com.htnguyen.healthy.service.MyService;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class DbHelper {
    private static final String DATABASE_NAME = "Healthy";
    private static final String ID= "id";
    private static final String NAME_ITEM = "nameItem";
    private static final String DESCRIPTION = "descriptionItem";
    private static final String WAKE_UP_TIME = "wakeUpTime";
    private static final String PENDING_ID = "pendingId";
    private static final String TIME_STAMP = "timeStamp";
    private static RealmResults<Timer> timerRealmResults;
    private static Realm sRealm;
    private static DbHelper sInstance = null;

    private DbHelper(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(DATABASE_NAME).schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public static DbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DbHelper(context);
            sRealm = Realm.getDefaultInstance();
        }
        return sInstance;
    }

    public static RealmResults<Timer> getsItemRealmResults(){
        return sRealm.where(Timer.class).findAll();
    }

    public static RealmResults<Heart> getsHeartRealmResults(){
        return sRealm.where(Heart.class).findAllSorted(ID, Sort.DESCENDING);
    }

    public static Timer getTimerById(long id){
        return sRealm.where(Timer.class).equalTo(ID, id).findFirst();
    }

    public static Heart getHeartById(long id){
        return sRealm.where(Heart.class).equalTo(ID, id).findFirst();
    }

    public static boolean addTimer(Activity activity, Timer timer){
        try {
            sRealm.beginTransaction();
            sRealm.copyToRealm(timer);
            scheduleNotification(activity, timer);
            sRealm.commitTransaction();
            return true;
        }catch (Exception e){
            sRealm.commitTransaction();
        }

        return false;
    }

    public static boolean addHeart(Heart heart){
        try {
            sRealm.beginTransaction();
            sRealm.copyToRealm(heart);
            sRealm.commitTransaction();
            return true;
        }catch (Exception e){
            sRealm.commitTransaction();
        }

        return false;
    }


    public static boolean deleteHeart(Heart heart){
        try {
            sRealm.beginTransaction();
            RealmQuery<Heart> query = sRealm.where(Heart.class);
            query.equalTo(ID, heart.getId());
            RealmResults realmResults = query.findAll();
            realmResults.deleteAllFromRealm();
            sRealm.commitTransaction();
            return true;
        }catch (Exception e){
            sRealm.commitTransaction();
        }
        return false;
    }

    public static boolean deleteTimer(Activity activity, Timer timer){
        try {
            sRealm.beginTransaction();
            RealmQuery<Timer> query = sRealm.where(Timer.class);
            query.equalTo(ID, timer.getId());
            RealmResults realmResults = query.findAll();
            NotificationManager mNotificationManager = (NotificationManager) activity.
                    getSystemService(Context.NOTIFICATION_SERVICE);
            cancelAlarmWakeUp(activity, timer);
            mNotificationManager.cancel(timer.getPendingId());
            realmResults.deleteAllFromRealm();
            sRealm.commitTransaction();
            return true;
        }catch (Exception e){
            sRealm.commitTransaction();
        }
        return false;
    }

    public static int getRandomPendingId() {
        int id;
        do {
            id = Tools.getRandomInt();
        } while (checkPendingIdExist(id));
        return id;
    }

    private static boolean checkPendingIdExist(int id) {
        RealmResults<Timer> mItem = sRealm.where(Timer.class).equalTo(PENDING_ID, id).findAll();
        return mItem.size() > 0;
    }

    private static void scheduleNotification(Activity mActivity, Timer mTimer) {
        Intent service = new Intent(mActivity, MyService.class);
        service.setAction(Constants.ACTION.START_SERVICE);
        service.putExtra(Constants.KEY.TIMER_KEY, mTimer.getId());
        PendingIntent sender = PendingIntent.getService(mActivity, mTimer.getPendingId(), service,
                0);
        AlarmManager alarmManager = (AlarmManager) mActivity.getBaseContext().
                getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, Tools.convertDateToCalendar(mTimer.getWakeUpTime())
                .getTimeInMillis(), sender);
    }

    private static void cancelAlarmWakeUp(Activity mActivity, Timer mTimer) {
        Intent service = new Intent(mActivity, MyService.class);
        service.setAction(Constants.ACTION.START_SERVICE);
        service.putExtra(Constants.KEY.TIMER_KEY, mTimer.getId());
        PendingIntent sender = PendingIntent.getService(mActivity, mTimer.getPendingId(), service,
                0);
        AlarmManager alarmManager = (AlarmManager) mActivity.getBaseContext().
                getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
