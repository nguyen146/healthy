package com.htnguyen.healthy.navigation;

import android.content.Context;
import android.content.Intent;

import com.htnguyen.healthy.view.activity.HeartRateActivity;
import com.htnguyen.healthy.view.activity.MainActivity;
import com.htnguyen.healthy.view.activity.SettingUserActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class used to navigate through the application.
 */
@Singleton
public class Navigator {

    @Inject
    public Navigator() {
        //empty
    }

    public void navigateToHeartRate(Context context) {
        if (context != null) {
            Intent intentToLaunch =  HeartRateActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToHealthy(Context context) {
        if (context != null) {
            Intent intentToLaunch =  MainActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToSettingUser(Context context) {
        if (context != null) {
            Intent intentToLaunch =  SettingUserActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }
}
