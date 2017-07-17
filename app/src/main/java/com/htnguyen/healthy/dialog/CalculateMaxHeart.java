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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.htnguyen.healthy.R;
import com.htnguyen.healthy.util.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalculateMaxHeart extends Dialog{

    @BindView(R.id.txt_title)
    TextInputLayout txtTitle;
    @BindView(R.id.edt_title)
    EditText titleView;
    @BindView(R.id.gender)
    RadioGroup genderView;
    @BindView(R.id.male)
    RadioButton maleView;

    public CalculateMaxHeart(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_calculate_max_heart);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        ButterKnife.bind(this, getWindow().getDecorView());
        setCancelable(true);
        maleView.setChecked(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @OnClick(R.id.btnOk)
    public void OnOk(){
        if(!checkRequired()) return;

        int checkGender = genderView.getCheckedRadioButtonId();
        int gender;
        switch (checkGender) {
            case R.id.male:
                gender = 0;
                break;
            case R.id.female:
                gender = 1;
                break;
            default:
                gender = 0;
                break;
        }
        int age = 0;
        try {
            age = Integer.parseInt(titleView.getText().toString().trim());
            if(age<0 || age >130){
                txtTitle.setError(getContext().getString(R.string.errage));
                return;
            }
        }catch (Exception e){
            txtTitle.setError(getContext().getString(R.string.errage));
            return;
        }
        double max = Tools.getMaxHeartRate(age,gender);
        String maxheart = String.format("%.2f", max);
        new DialogResult(getContext(), getContext().getString(R.string.your_max_heart)+ " "+ maxheart).show();
        dismiss();
    }

    @OnClick(R.id.btnCancel)
    public void OnCancel(){
        dismiss();
    }

        private boolean checkRequired(){
            if(titleView.getText().toString().trim().length() == 0){
                txtTitle.setError(getContext().getString(R.string.errrequired));
                return false;
            }else {
                txtTitle.setError(null);
            }

            return true;
    }
}
