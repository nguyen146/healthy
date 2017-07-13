package com.htnguyen.healthy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.Window;
import android.widget.EditText;

import com.htnguyen.healthy.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CreateTimerDialog extends Dialog {

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

    private OnCreateTimerListener onCreateTimerListener;
    public CreateTimerDialog(@NonNull Context context, OnCreateTimerListener onCreateTimerListener) {
        super(context);
        this.onCreateTimerListener = onCreateTimerListener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_create_timer);
        ButterKnife.bind(this, getWindow().getDecorView());
    }

    @OnClick(R.id.btnOk)
    public void onOK(){
        if (!checkRequired()) return;
        onCreateTimerListener.onCreate(titleView.getText().toString().trim(),
                timerView.getText().toString().trim(),
                desciptionView.getText().toString().trim());
        dismiss();
    }

    @OnClick(R.id.btnCancel)
    public void onCancel(){
        dismiss();
    }

    public interface OnCreateTimerListener{
        void onCreate(String title, String date, String description);
    }

    public boolean checkRequired(){
        boolean validate = true;
        if(titleView.getText().toString().trim().length() == 0){
            validate = false;
            txtTitle.setError(getContext().getString(R.string.required_field));
        }else {
            txtTitle.setError(null);
        }

        if(timerView.getText().toString().trim().length() == 0){
            validate = false;
            txtTimer.setError(getContext().getString(R.string.required_field));
        }else {
            txtTimer.setError(null);
        }

        return validate;
    }

    @OnClick(R.id.edt_timer)
    public void onCreateTimer(){
        new DateTimePickerDialog(getContext(),getWindow().getDecorView()).show();
    }
}
