package com.htnguyen.healthy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.TextView;

import com.htnguyen.healthy.R;
import com.htnguyen.healthy.model.Category;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmDialog extends Dialog{
    @BindView(R.id.title)
    TextView titleView;

    private OnConfirmListener onConfirmListener;
    private final Category category;
    public ConfirmDialog(@NonNull Context context, OnConfirmListener onConfirmListener, String title, Category category) {
        super(context);
        this.onConfirmListener = onConfirmListener;
        this.category = category;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm);
        ButterKnife.bind(this, getWindow().getDecorView());
        titleView.setText(title);
        setCancelable(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @OnClick(R.id.btnOk)
    public void onOK(){
        onConfirmListener.onConfirm(category);
        dismiss();
    }

    @OnClick(R.id.btnCancel)
    public void onCancel(){
        dismiss();
    }

    public interface OnConfirmListener{
        void onConfirm(Category category);
    }
}
