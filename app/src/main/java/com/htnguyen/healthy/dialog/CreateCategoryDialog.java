package com.htnguyen.healthy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.Window;
import android.widget.EditText;

import com.htnguyen.healthy.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateCategoryDialog extends Dialog{
    @BindView(R.id.txt_title)
    TextInputLayout txtTitle;
    @BindView(R.id.edt_title)
    EditText titleView;
    @BindView(R.id.txt_Namevalue)
    TextInputLayout txtNmaeValue;
    @BindView(R.id.edtNameValue)
    EditText nameValueView;

    private OnCreateCategoryListener onCreateCategoryListener;
    public CreateCategoryDialog(@NonNull Context context, OnCreateCategoryListener onCreateCategoryListener) {
        super(context);
        this.onCreateCategoryListener = onCreateCategoryListener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_create_tracker);
        ButterKnife.bind(this, getWindow().getDecorView());
        setCancelable(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @OnClick(R.id.btnOk)
    public void onOK(){
        if(!checkReauired()) return;
        onCreateCategoryListener.onCreateCategory(titleView.getText().toString().trim(),nameValueView.getText().toString().trim());
        dismiss();
    }

    @OnClick(R.id.btnCancel)
    public void onCancel(){
        dismiss();
    }

    public interface OnCreateCategoryListener{
        void onCreateCategory(String title, String nameValue);
    }

    private boolean checkReauired(){
        boolean valid = true;
        if(titleView.getText().toString().trim().length() == 0){
            valid = false;
            txtTitle.setError(getContext().getString(R.string.errValue));
        }else {
            txtTitle.setError(null);
        }
        if (nameValueView.getText().toString().trim().length() == 0){
            valid = false;
            txtNmaeValue.setError(getContext().getString(R.string.errValue));
        }else {
            txtTitle.setError(null);
        }
        return valid;
    }
}
