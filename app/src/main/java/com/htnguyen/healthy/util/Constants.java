package com.htnguyen.healthy.util;

public class Constants {
    public static final String TABLE_USER = "User";
    public static final String TABLE_CATEGORY = "Category";
    public static final String CHAT_ROOM = "ChatRoom";

    public static final int CATEGORY_WEIGHT = 0;
    public static final int CATEGORY_HEART = 1;
    public static final int CATEGORY_PEDOMETER = 2;

    public interface ACTION {
        String DISMISS_ACTION = "action.dismiss";
        String SNOOZE_ACTION = "action.snooze";
        String CALL_ACTION = "action.call";
        String SMS_ACTION = "action.sms";
        String START_SERVICE = "start.service";
        String STOP_SERVICE = "stop.service";
    }

    //Use public => Outside package
    public interface KEY {
        String TIMER_KEY = "com.timer";
    }

    public interface REQUEST_CODE {
        int CAMERA_REQUEST = 1;
        int GALLERY_REQUEST = 2;
    }
}
