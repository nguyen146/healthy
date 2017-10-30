package com.htnguyen.healthy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.htnguyen.healthy.R;
import com.htnguyen.healthy.model.TimerSender;
import com.htnguyen.healthy.util.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailTimerDialog extends Dialog {
    private OnTimerAcceptListener onTimerAcceptListener;
    @BindView(R.id.status_title)
    TextView statusView;
    @BindView(R.id.txt_title)
    TextInputLayout txtTitle;
    @BindView(R.id.edt_title)
    EditText titleView;
    @BindView(R.id.txt_timer)
    TextInputLayout txtTimer;
    @BindView(R.id.edt_timer)
    EditText timerView;
    @BindView(R.id.edtDescription)
    EditText desciptionView;
    @BindView(R.id.txt_phone)
    TextInputLayout txtPhone;
    @BindView(R.id.edt_phone)
    EditText phoneView;
    private TimerSender timer;
    public DetailTimerDialog(@NonNull Context context, OnTimerAcceptListener onTimerAcceptListener, TimerSender timer) {
        super(context);
        this.onTimerAcceptListener = onTimerAcceptListener;
        this.timer =timer;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_create_timer);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this, getWindow().getDecorView());
        statusView.setText(getContext().getString(R.string.received_timer));
        timerView.setFocusable(false);
        titleView.setFocusable(false);
        phoneView.setFocusable(false);
        desciptionView.setFocusable(false);
        try {
                titleView.setText(timer.getTitle());
                timerView.setText(Tools.convertDateToString(Tools.convertLongToDate(timer.getWakeUpTime())));
            if (timer.getDescription() !=null)
            {
                desciptionView.setText(timer.getDescription());
            }
            else {
                desciptionView.setText("");
            }
            if (timer.getPhoneNumber() !=null){
                phoneView.setText(timer.getPhoneNumber());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btnOk)
    public void onOK(){
        if(Tools.convertLongToDate(timer.getWakeUpTime()).compareTo(Tools.convertStringToDate(Tools.getCurrentDate()))< 0 ){
            txtTimer.setError(getContext().getString(R.string.errTimer));
            return;
        }
        onTimerAcceptListener.onAccept(timer);
        dismiss();
    }

    @OnClick(R.id.btnCancel)
    public void onCancel(){
        dismiss();
    }

    public interface OnTimerAcceptListener{
        void onAccept(TimerSender timer);
    }
}
