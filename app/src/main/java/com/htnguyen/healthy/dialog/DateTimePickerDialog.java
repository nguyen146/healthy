package com.htnguyen.healthy.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.htnguyen.healthy.R;
import com.htnguyen.healthy.util.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DateTimePickerDialog extends Dialog implements View.OnClickListener {
    @BindView(R.id.txtSetDate)
    TextView txtSetDate;
    @BindView(R.id.txtSetTime)
    TextView txtSetTime;
    @BindView(R.id.txtError)
    TextView txtError;
    @BindView(R.id.timePicker)
    TimePicker timePicker;
    @BindView(R.id.datePicker)
    DatePicker datePicker;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.btnOk)
    Button btnOk;
    // Choose "tvSetDate"  mCheckSelected = false, Choose "tvSetTime" mCheckSelected = true.
    private boolean mCheckSelected = false;
    private Activity mActivity;
    private String mDate;
    private String mTime;
    private View mView;

    public DateTimePickerDialog(@NonNull Context context, Activity mActivity, View mView) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_date_time_picker);
        ButterKnife.bind(this);
        setCancelable(true);
        setOnClickListener();
        setDateTimeForTextView();
        setOnChangedListenerForDateTimePicker();
        this.mActivity = mActivity;
        this.mView = mView;
    }

    @Override
    public void onClick(View v) {
//        setDateTimeForTextView();
//        EditText edtEndDate = (EditText) mActivity.findViewById(R.id.edtEndDate);
//        EditText edtStartDate = (EditText) mActivity.findViewById(R.id.edtStartDate);
//        EditText edtWakeUpTime = (EditText) mActivity.findViewById(R.id.edtWakeUpTime);
//        //Begin switch onClick
//        switch (v.getId()) {
//            case R.id.txtSetDate:
//                if (mCheckSelected) {
//                    txtSetDate.setTextColor(mActivity.getResources().
//                            getColor(R.color.colorPrimary));
//                    txtSetTime.setTextColor(mActivity.getResources().
//                            getColor(R.color.colorBlack));
//                    datePicker.setVisibility(View.VISIBLE);
//                    timePicker.setVisibility(INVISIBLE);
//                    mCheckSelected = false;
//                }
//
//                break;
//            case R.id.txtSetTime:
//                if (!mCheckSelected) {
//                    txtSetTime.setTextColor(mActivity.getResources().
//                            getColor(R.color.colorPrimary));
//                    txtSetDate.setTextColor(mActivity.getResources().
//                            getColor(R.color.colorBlack));
//                    datePicker.setVisibility(INVISIBLE);
//                    timePicker.setVisibility(View.VISIBLE);
//                    mCheckSelected = true;
//                }
//                break;
//            case R.id.btnOk:
//                //Begin switch btnOk
//                switch (mView.getId()) {
//                    case R.id.edtEndDate:
//                        //Compare date of datePicker with current date
//                        //When date > current date
//                        if (Tools.convertStringToDate(mDate + " - " + mTime).compareTo(Tools.
//                                convertStringToDate(Tools.getCurrentDate())) >= 0) {
//                            //When start date > end date
//                            if (!edtStartDate.getText().toString().equals("")) {
//                                if (Tools.convertStringToDate(mDate + " - " + mTime).
//                                        compareTo(Tools.convertStringToDate(edtStartDate.
//                                                getText().toString())) < 0) {
//                                    txtError.requestFocus();
//                                    txtError.setError(mActivity.getResources().
//                                            getString(R.string.errorEndDate));
//                                    return;
//                                }
//                            }
//                            edtEndDate.setText(mDate + " - " + mTime);
//                            this.dismiss();
//                        } else {
//                            txtError.requestFocus();
//                            txtError.setError(mActivity.getResources().
//                                    getString(R.string.errorDate));
//                            return;
//                        }
//                        break;
//
//                    case R.id.edtStartDate:
//                        //Compare date of datePicker with current date
//                        //When date > current date
//                        if (Tools.convertStringToDate(mDate + " - " + mTime).compareTo(Tools.
//                                convertStringToDate(Tools.getCurrentDate())) >= 0) {
//                            //When start date > end date
//                            if (!edtEndDate.getText().toString().equals("")) {
//                                if (Tools.convertStringToDate(mDate + " - " + mTime).
//                                        compareTo(Tools.convertStringToDate(edtEndDate.
//                                                getText().toString())) > 0) {
//                                    txtError.requestFocus();
//                                    txtError.setError(mActivity.getResources().
//                                            getString(R.string.errorStartDate));
//                                    return;
//                                }
//                            }
//                            //when start date < wake up time
//                            if (!edtWakeUpTime.getText().toString().equals("")) {
//                                if (Tools.convertStringToDate(mDate + " - " + mTime).
//                                        compareTo(Tools.convertStringToDate(edtWakeUpTime.
//                                                getText().toString())) < 0) {
//                                    txtError.requestFocus();
//                                    txtError.setError(mActivity.getResources().
//                                            getString(R.string.errorStartDateWakeUpTime));
//                                    return;
//                                }
//                            }
//                            edtStartDate.setText(mDate + " - " + mTime);
//                            this.dismiss();
//                        } else {
//                            txtError.requestFocus();
//                            txtError.setError(mActivity.getResources().
//                                    getString(R.string.errorDate));
//                            return;
//                        }
//                        break;
//
//                    case R.id.edtWakeUpTime:
//                        //Compare date of datePicker with current date
//                        //When date > current date
//                        if (Tools.convertStringToDate(mDate + " - " + mTime).compareTo(Tools.
//                                convertStringToDate(Tools.getCurrentDate())) >= 0) {
//                            //When start date > end date
//                            if (!edtStartDate.getText().toString().equals("")) {
//                                if (Tools.convertStringToDate(mDate + " - " + mTime).
//                                        compareTo(Tools.convertStringToDate(edtStartDate.
//                                                getText().toString())) > 0) {
//                                    txtError.requestFocus();
//                                    txtError.setError(mActivity.getResources().
//                                            getString(R.string.errorWakeUpTime));
//                                    return;
//                                }
//                            }
//                            edtWakeUpTime.setText(mDate + " - " + mTime);
//                            this.dismiss();
//                        } else {
//                            txtError.requestFocus();
//                            txtError.setError(mActivity.getResources().
//                                    getString(R.string.errorDate));
//                            return;
//                        }
//                        break;
//                    default:
//                        break;
//                }//End switch btnOk
//                break;
//            case R.id.btnCancel:
//                this.dismiss();
//                break;
//            default:
//                break;
//        }//End switch onClick()
    }

    private void setOnClickListener() {
        txtSetDate.setOnClickListener(this);
        txtSetTime.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    private void setDateTimeForTextView() {
        mTime = Tools.convertDateTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
        mDate = (datePicker.getMonth() + 1) + "/" + datePicker.getDayOfMonth() + "/" +
                datePicker.getYear();
        txtSetDate.setText(mDate);
        txtSetTime.setText(mTime);
    }

    private void setOnChangedListenerForDateTimePicker() {
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                        mDate = monthOfYear + "/" + dayOfMonth + "/" + year;
                    }
                });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mTime = Tools.convertDateTime(hourOfDay, minute);
            }
        });
    }
}
