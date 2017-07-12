package com.htnguyen.healthy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.TextView;

import com.htnguyen.healthy.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadingDialog extends Dialog{

    @BindView(R.id.text_loading)
    TextView loadingView;
    public LoadingDialog(@NonNull Context context, String messageLoading) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loading);
        setCancelable(false);
        ButterKnife.bind(this, getWindow().getDecorView());
        loadingView.setText(messageLoading);
    }
}
