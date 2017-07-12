package com.htnguyen.healthy.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;

import com.htnguyen.healthy.R;
import com.htnguyen.healthy.model.HeartRateModel;
import com.htnguyen.healthy.presenter.HeartRatePresenter;
import com.htnguyen.healthy.util.CaculateHeartRate;
import com.htnguyen.healthy.util.FFT;
import com.htnguyen.healthy.view.HeartRateView;
import com.htnguyen.healthy.view.component.CameraView;
import com.htnguyen.healthy.view.component.GraphView;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HeartRateActivity extends BaseActivity implements HeartRateView, CameraBridgeViewBase.CvCameraViewListener2{

    @Inject
    HeartRatePresenter heartRatePresenter;

    private static final String TAG = "OpenCVCamera";
    private CameraView cameraView;
    private int skip = 0;
    private long startTime;
    private Mat screenRecoder;
    private Mat countBlackColor;
    private long avgBlackColor;
    private int bpmTemp;
    private int bpm;
    List<HeartRateModel> listBmp = new ArrayList<>();
    CaculateHeartRate caculateHeartRate = new CaculateHeartRate();
    List<Integer> listAvgBpm = new ArrayList<>();

    private static final int sampleSize = 256;
    private final CircularFifoQueue sampleQueue = new CircularFifoQueue(sampleSize);
    private final CircularFifoQueue timeQueue = new CircularFifoQueue(sampleSize);
    public final CircularFifoQueue bpmQueue = new CircularFifoQueue(40);
    private final FFT fft = new FFT(sampleSize);

    @BindView(R.id.Bmp)
    TextView bmpView;
    @BindView(R.id.second)
    TextView secondView;
    @BindView(R.id.graph_view)
    GraphView graphView;


    public static Intent getCallingIntent(Context context) {
        return new Intent(context, HeartRateActivity.class);
    }

    private BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    cameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_heart_rate);
        graphView = new GraphView(HeartRateActivity.this);
        heartRatePresenter = new HeartRatePresenter();
        heartRatePresenter.setView(this);
        heartRatePresenter.initializeView();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, baseLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
            screenRecoder = new Mat();
            countBlackColor = new Mat();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        startTime = System.currentTimeMillis();
        cameraView.setEffect(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
    }

    @Override
    public void onCameraViewStopped() {
        cameraView.setEffect(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
//Gray color
//        Imgproc.threshold(inputFrame.gray(), screenRecoder, 123, 255, Imgproc.THRESH_BINARY); gray not support xperia huhu
        //Rba to black white color
        Imgproc.threshold(inputFrame.gray(), screenRecoder, 123, 255,  Imgproc.COLOR_RGB2GRAY);
//        inputFrame.rgba().convertTo(screenRecoder, );
        Imgproc.threshold(inputFrame.rgba() , screenRecoder, 123, 255, Imgproc.THRESH_BINARY);
        //Black white color
        Core.extractChannel(screenRecoder, countBlackColor, 0);
        avgBlackColor = Core.countNonZero(countBlackColor);
        HeartRateModel heartRateModel = new HeartRateModel(avgBlackColor, System.currentTimeMillis());
        heartRatePresenter.updateUi();
        listBmp.add(heartRateModel);
        if ((System.currentTimeMillis() - startTime)/1000d >= 2){
            startTime = System.currentTimeMillis();
            bpmTemp = caculateHeartRate.getBpm(listBmp);
            avgBpm(bpmTemp);
            listBmp.clear();
        }

//        sampleQueue.add(avgBlackColor);
//        timeQueue.add(System.currentTimeMillis());
//        long[] y = new long[sampleSize];
//        long[] x = ArrayUtils.toPrimitive((Long[]) sampleQueue
//                .toArray(new Long[0]));
//        long[] time = ArrayUtils.toPrimitive((Long[]) timeQueue
//                .toArray(new Long[0]));
//        if (timeQueue.size() < sampleSize) {
//            return null;
//        }
//
//        double Fs = ((double) timeQueue.size())
//                / (double) (time[timeQueue.size() - 1] - time[0]) * 1000;
//
//        fft.fft(x, y);
//
//        int low = Math.round((float) (sampleSize * 40 / 60 / Fs));
//        int high = Math.round((float) (sampleSize * 160 / 60 / Fs));
//
//        int bestI = 0;
//        double bestV = 0;
//        for (int i = low; i < high; i++) {
//            double value = Math.sqrt(x[i] * x[i] + y[i] * y[i]);
//
//            if (value > bestV) {
//                bestV = value;
//                bestI = i;
//            }
//        }
//        bpm = Math.round((float) (bestI * Fs * 60 / sampleSize));
//        bpmQueue.add(bpm);
//        int bmptemp;
        return screenRecoder;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        cameraView = (CameraView) findViewById(R.id.camera_view);
        cameraView.setVisibility(SurfaceView.VISIBLE);
        cameraView.setCvCameraViewListener(this);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void renderUi() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bmpView.setText(String.valueOf(bpm));
                graphView.setLineGraphData((int)avgBlackColor);
            }
        });
    }

    public void avgBpm(int newBpm){
        if(newBpm == 0){
            return;
        }
        if (listAvgBpm.size() == 0){
            listAvgBpm.add(newBpm);
            Log.e("OVL", "init "+ newBpm);
            return;
        }
        int caculate = Math.abs(newBpm - listAvgBpm.get(listAvgBpm.size()-1));
        int upDown = newBpm - listAvgBpm.get(listAvgBpm.size()-1); //>0 up  <0 down
        if(caculate<=2){
            listAvgBpm.add(listAvgBpm.get(listAvgBpm.size()-1));
            skip = 0;
        }else if(caculate>2 && caculate <=4){
            if(upDown>0){
                listAvgBpm.add(listAvgBpm.get(listAvgBpm.size()-1) +2);
            }else {
                listAvgBpm.add(listAvgBpm.get(listAvgBpm.size()-1) -2);
            }
            skip = 0;
        }else if(caculate>4 && caculate <=6){
            if(upDown>0){
                listAvgBpm.add(listAvgBpm.get(listAvgBpm.size()-1) +3);
            }else {
                listAvgBpm.add(listAvgBpm.get(listAvgBpm.size()-1) -3);
            }
            skip = 0;
        } else if(caculate>6 && caculate <=10){
            if(upDown>0){
                listAvgBpm.add(listAvgBpm.get(listAvgBpm.size()-1) +4);
            }else {
                listAvgBpm.add(listAvgBpm.get(listAvgBpm.size()-1) -4);
            }
            skip = 0;
        }else {
            skip ++;
        }
        bpm = listAvgBpm.get(listAvgBpm.size()-1);
        Log.e("OVL", "Bpm "+ bpm);
        if(skip >=2 ){
            listAvgBpm.clear();
            Log.e("OVL", "Wrong session clear!! ");
            skip =0;
            return;
//            Toast.makeText(HeartRateActivity.this, "Wrong heart rate progress will be Reset",Toast.LENGTH_SHORT).show();
        }
        if(listAvgBpm.size()>=3){
            int total = 0;
            for(int i =0; i<listAvgBpm.size();i++){
                total += listAvgBpm.get(i);
            }
            if(listAvgBpm.contains(total/(listAvgBpm.size()))){
                Log.e("OVL", "Heart rate"+ total/(listAvgBpm.size()));
                skip =0;
                listAvgBpm.clear();
                return;
            }
        }
        if (listAvgBpm.size()>=4){
            //this get heart rate :))
            Log.e("OVL", "Heart rate"+ listAvgBpm.get(listAvgBpm.size()-1));
//                    Toast.makeText(HeartRateActivity.this, "Heart rate is: " + listAvgBpm.get(listAvgBpm.size()-1),Toast.LENGTH_SHORT).show();
            skip =0;
            listAvgBpm.clear();
            return;
        }

    }

}
