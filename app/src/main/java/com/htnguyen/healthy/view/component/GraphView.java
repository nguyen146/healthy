package com.htnguyen.healthy.view.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.htnguyen.healthy.R;

import java.util.ArrayList;

public class GraphView extends BarLineChartBase<LineData> implements LineDataProvider {

    public static final int MAX_POINT_IN_GRAPH = 70;
    public static final String COLOR_RED = "#e65030";
    public static final String COLOR_BLUE = "#45c4db";
    public static final String COLOR_GREEN = "#177a6e";
    public static final String COLOR_ORANGE = "#ffa151";

    private ArrayList<Entry> entryPoint;

    public GraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GraphView(Context context) {
        super(context);
        //Set animation (2 second)
        this.animateXY(2000, 2000);
        //Show graph
        this.invalidate();

    }

    @Override
    protected void init() {
        super.init();
        //Set backgoundColor to transparent
        this.setBackgroundColor(Color.TRANSPARENT);
        //Disable touch
        this.setTouchEnabled(false);
        //Disable display description
        this.getDescription().setEnabled(false);
        //Disable display legend
        this.getLegend().setEnabled(false);
        //Disable display background grid
        this.getXAxis().setDrawGridLines(false);
        this.getXAxis().setDrawAxisLine(false);
        this.getXAxis().setDrawLabels(false);
        this.getXAxis().disableGridDashedLine();
        this.getAxisLeft().disableGridDashedLine();
        this.getAxisLeft().setDrawGridLines(false);
        this.getAxisLeft().setDrawAxisLine(false);
        this.getAxisRight().disableGridDashedLine();
        this.getAxisRight().setDrawLabels(false);
        this.getAxisRight().setDrawGridLines(false);
        this.getAxisRight().setAxisLineColor(Color.TRANSPARENT);
        //Custom value number
        YAxis leftYAxis = this.getAxisLeft();
        leftYAxis.setDrawLabels(false);
//        leftYAxis.setDrawLabels(false);
//        leftYAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return value == 0.0f ? "" : String.valueOf((int) value);
//            }
//        });
//        leftYAxis.setAxisMaximum(16f);
//        leftYAxis.setAxisMinimum(0f);
//        leftYAxis.setLabelCount(3);
        leftYAxis.setTextSize(20f);
        mRenderer = new LineChartRenderer(this, mAnimator, mViewPortHandler);
    }

    @Override
    public LineData getLineData() {
        return mData;
    }

    @Override
    protected void onDetachedFromWindow() {
        // releases the bitmap in the renderer to avoid oom error
        if (mRenderer != null && mRenderer instanceof LineChartRenderer) {
            ((LineChartRenderer) mRenderer).releaseBitmap();
        }
        super.onDetachedFromWindow();
    }

    @SuppressLint("DefaultLocale")
    public String getNewPoint() {
        return String.format("%.0f", entryPoint.get(MAX_POINT_IN_GRAPH - 1).getY());
    }


    public void setLineGraphData(int newPointLine) {
        this.setData(getLineData(newPointLine));
        this.notifyDataSetChanged();
        this.invalidate();
    }

    public void setDefaultLineGraphData() {
        //Create empty point
        entryPoint = new ArrayList<>();
        for (int i = 0; i < MAX_POINT_IN_GRAPH; i++) {
            entryPoint.add(new Entry(i, 0));
        }
    }

    /**
     * @param newPoint new entry point in line
     */
    private ArrayList<Entry> addNewEntryPoint(int newPoint) {
        entryPoint = addNewPositionPoint(entryPoint, newPoint);
        return entryPoint;
    }

    /**
     * Swap new point with old point in line graph
     *
     * @param oldEntryPoints data entry point
     * @param newPoint       new point
     */
    private ArrayList<Entry> addNewPositionPoint(ArrayList<Entry> oldEntryPoints, int newPoint) {
        ArrayList<Entry> newEntryPoints = new ArrayList<>();
        for (int i = 0; i < MAX_POINT_IN_GRAPH; i++) {
            if (i == (MAX_POINT_IN_GRAPH - 1)) {
                newEntryPoints.add(new Entry(MAX_POINT_IN_GRAPH - 1, newPoint));
            } else {
                newEntryPoints.add(new Entry(i, oldEntryPoints.get(i + 1).getY()));
            }
        }
        return newEntryPoints;
    }

    /**
     * @param newPointLine new point behind
     * @return data for graph
     */
    private LineData getLineData(int newPointLine) {
        if (entryPoint == null) {
            setDefaultLineGraphData();
        }

        LineDataSet lineDataSet = new LineDataSet(addNewEntryPoint(newPointLine), "");
        lineDataSet.setLineWidth(1f);
//        lineDataSet.setCircleRadius(8f);
//        lineDataSet.setCircleHoleRadius(6f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawValues(true);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.bg_green);
        lineDataSet.setFillDrawable(drawable);
        lineDataSet.setCircleColor(argb(COLOR_BLUE, 100));
        lineDataSet.setColor(argb(COLOR_BLUE, 100));

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(lineDataSet);
        LineData data = new LineData(sets);
        data.setDrawValues(false);
        data.setHighlightEnabled(false);
        return data;
    }

    /**
     * Converts the given hex-color-string to argb.
     */
    private static int argb(String hex, int alpha) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = (color) & 0xFF;
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

}
