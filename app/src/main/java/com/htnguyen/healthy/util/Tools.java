package com.htnguyen.healthy.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ImageView;

import com.htnguyen.healthy.R;

import java.io.ByteArrayOutputStream;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Tools {
    //Get date now
    public static String getCurrentDate() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy - hh:mm aa", Locale.US);
        return df.format(Calendar.getInstance().getTime());
    }

    public static Date convertLongToDate(long currentTimeMillis) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy - hh:mm aa", Locale.US);
        return new Date(currentTimeMillis);
    }

    public static long convertDateToLong(Date date){
        return date.getTime();
    }

    public static Date convertStringToDate(String dateString) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy - hh:mm aa", Locale.US);
        Date date = null;
        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Calendar convertDateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static String convertDateToString(Date date) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy - hh:mm aa", Locale.US);
        return df.format(date);
    }

    public static byte[] convertImageViewToByteArray(ImageView imgPicture) {
        BitmapDrawable drawable = (BitmapDrawable) imgPicture.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static byte[] convertDrawableYoByteArray(Context context, int id) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();

    }

    public static Bitmap convertByteArrayToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static String getStringEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static int getRandomInt() {
        Random random = new Random();
        return random.nextInt();
    }

    public static String convertDateTime(int hour, int minute) {
        Time mTime = new Time(hour, minute, 0);
        DateFormat df = new SimpleDateFormat("h:mm aa", Locale.US);
        return df.format(mTime);
    }

    // Category tools
    public static Bitmap convertImageViewToBitmap(ImageView imgStudent) {
        BitmapDrawable drawable = (BitmapDrawable) imgStudent.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        return Bitmap.createScaledBitmap(bitmap, 500, 500, true);
    }

    public static byte[] convertBitmapToByteAray(Bitmap bitmap) {
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static String byeArrayToString(byte[] img) {
        return Base64.encodeToString(img, Base64.DEFAULT);
    }

    public static byte[] stringToByteArray(String array) {
        return Base64.decode(array, Base64.DEFAULT);
    }

    public static double getMaxHeartRate(int age, int gender) {
        //reference http://www.gymlord.com/2014/11/cach-tinh-nhip-tim-toi-da.html
        double max = -1;
        if (age > 130) return -1;
        switch (gender) {
            case 0: //Male
                max = 202 - (0.55 * age);
                break;
            case 1: //Female
                max = 216 - (1.09 * age);
                break;
        }
        return max;
    }

    public static double getWaterNeeded(double weight) {
        if (weight > 500) return -1;
        return weight * 0.03;
    }

    public static double getBMI(double weight, double height) {
        if (weight > 500 || height > 300) return -1;
        double heightM = height/100d;
        return weight / (heightM * heightM);
    }

    public static String getStatusBMI(Context context, double BMI) {
        if (BMI == -1) return null;
        if (BMI < 18.5) return context.getString(R.string.BMI0);
        if (BMI < 24.9) return context.getString(R.string.BMI1);
        if (BMI == 25) return context.getString(R.string.BMI2);
        if (BMI < 29.9) return context.getString(R.string.BMI3);
        if (BMI < 34.9) return context.getString(R.string.BMI4);
        if (BMI < 39.9) return context.getString(R.string.BMI5);
        if (BMI >= 40) return context.getString(R.string.BMI6);
        return null;
    }

    public static double fatInBody(double BMI, int age, int gender) {
        switch (gender) {
            case 0://Male
                return (1.20 * BMI) + (0.23d * age) - 10.8 - 5.4;
            case 1: //Female
                return (1.20 * BMI) + (0.23 * age) - 5.4;
        }
        return -1;
    }

    public static int statusHeartRate(int bmp){
        if(bmp<60) return 0;
        if (bmp<90) return 1;
        return 2;
    }
}
