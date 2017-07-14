package com.htnguyen.healthy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.htnguyen.healthy.R;
import com.htnguyen.healthy.model.Heart;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmDialogHeartRate extends Dialog{
    @BindView(R.id.title)
    TextView titleView;

    private OnConfirmListener onConfirmListener;
    private Heart heart;
    public ConfirmDialogHeartRate(@NonNull Context context, OnConfirmListener onConfirmListener, String title, Heart heart) {
        super(context);
        this.onConfirmListener = onConfirmListener;
        this.heart = heart;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        ButterKnife.bind(this, getWindow().getDecorView());
        titleView.setText(title);
        setCancelable(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @OnClick(R.id.btnOk)
    public void onOK(){
        onConfirmListener.onConfirm(heart);
        dismiss();
    }

    @OnClick(R.id.btnCancel)
    public void onCancel(){
        dismiss();
    }

    public interface OnConfirmListener{
        void onConfirm(Heart heart);
    }
}
