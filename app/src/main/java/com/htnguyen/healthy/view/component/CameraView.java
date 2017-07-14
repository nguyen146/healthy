package com.htnguyen.healthy.view.component;

import android.content.Context;
import android.util.AttributeSet;

import org.opencv.android.JavaCameraView;

public class CameraView extends JavaCameraView {

    public CameraView(Context context, int cameraId) {
        super(context, cameraId);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}
