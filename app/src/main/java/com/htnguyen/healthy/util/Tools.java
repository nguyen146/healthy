package com.htnguyen.healthy.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ImageView;

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
//        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
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

    public static String byeArrayToString(byte[] img){
        return Base64.encodeToString(img, Base64.NO_WRAP);
    }

    public static byte[] stringToByteArray(String array){
        return Base64.decode(array, Base64.NO_WRAP);
    }
}
