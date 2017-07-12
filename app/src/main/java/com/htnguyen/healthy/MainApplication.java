package com.htnguyen.healthy;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.htnguyen.healthy.util.DbHelper;

public class MainApplication extends Application {
    private static boolean isAppOpen     = false;

    public static boolean isChatActivityOpen() {
        return isAppOpen;
    }

    public static void setChatActivityOpen(boolean isChatActivityOpen) {
        MainApplication.isAppOpen = isChatActivityOpen;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DbHelper.getInstance(this);
    }
}
